package com.francesco.patientmonitoring.fragmentParametri;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.utilities.LastPointValues;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriValoriPuntualiFragment extends Fragment implements View.OnClickListener {

    String pat_id;

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    SharedPreferences pref;

    Button searchDayButton;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText mDateEdTxt;
    private String mDateStr;

    JSONObject jsonServerResp;
    TextView tvDate;
    TextView tvSystolic;
    TextView tvDiastolic;
    TextView tvHeartRate;
    TextView tvSpo2;
    TextView tvGlicemy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        //default value
        mDateStr = "";

        View rootview = inflater.inflate(R.layout.parametri_valori_puntuali, container, false);
        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();
        tvNome = (TextView)rootview.findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)rootview.findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)rootview.findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        mDateEdTxt = (EditText) rootview.findViewById(R.id.etxt_date);
        mDateEdTxt.setInputType(InputType.TYPE_NULL);
        mDateEdTxt.requestFocus();

        searchDayButton = (Button)rootview.findViewById(R.id.search_day_btn);
        searchDayButton.setOnClickListener(this);

        setDateTimeField();

        tvDate = (TextView)rootview.findViewById(R.id.date_point_params_tv) ;
        tvSystolic = (TextView)rootview.findViewById(R.id.tv_press_sist);
        tvDiastolic = (TextView)rootview.findViewById(R.id.tv_press_diast);
        tvHeartRate = (TextView)rootview.findViewById(R.id.tv_heart_rate);
        tvSpo2 = (TextView)rootview.findViewById(R.id.tv_spo2);;
        tvGlicemy = (TextView)rootview.findViewById(R.id.tv_glic);

        return rootview;
    }

    private void setDateTimeField() {

        mDateEdTxt.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateEdTxt.setText(dateFormatter.format(newDate.getTime()));
                mDateStr = dateFormatter.format(newDate.getTime());
                LastPointValues.setDate(mDateStr);
                tvDate.setText(LastPointValues.getDate());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void refreshValues(){
        //refresh values of Last Points measurements
        LastPointValues.setSyst("");
        LastPointValues.setDiast("");
        LastPointValues.setHr("");
        LastPointValues.setSpo2("");
        LastPointValues.setGlic("");
        tvSystolic.setText(LastPointValues.getSyst());
        tvDiastolic.setText(LastPointValues.getDiast());
        tvHeartRate.setText(LastPointValues.getHr());
        tvSpo2.setText(LastPointValues.getSpo2());
        tvGlicemy.setText(LastPointValues.getGlic());
    }

    private void sendParams(final String id_pat, final String date) {

        final ProgressDialog pd = new ProgressDialog(getContext());

        refreshValues();

        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/parameters/pointvalues";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            String systolic_measurements = "";
                            String diastolic_measurements = "";
                            String hr_measurements = "";
                            String spo2_measurements = "";
                            String glic_measurements = "";
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonSysArraySys = jsonServerResp.getJSONArray("systolic");
                            for (int i=0;i<jsonSysArraySys.length();i++){
                                JSONObject jsonSysObj = jsonSysArraySys.getJSONObject(i);
                                String date = jsonSysObj.getString("measurementDate");
                                float value = (float) jsonSysObj.getDouble("value");
                                String value_str = String.valueOf(value);
                                systolic_measurements = systolic_measurements+(date+"\t\t\t"+value_str+"\n");
                            }
                            JSONArray jsonSysArrayDias = jsonServerResp.getJSONArray("diastolic");
                            for (int i=0;i<jsonSysArrayDias.length();i++){
                                JSONObject jsonSysObj = jsonSysArrayDias.getJSONObject(i);
                                String date = jsonSysObj.getString("measurementDate");
                                float value = (float) jsonSysObj.getDouble("value");
                                String value_str = String.valueOf(value);
                                diastolic_measurements = diastolic_measurements+(date+"\t\t\t"+value_str+"\n");
                            }

                            JSONArray jsonSysArrayHR = jsonServerResp.getJSONArray("heart_rate");
                            for (int i=0;i<jsonSysArrayHR.length();i++){
                                JSONObject jsonSysObj = jsonSysArrayHR.getJSONObject(i);
                                String date = jsonSysObj.getString("measurementDate");
                                float value = (float) jsonSysObj.getDouble("value");
                                String value_str = String.valueOf(value);
                                hr_measurements = hr_measurements+(date+"\t\t\t"+value_str+"\n");
                            }
                            JSONArray jsonSysArraySpO2 = jsonServerResp.getJSONArray("spo2");
                            for (int i=0;i<jsonSysArraySpO2.length();i++){
                                JSONObject jsonSysObj = jsonSysArraySpO2.getJSONObject(i);
                                String date = jsonSysObj.getString("measurementDate");
                                float value = (float) jsonSysObj.getDouble("value");
                                String value_str = String.valueOf(value);
                                spo2_measurements = spo2_measurements+(date+"\t\t\t"+value_str+"\n");
                            }



                            LastPointValues.setSyst(systolic_measurements);
                            LastPointValues.setDiast(diastolic_measurements);
                            LastPointValues.setHr(hr_measurements);
                            LastPointValues.setSpo2(spo2_measurements);
                            tvSystolic.setText(LastPointValues.getSyst());
                            tvDiastolic.setText(LastPointValues.getDiast());
                            tvHeartRate.setText(LastPointValues.getHr());
                            tvSpo2.setText(LastPointValues.getSpo2());


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();

                        NetworkResponse err_ = error.networkResponse;
                        //String display_err_user_msg="\n\n\nError in sending request.";
                        if(err_ != null && err_.data != null) {
                            //int err_status_code = err_.statusCode;
                            //String err_status_code_str = ("" + err_status_code);
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }
                            if (err_msg.equals("no_meas")) {
                                AlertDialog.Builder noMeasAlert = new AlertDialog.Builder(getActivity());
                                noMeasAlert.setTitle("Attenzione!")
                                        .setMessage("Nessun parametro misurato in questa data!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noMeasAlert.show();
                            }
                            if (err_msg.equals("no_device")) {
                                AlertDialog.Builder noDeviceAlert = new AlertDialog.Builder(getActivity());
                                noDeviceAlert.setTitle("Attenzione!")
                                        .setMessage("I dispositivi di monitoraggio dei parametri non sono ancora associati al paziente!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noDeviceAlert.show();
                            }
                            if (err_msg.equals("not_valid_date")) {
                                AlertDialog.Builder noDateAlert = new AlertDialog.Builder(getActivity());
                                noDateAlert.setTitle("Attenzione!")
                                        .setMessage("Inserisci una data valida!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noDateAlert.show();
                            }
                            if (err_msg.equals("no_date")) {
                                AlertDialog.Builder noDateAlert = new AlertDialog.Builder(getActivity());
                                noDateAlert.setTitle("Attenzione!")
                                        .setMessage("Non hai inserito nessuna data!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noDateAlert.show();
                            }


                        }
                        else{
                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                            noServerAlert.setTitle("Attenzione!")
                                    .setMessage("Errore di connessione al server")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create();
                            noServerAlert.show();
                        }

                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pat_id", id_pat);
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View view) {

        if(view == mDateEdTxt) {
            mDatePickerDialog.show();
        }

        if(view == searchDayButton){
            sendParams(pat_id, mDateStr);

        }

    }
}
