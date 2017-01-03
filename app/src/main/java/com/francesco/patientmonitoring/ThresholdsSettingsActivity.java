package com.francesco.patientmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.pojo.Alerts;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;
import com.francesco.patientmonitoring.utilities.PhysicianInfo;
import com.francesco.patientmonitoring.utilities.ThresholdsValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThresholdsSettingsActivity extends BaseActivity implements View.OnClickListener{

    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    private String pat_id;


    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;

    //TextViews Thresholds
    TextView tvSysMin;
    TextView tvSysMax;
    TextView tvDiasMin;
    TextView tvDiasMax;
    TextView tvHRMin;
    TextView tvHRMax;
    TextView tvSpo2Min;
    TextView tvSpo2Max;
    TextView tvGlucMin;
    TextView tvGlucMax;

    Button setThreshBtn;
    //EditText Thresholds
    EditText etSysMin;
    EditText etSysMax;
    EditText etDiasMin;
    EditText etDiasMax;
    EditText etHrMin;
    EditText etHrMax;
    EditText etSpo2Min;
    EditText etSpo2Max;
    EditText etGlicMin;
    EditText etGlicMax;
    String et_sys_min;
    String et_sys_max;
    String et_dias_min;
    String et_dias_max;
    String et_hr_min;
    String et_hr_max;
    String et_spo2_min;
    String et_spo2_max;
    String et_glic_min;
    String et_glic_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thresholds_settings);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        setTitle(nome+" - "+getString(R.string.set_thersh_par));

        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        //textvies thresholds
        tvSysMin = (TextView)findViewById(R.id.sys_min);
        tvSysMax = (TextView)findViewById(R.id.sys_max);
        tvDiasMin = (TextView)findViewById(R.id.dias_min);
        tvDiasMax = (TextView)findViewById(R.id.dias_max);
        tvHRMin = (TextView)findViewById(R.id.hr_min);
        tvHRMax = (TextView)findViewById(R.id.hr_max);
        tvSpo2Min = (TextView)findViewById(R.id.spo2_min);
        tvSpo2Max = (TextView)findViewById(R.id.spo2_max);
        tvGlucMin = (TextView)findViewById(R.id.glic_min);
        tvGlucMax = (TextView)findViewById(R.id.glic_max);

        //sending GET Request

        getThreshForPatient(pat_id);
        //editTexts
        etSysMin = (EditText)findViewById(R.id.new_sys_min);
        etSysMax = (EditText)findViewById(R.id.new_sys_max);
        etDiasMin = (EditText)findViewById(R.id.new_dias_min);
        etDiasMax = (EditText)findViewById(R.id.new_dias_max);
        etHrMin = (EditText)findViewById(R.id.new_hr_min);
        etHrMax = (EditText)findViewById(R.id.new_hr_max);
        etSpo2Min = (EditText)findViewById(R.id.new_spo2_min);
        etSpo2Max = (EditText)findViewById(R.id.new_spo2_max);
        etGlicMin = (EditText)findViewById(R.id.new_glic_min);
        etGlicMax = (EditText)findViewById(R.id.new_glic_max);

        setThreshBtn = (Button)findViewById(R.id.btn_thresh_new);
        setThreshBtn.setOnClickListener(this);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(ThresholdsSettingsActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(ThresholdsSettingsActivity.this, HomeActivity.class);
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
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(ThresholdsSettingsActivity.this, LoginActivity.class);
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

    private void setTextViews(){

        tvSysMin.setText(String.valueOf(ThresholdsValues.getSysMin()));
        tvSysMax.setText(String.valueOf(ThresholdsValues.getSysMax()));
        tvDiasMin.setText(String.valueOf(ThresholdsValues.getDiasMin()));
        tvDiasMax.setText(String.valueOf(ThresholdsValues.getDiasMax()));
        tvHRMin.setText(String.valueOf(ThresholdsValues.getHrMin()));
        tvHRMax.setText(String.valueOf(ThresholdsValues.getHrMax()));
        tvSpo2Min.setText(String.valueOf(ThresholdsValues.getSpo2Min()));
        tvSpo2Max.setText(String.valueOf(ThresholdsValues.getSpo2Max()));
        tvGlucMin.setText(String.valueOf(ThresholdsValues.getGlicMin()));
        tvGlucMax.setText(String.valueOf(ThresholdsValues.getGlicMax()));

    }

    private void getThreshForPatient(String patient_id){

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url + "/thresholds?pat_id="+patient_id+"&physician_id="+PhysicianInfo.getPhysician_id();

        JsonObjectRequest jobjRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        double sys_min,sys_max,dias_min,dias_max,hr_min,hr_max,spo2_min,spo2_max,glic_min,glic_max;

                        try {
                            JSONObject resp = response.getJSONObject("results");
                            JSONObject sys = resp.getJSONObject("systolic");
                            sys_min = sys.getDouble("min");
                            sys_max = sys.getDouble("max");
                            ThresholdsValues.setSysMin(sys_min);
                            ThresholdsValues.setSysMax(sys_max);
                            JSONObject dias = resp.getJSONObject("diastolic");
                            dias_min = dias.getDouble("min");
                            dias_max = dias.getDouble("max");
                            ThresholdsValues.setDiasMin(dias_min);
                            ThresholdsValues.setDiasMax(dias_max);
                            JSONObject hr = resp.getJSONObject("heart_rate");
                            hr_min = hr.getDouble("min");
                            hr_max = hr.getDouble("max");
                            ThresholdsValues.setHrMin(hr_min);
                            ThresholdsValues.setHrMax(hr_max);
                            JSONObject spo2 = resp.getJSONObject("spo2");
                            spo2_min = spo2.getDouble("min");
                            spo2_max = spo2.getDouble("max");
                            ThresholdsValues.setSpo2Min(spo2_min);
                            ThresholdsValues.setSpo2Max(spo2_max);
                            JSONObject glic = resp.getJSONObject("glucose_value");
                            glic_min = glic.getDouble("min");
                            glic_max = glic.getDouble("max");
                            ThresholdsValues.setGlicMin(glic_min);
                            ThresholdsValues.setGlicMax(glic_max);
                            setTextViews();

                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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

                            if (err_msg.equals("error")) {

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
                                noServerAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_server_connection))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noServerAlert.show();
                            }

                        }
                        else
                        {

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
                            noServerAlert.setTitle(getString(R.string.attention_message))
                                    .setMessage(getString(R.string.no_server_connection))
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
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ThresholdsSettingsActivity.this);
        requestQueue.add(jobjRequest);

    }

    private void postNewThresholds(){

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        String test = "";
        final String final_addr = url+"/thresholds";
        if (!etSysMin.getText().toString().equals("")){et_sys_min = etSysMin.getText().toString();} else{et_sys_min=String.valueOf(ThresholdsValues.getSysMin());}
        if (!etSysMax.getText().toString().equals("")){et_sys_max = etSysMax.getText().toString();} else{et_sys_max=(String.valueOf(ThresholdsValues.getSysMax()));}
        if (!etDiasMin.getText().toString().equals("")){et_dias_min = etDiasMin.getText().toString();} else{et_dias_min=(String.valueOf(ThresholdsValues.getDiasMin()));}
        if (!etDiasMax.getText().toString().equals("")){et_dias_max = etDiasMax.getText().toString();} else{et_dias_max=(String.valueOf(ThresholdsValues.getDiasMax()));}
        if (!etHrMin.getText().toString().equals("")){et_hr_min = etHrMin.getText().toString();} else{et_hr_min=(String.valueOf(ThresholdsValues.getHrMin()));}
        if (!etHrMax.getText().toString().equals("")){et_hr_max = etHrMax.getText().toString();} else{et_hr_max=(String.valueOf(ThresholdsValues.getHrMax()));}
        if (!etSpo2Min.getText().toString().equals("")){et_spo2_min = etSpo2Min.getText().toString();} else{et_spo2_min=(String.valueOf(ThresholdsValues.getSpo2Min()));}
        if (!etSpo2Max.getText().toString().equals("")){et_spo2_max = etSpo2Max.getText().toString();} else{et_spo2_max=(String.valueOf(ThresholdsValues.getSpo2Max()));}
        if (!etGlicMin.getText().toString().equals("")){et_glic_min = etGlicMin.getText().toString();} else{et_glic_min=(String.valueOf(ThresholdsValues.getGlicMin()));}
        if (!etGlicMax.getText().toString().equals("")){et_glic_max = etGlicMax.getText().toString();} else{et_glic_max=(String.valueOf(ThresholdsValues.getGlicMax()));}

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ThresholdsSettingsActivity.this,getString(R.string.thresh_succ_string),Toast.LENGTH_LONG).show();
                        getThreshForPatient(pat_id);
                        setTextViews();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        NetworkResponse err_ = error.networkResponse;
                        //String display_err_user_msg="\n\n\nError in sending request.";
                        if (err_ != null && err_.data != null) {
                            int err_status_code = err_.statusCode;
                            //String err_status_code_str = ("" + err_status_code);
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }

                            if (err_msg.equals("error")) {
                                AlertDialog.Builder badRequestAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
                                badRequestAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.thresh_prob_string))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                badRequestAlert.show();
                            }
                        }

                        else {

                            AlertDialog.Builder badRequestAlert = new AlertDialog.Builder(ThresholdsSettingsActivity.this);
                            badRequestAlert.setTitle(getString(R.string.attention_message))
                                    .setMessage(getString(R.string.thresh_prob_string))
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create();
                            badRequestAlert.show();

                        }



                        error.printStackTrace();

                    }


                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PAT_ID, pat_id);
                params.put("physician_id",PhysicianInfo.getPhysician_id());
                params.put("new_sys_min",et_sys_min);
                params.put("new_sys_max",et_sys_max);
                params.put("new_dias_min",et_dias_min);
                params.put("new_dias_max",et_dias_max);
                params.put("new_hr_min",et_hr_min);
                params.put("new_hr_max",et_hr_max);
                params.put("new_spo2_min",et_spo2_min);
                params.put("new_spo2_max",et_spo2_max);
                params.put("new_glic_min",et_glic_min);
                params.put("new_glic_max",et_glic_max);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {

        if(view == setThreshBtn){
            postNewThresholds();
        }

    }
}
