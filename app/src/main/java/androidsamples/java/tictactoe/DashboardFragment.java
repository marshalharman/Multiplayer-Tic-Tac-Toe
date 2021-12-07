package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardFragment extends Fragment implements OpenGamesAdapter.OnGameClickListener{

  private static final String TAG = "DashboardFragment";
  private NavController mNavController;
  private FirebaseAuth mAuth;
  private DatabaseReference mDatabase;
  private TextView mTxtScore;
  private PlayerInfo mPlayerInfo;
  RecyclerView recyclerView;
  OpenGamesAdapter mAdapter;
  ArrayList<String> mList;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public DashboardFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    mAuth = FirebaseAuth.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
    recyclerView = view.findViewById(R.id.list);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) );

    mList = new ArrayList<String>();
    mAdapter = new OpenGamesAdapter(getContext(),mList, this);
    recyclerView.setAdapter(mAdapter);

    mDatabase = FirebaseDatabase.getInstance().getReference("games");
//    mDatabase.getDatabase().getReference("Games");
    mDatabase.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for( DataSnapshot dataSnapshot : snapshot.getChildren()){
//          GameInfo game = dataSnapshot.getValue(GameInfo.class);
          HashMap<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
//          mList.add(td.);
          for(HashMap.Entry m : td.entrySet()){
//            System.out.println(m.getKey()+" "+m.getValue());
            if(m.getKey().equals("player1")){
              mList.add(m.getValue().toString());
            }
          }
        }
        mAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    return view;

  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mNavController = Navigation.findNavController(view);
    mTxtScore = view.findViewById(R.id.txt_score);

    // TODO if a user is not logged in, go to LoginFragment
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if(currentUser == null){
      NavController navController = Navigation.findNavController(view);
      navController.navigate(R.id.loginFragment);
    }
    updateUI();



    // Show a dialog when the user clicks the "new game" button
    view.findViewById(R.id.fab_new_game).setOnClickListener(v -> {

      // A listener for the positive and negative buttons of the dialog
      DialogInterface.OnClickListener listener = (dialog, which) -> {
        String gameType = "No type";
        if (which == DialogInterface.BUTTON_POSITIVE) {
          gameType = getString(R.string.two_player);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
          gameType = getString(R.string.one_player);
        }
        Log.d(TAG, "New Game: " + gameType);

        // Passing the game type as a parameter to the action
        // extract it in GameFragment in a type safe way
        NavDirections action = DashboardFragmentDirections.actionGame(gameType);
        mNavController.navigate(action);

      };

      // create the dialog
      AlertDialog dialog = new AlertDialog.Builder(requireActivity())
          .setTitle(R.string.new_game)
          .setMessage(R.string.new_game_dialog_message)
          .setPositiveButton(R.string.two_player, listener)
          .setNegativeButton(R.string.one_player, listener)
          .setNeutralButton(R.string.cancel, (d, which) -> d.dismiss())
          .create();
      dialog.show();
    });
  }

  public void updateUI(){
    int wins = SharedPrefs.wins(getContext());
    int losses = SharedPrefs.losses(getContext());
    mTxtScore.setText(String.format("Wins: %d    Losses: %d", wins, losses));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }

  @Override
  public void onGameClick(int position) {
//    mList.get(position);
    NavDirections action = DashboardFragmentDirections.actionGame("Two-Player");
    mNavController.navigate(action);
  }
}