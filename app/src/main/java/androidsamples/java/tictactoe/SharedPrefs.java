package androidsamples.java.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.annotations.NotNull;

public class SharedPrefs {

    private static final String PREFS_KEY_WINS = "PREFS_KEY_WINS";
    private static final String PREFS_KEY_LOSSES = "PREFS_KEY_LOSSES";
    private static final String PREFS_KEY_EMAIL = "PREFS_KEY_EMAIL";
    private static final String SHARED_PREF_FILE = "androidsamples.java.tictactoe.SHARED_PREF_FILE";


    private static SharedPreferences getSharedPrefs(@NotNull Context context) {
        return context
                .getSharedPreferences(SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);
    }

    static int wins (@NotNull Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        int wins = prefs.getInt(PREFS_KEY_WINS, 0);
        return wins;
    }

    static void setWins(@NotNull Context context, int wins) {
        SharedPreferences prefs = getSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREFS_KEY_WINS, wins);
        editor.apply();
    }

    static int losses (@NotNull Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        int wins = prefs.getInt(PREFS_KEY_LOSSES, 0);
        return wins;
    }

    static void setLosses(@NotNull Context context, int losses) {
        SharedPreferences prefs = getSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREFS_KEY_LOSSES, losses);
        editor.apply();
    }

    static void setEmail(@NotNull Context context, String email){
        SharedPreferences prefs = getSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_KEY_EMAIL, email);
        editor.apply();
        editor.commit();
    }

    static String email (@NotNull Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        String email = prefs.getString(PREFS_KEY_EMAIL, "?");
        return email;
    }

}
