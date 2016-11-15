package com.francesco.patientmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.francesco.patientmonitoring.utilities.PatientInfo;

import java.util.ArrayList;

public class HomePazienteActivity extends BaseActivity {

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_paziente);


        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();
        ArrayList<String> disease = PatientInfo.getList();

        setTitle(nome+" - Home");

        tvNome = (TextView)findViewById(R.id.tv_pat_name_home);
        tvCity = (TextView)findViewById(R.id.tv_pat_city_home);
        tvBirthdate = (TextView)findViewById(R.id.tv_pat_birthdate_home);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
    }
}
