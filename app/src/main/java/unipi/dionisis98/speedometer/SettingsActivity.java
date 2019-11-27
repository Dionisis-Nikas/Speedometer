package unipi.dionisis98.speedometer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    // Preference Keys
    public static final String KEY_PREF_SPEED_LIMIT = "speedlimit";
    public static final String KEY_PREF_METRIC = "metrics";
    public static final String KEY_PREF = "sharedPrefs";
    // Shared preference
    SharedPreferences mSharedPreferences;

    EditTextPreference mPreferenceSpeed;
    ListPreference mPreferenceMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Shared preference
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        // Leguage
        String name = mSharedPreferences.getString(KEY_PREF_SPEED_LIMIT, "");

        Toast toast = Toast.makeText(this,name,Toast.LENGTH_LONG);
        toast.show();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }




}