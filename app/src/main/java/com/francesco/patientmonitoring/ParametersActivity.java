package com.francesco.patientmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.fragmentParametri.ParametriGraficiFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriMediFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriPuntualiFragment;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;

public class ParametersActivity extends BaseActivity {

    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameteres);

        final String nome = PatientInfo.getPatient_name();
        setTitle(nome+" - "+getString(R.string.parameters));

        mTabHost = (FragmentTabHost)findViewById(R.id.tabHostParams);
        mTabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.label_valori_medi)), ParametriValoriMediFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.label_valori_puntuali)), ParametriValoriPuntualiFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(getString(R.string.label_grafici)), ParametriGraficiFragment.class, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_paziente,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(ParametersActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(ParametersActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(ParametersActivity.this, HomeActivity.class);
                            startActivity(i_home);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            logoutAlert.show();
        }

        if (id == R.id.action_logout){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(ParametersActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(ParametersActivity.this, LoginActivity.class);
                            startActivity(i_logout);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            logoutAlert.show();
        }

        return super.onOptionsItemSelected(item);
    }


}
