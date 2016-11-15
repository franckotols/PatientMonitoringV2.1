package com.francesco.patientmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import com.francesco.patientmonitoring.utilities.PatientInfo;

public class DiaryDpActivity extends BaseActivity {

    String pat_id;
    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    TabHost mTabHost;
    TabHost.TabSpec mSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_dp);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        setTitle(nome+" - Diario Clinico");
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
        setTabHost();



    }

    private void setTabHost(){
        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        //Lets add the first Tab
        mSpec = mTabHost.newTabSpec("Generalità");
        mSpec.setContent(R.id.lay_one);
        mSpec.setIndicator("Generalità");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Conc. Sacche");
        mSpec.setContent(R.id.lay_two);
        mSpec.setIndicator("Conc. Sacche");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Dati Dialisi");
        mSpec.setContent(R.id.lay_three);
        mSpec.setIndicator("Dati Dialisi");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Azioni Manuali");
        mSpec.setContent(R.id.lay_four);
        mSpec.setIndicator("Azioni Manuali");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Sintomi");
        mSpec.setContent(R.id.lay_five);
        mSpec.setIndicator("Sintomi");
        mTabHost.addTab(mSpec);
    }
}
