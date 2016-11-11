package com.francesco.patientmonitoring.sharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.sharedPreferences.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container_settings, new Settings()).commit();
        }
    }
}
