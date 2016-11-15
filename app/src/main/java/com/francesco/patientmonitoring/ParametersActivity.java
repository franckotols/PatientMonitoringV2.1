package com.francesco.patientmonitoring;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.francesco.patientmonitoring.fragmentParametri.ParametriGraficiFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriMediFragment;
import com.francesco.patientmonitoring.utilities.PatientInfo;

public class ParametersActivity extends BaseActivity {

    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameteres);

        final String nome = PatientInfo.getPatient_name();
        setTitle(nome+" - Parametri");

        mTabHost = (FragmentTabHost)findViewById(R.id.tabHostParams);
        mTabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Valori Medi"), ParametriValoriMediFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Grafici"), ParametriGraficiFragment.class, null);


    }


}
