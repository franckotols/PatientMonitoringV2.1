package com.francesco.patientmonitoring.sharedPreferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.francesco.patientmonitoring.R;


public class Settings extends PreferenceFragmentCompat {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        setPreferencesFromResource(R.xml.settings_menu, rootkey);

    }


}


