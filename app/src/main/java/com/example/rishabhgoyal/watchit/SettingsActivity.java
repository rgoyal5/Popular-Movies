package com.example.rishabhgoyal.watchit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    public static class MoviePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference sortBy = findPreference(getString(R.string.settings_sort_by_key));
            bindPreferenceSummaryToValue(sortBy);
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object Value) {
            String value=Value.toString();
            if(preference instanceof ListPreference)
            {
                ListPreference list=(ListPreference)preference;
                int prefIndex=list.findIndexOfValue(value);
                if(prefIndex>0)
                {
                    CharSequence[] label=list.getEntries();
                    preference.setSummary(value);
                }
            }
            else
            {
                preference.setSummary(value);
            }
            return true;

        }
        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
           String prefString=preferences.getString(preference.getKey()," ");
            onPreferenceChange(preference,prefString);
        }
    }
}
