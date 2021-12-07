package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private FirebaseAuth mAuth;
  private PlayerInfo mPlayerInfo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    if (currentFragment == null) {
      Fragment fragment = new DashboardFragment();
      getSupportFragmentManager()
              .beginTransaction()
              .add(R.id.nav_host_fragment, fragment)
              .commit();
    }
//    mPlayerInfo = new PlayerInfo();
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_logout) {
      Log.d(TAG, "logout clicked");
      // TODO handle log out
      FirebaseAuth.getInstance().signOut();
      Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
      NavController navController = Navigation.findNavController(currentFragment.getView());
      navController.navigate(R.id.loginFragment);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

//  public PlayerInfo getPlayerInfo() {
//    return mPlayerInfo;
//  }
}