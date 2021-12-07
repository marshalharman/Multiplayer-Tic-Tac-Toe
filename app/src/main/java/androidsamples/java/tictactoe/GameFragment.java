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
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class GameFragment extends Fragment {
  private static final String TAG = "GameFragment";
  private static final int GRID_SIZE = 9;

  private final Button[] mButtons = new Button[GRID_SIZE];
  private boolean[] mBtnAvailable = new boolean[GRID_SIZE];
  private NavController mNavController;
  private String mGameType;
  private FirebaseAuth mAuth;
  private DatabaseReference mDatabase;
  private int mTurn;
  private GameInfo mGameInfo;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    // Extract the argument passed with the action in a type-safe way
    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    Log.d(TAG, "New game type = " + args.getGameType());
    mGameType = args.getGameType();

    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");

        // TODO show dialog only when the game is still in progress
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
            .setTitle(R.string.confirm)
            .setMessage(R.string.forfeit_game_dialog_message)
            .setPositiveButton(R.string.yes, (d, which) -> {
              // TODO update loss count
              int losses = SharedPrefs.losses(getContext());
              losses++;
              mDatabase.child("users").child(mAuth.getUid()).child("losses").setValue(losses);
              SharedPrefs.setLosses(getContext(), losses);

              mNavController.popBackStack();
            })
            .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
            .create();
        dialog.show();
      }
    };
    requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


    mAuth = FirebaseAuth.getInstance();
    mTurn = 0;
    mDatabase = FirebaseDatabase.getInstance().getReference();
    addPostEventListener();
//    mGameInfo = new GameInfo(SharedPrefs.email(getContext()));
    Arrays.fill(mBtnAvailable, true);
    String temp = "Hello";

    if(mGameType.equals("Two-Player")){
//      GameInfo game = new GameInfo(SharedPrefs.email(getContext()));
      String userId = mAuth.getCurrentUser().getUid();
//      mDatabase.child("games").child(userId).push().setValue(game);
      mDatabase.child("games").child(userId).child("gameState").setValue(0);
      mDatabase.child("games").child(userId).child("lastAction").setValue(-1);
      mDatabase.child("games").child(userId).child("p2Joined").setValue(false);
      mDatabase.child("games").child(userId).child("player1").setValue(SharedPrefs.email(getContext()));
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_game, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mNavController = Navigation.findNavController(view);

    mButtons[0] = view.findViewById(R.id.button0);
    mButtons[1] = view.findViewById(R.id.button1);
    mButtons[2] = view.findViewById(R.id.button2);

    mButtons[3] = view.findViewById(R.id.button3);
    mButtons[4] = view.findViewById(R.id.button4);
    mButtons[5] = view.findViewById(R.id.button5);

    mButtons[6] = view.findViewById(R.id.button6);
    mButtons[7] = view.findViewById(R.id.button7);
    mButtons[8] = view.findViewById(R.id.button8);

    for (int i = 0; i < mButtons.length; i++) {
      int finalI = i;
      mButtons[finalI].setOnClickListener(v -> {
        Log.d(TAG, "Button " + finalI + Boolean.toString(mBtnAvailable[finalI]));
       // TODO implement listeners
        int result = 0;
        if(mBtnAvailable[finalI]){
          if (mGameType.equals("One-Player")) {
            Log.d("GAMEFRAGMENT", "oneplayer");

            //player move
              mButtons[finalI].setText("X");
              mBtnAvailable[finalI] = false;
              result = isTerminalState("X");

              if (result == 1 || result == 2) {
                int wins = SharedPrefs.wins(getContext());
                String response = "";
                if (result == 2) {
                  wins++;
                  response = "Congratulations, You Won!";
                  mDatabase.child("users").child(mAuth.getUid()).child("wins").setValue(wins);
                  SharedPrefs.setWins(getContext(), wins);
                } else {
                  response = "Draw";
                }
                showDialog(response);
              }
              else{
                computerMove();
                result = isTerminalState("O");
                if (result == 1 || result == 2) {
                  Log.d("COMPUTERTURN", Integer.toString(result));
                  int losses = SharedPrefs.losses(getContext());
                  String response = "";
                  if (result == 2) {
                    losses++;
                    response = "Sorry, You Lost!";
                    mDatabase.child("users").child(mAuth.getUid()).child("losses").setValue(losses);
                    SharedPrefs.setLosses(getContext(), losses);
                  } else {
                    response = "Draw";
                  }
                  Log.d("COMPUTERTURN", response);
                  showDialog(response);
              }
            }
          }
          else{
            if(mTurn == 0){
              mButtons[finalI].setText("X");

            }
          }
        }
//        else{
//          if( mTurn == 0){
//            mButtons[finalI].setText("X");
//          }
//          else{
//            mButtons[finalI].setText("O");
//          }
//          FirebaseUser user = mAuth.getCurrentUser();
//          String userId = user.getUid();
//// Set the last action and current state of game in db
//          mGameInfo.setLastMove(finalI);
//          mGameInfo.setGameState(result);
//
//          mDatabase.child("users").child(userId).push().setValue(mGameInfo);
//        }
        });
    }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }

  public void showDialog(String response){
    // create the dialog
    AlertDialog dialog = new AlertDialog.Builder(requireActivity())
            .setTitle(R.string.game_over)
            .setMessage(response)
            .setNeutralButton(R.string.ok, (d, which) -> {
              d.dismiss();
              mNavController.popBackStack();
            })
            .create();
    dialog.show();
  }

  public void computerMove(){
    for(int i=0;i<3;i++){
      for(int j=0;j<3;j++){
        if(mButtons[3*i+j].getText().equals("")){
          mButtons[3*i+j].setText("O");
          mBtnAvailable[3*i+j] = false;
          Log.d("ComputerMove", Integer.toString(i) + ":" + Integer.toString(j));
          return;
        }
      }
    }
  }

  /**
   * Checks if given state is a win/draw position or not
   * @return
   */
  public int isTerminalState(String sign){
    int isTS = 0;
    String c = sign;
//    if( mTurn == 0){
//      c = "X";
//    }
//    else c = "O";

    boolean cellAvailable = false;

    // ROW Check
    for(int i=0;i<3;i++){
      int count = 0;
      for(int j=0;j<3;j++){
        if(mButtons[3*i+j].getText().equals(c)){
          count++;
        }
        if(mButtons[3*i+j].getText().equals("") ){
          cellAvailable = true;
        }
      }
      if(count == 3){
        isTS = 2;
        break;
      }
    }

    // COLUMN Check
    for(int i=0;i<3;i++){
      int count = 0;
      for(int j=0;j<=6;j+=3){
        if(mButtons[i+j].getText().equals(c)){
          count++;
        }
        if(mButtons[i+j].getText().equals("") ){
          cellAvailable = true;
        }
      }
      if(count == 3){
        isTS = 2;
        break;
      }
    }

    //DIAGONALS Check
    if(mButtons[0].getText().equals(c) && mButtons[4].getText().equals(c) && mButtons[8].getText().equals(c) ){
      isTS = 2;
    }
    if(mButtons[2].getText().equals(c) && mButtons[4].getText().equals(c) && mButtons[6].getText().equals(c) ){
      isTS = 2;
    }

    if(!cellAvailable && isTS == 0){
      isTS = 1;
    }

    return isTS;
  }

  private void addPostEventListener() {
    ValueEventListener postListener = new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // Get Post object and use the values to update the UI
        mGameInfo = dataSnapshot.getValue(GameInfo.class);
//        mTurn = 1-mTurn;
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
      }
    };
    mDatabase.addValueEventListener(postListener);
  }
}