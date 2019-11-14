package ar.edu.itba.hci.hoh.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;

// TODO: VER SI FALTA ALGUN SETTINGS MAS
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        SwitchPreferenceCompat notifPreference = findPreference("notifications");
        if (notifPreference != null){
            notifPreference.setChecked(MainActivity.isNotifications());
        }

        EditTextPreference apiPreference = findPreference("api_url");
        if (apiPreference != null) {
            apiPreference.setText(Api.getURL());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("notifications")) {
            MainActivity.setNotifications(sharedPreferences.getBoolean("notifications", MainActivity.isNotifications()));
        } else if (key.equals("api_url")) {
            Api.setURL(sharedPreferences.getString("api_url", Api.getURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
