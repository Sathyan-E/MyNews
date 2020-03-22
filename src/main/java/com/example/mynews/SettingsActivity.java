package com.example.mynews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    //Fragment class
    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //adding preferences from resources
            addPreferencesFromResource(R.xml.settings_main);
            //finding preferences
            Preference category = findPreference(getString(R.string.settings_category_key));
            //binding preference summary
            bindPreferenceSummary(category);
        }
        //setting the new preference after changed by user in setting activity
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);

            if (preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;
                int preIndex = listPreference.findIndexOfValue(stringValue);
                if (preIndex >=0)
                {
                    CharSequence[] label = listPreference.getEntries();
                    preference.setSummary(label[preIndex]);
                }
                else
                {
                    preference.setSummary(stringValue);
                }
            }
            return true;
        }
        //method for binding and adding the changed preference in preference summary
        private void bindPreferenceSummary(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString =preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }
    }
}
