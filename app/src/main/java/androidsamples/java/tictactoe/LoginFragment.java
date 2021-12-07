package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mEmail;
    private EditText mPassword;

//
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO if a user is logged in, go to Dashboard
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEmail = view.findViewById(R.id.edit_email);
        mPassword = view.findViewById(R.id.edit_password);
        view.findViewById(R.id.btn_log_in)
                .setOnClickListener(v -> {
                    // TODO implement sign in logic

                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();

                    if(email.equals("")){
                        Toast.makeText(getActivity(), "Enter your email address." ,
                                Toast.LENGTH_LONG).show();
                    }
                    else if(password.equals("")){
                        Toast.makeText(getActivity(), "Enter the password." ,
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        createAccount(email,password, v);
                    }
//                    if(mAuth.getCurrentUser() != null){
//                        NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
//                        Navigation.findNavController(v).navigate(action);
//                    }
                });

        return view;
    }

    // No options menu in login fragment.
    public void createAccount(String email, String password, View v){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // create entry in user database
                            String userId = user.getUid();


//                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            PlayerInfo newUser = new PlayerInfo(email);
//                            mDatabase.child("users").child(userId).push().setValue(newUser);

                            mDatabase.child("users").child(userId).child("wins").setValue(newUser.getWins());
                            mDatabase.child("users").child(userId).child("losses").setValue(newUser.getLosses());
                            mDatabase.child("users").child(userId).child("email").setValue(newUser.getEmail());

                            SharedPrefs.setEmail(getContext(), email);
                            SharedPrefs.setWins(getContext(), newUser.getWins());
                            SharedPrefs.setLosses(getContext(), newUser.getLosses());


                            NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                            Navigation.findNavController(v).navigate(action);

                        } else {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")){
                                signIn(email,password, v);
                            }
                            else{
                            Toast.makeText(getActivity(), "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                           }
                        }
                    }
                });
    }

    public void signIn(String email, String password,View v){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity() , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            mDatabase.child("users").child(userId).child("wins").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                                        PlayerInfo userInfo = task.getResult().getValue(PlayerInfo.class);
                                        int wins = task.getResult().getValue(Integer.class);
                                        SharedPrefs.setWins(getContext(),wins);
                                    }
                                }
                            });
                            mDatabase.child("users").child(userId).child("losses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        int losses = task.getResult().getValue(Integer.class);
                                        SharedPrefs.setLosses(getContext(),losses);
                                    }
                                }
                            });

                            mDatabase.child("users").child(userId).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                                        PlayerInfo userInfo = task.getResult().getValue(PlayerInfo.class);
                                        String email = task.getResult().getValue(String.class);
                                        SharedPrefs.setEmail(getContext(),email);


                                        NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                                        Navigation.findNavController(v).navigate(action);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}