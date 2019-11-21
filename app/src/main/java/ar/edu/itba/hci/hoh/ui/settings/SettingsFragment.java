package ar.edu.itba.hci.hoh.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import ar.edu.itba.hci.hoh.MainActivity;
import ar.edu.itba.hci.hoh.R;
import ar.edu.itba.hci.hoh.api.Api;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ListPreference timePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);

        boolean initialNotif = MainActivity.isNotifications();

        SwitchPreferenceCompat notifPreference = findPreference("notifications");
        if (notifPreference != null){
            notifPreference.setChecked(initialNotif);
        }

        EditTextPreference apiPreference = findPreference("api_url");
        if (apiPreference != null) {
            apiPreference.setText(Api.getURL());
        }

        timePreference = findPreference("notifications_time");
        if (timePreference != null) {
            timePreference.setValue(String.valueOf(MainActivity.getNotificationsTime()));
            timePreference.setEnabled(initialNotif);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "notifications":
                boolean newNotif = sharedPreferences.getBoolean("notifications", MainActivity.isNotifications());
                MainActivity.setNotifications(newNotif);
                if (timePreference != null) timePreference.setEnabled(newNotif);
                break;
            case "api_url":
                Api.setURL(sharedPreferences.getString("api_url", Api.getURL()));
                break;
            case "notifications_time":
                MainActivity.setNotificationsTime(Integer.valueOf(sharedPreferences.getString("notifications_time", "15")));
                break;
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
