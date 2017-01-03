package com.francesco.patientmonitoring;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.adapters.DiaryDPMeasureAdapter;
import com.francesco.patientmonitoring.adapters.DiaryEmoMeasureAdapter;
import com.francesco.patientmonitoring.pojo.DiaryDpMeasure;
import com.francesco.patientmonitoring.pojo.DiaryEmoMeasure;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DiaryEmoActivity extends BaseActivity {

    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    private JSONObject jsonServerResp;

    String pat_id;
    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    TabHost mTabHost;
    TabHost.TabSpec mSpec;

    ListView listView;
    DiaryEmoMeasureAdapter mDiaryEmoMeasureAdapter;

    //TextViews
    TextView tvDate;
    TextView tvEmoFeverTemp;
    TextView tvEmoOtherAnnotation;
    TextView tvEmoIdealWeight;
    TextView tvEmoDeltaWeight;
    TextView tvEmoProgrammingWeight;
    TextView tvEmoDialysisHour;
    TextView tvEmoFilter;
    TextView tvEmoDuration;
    TextView tvEmoInitialWeight;
    TextView tvEmoFinalWeight;
    TextView tvEmoDialysisTherapyProgress;
    TextView tvEmoDialysisTherapyEnd;
    TextView tvEmoStartHour;
    TextView tvEmoPreClosingHour;
    TextView tvEmoEndHour;
    TextView tvEmoHourCramp;
    TextView tvEmoPressureStartMin;
    TextView tvEmoPressureStartMax;
    TextView tvEmoPressurePreMin;
    TextView tvEmoPressurePreMax;
    TextView tvEmoPressureEndMin;
    TextView tvEmoPressureEndMax;
    TextView tvEmoHRStartHour;
    TextView tvEmoHRPreHour;
    TextView tvEmoHREndHour;
    TextView tvEmoBloodStrStartHour;
    TextView tvEmoBloodStrEndHour;
    TextView tvEmoVenPressStartHour;
    TextView tvEmoVenPressEndHour;
    TextView tvEmoCramp;
    TextView tvEmoFever;
    TextView tvEmoListDisMonitor;
    TextView tvEmoListDisOsm;
    TextView tvEmoListOthAct;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_emo);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        setTitle(nome+" - "+getString(R.string.diario_clinico));
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
        setTabHost();

        listView=(ListView)findViewById(R.id.list_diaryEMO);
        mDiaryEmoMeasureAdapter = new DiaryEmoMeasureAdapter(this,R.layout.list_layout_diaryemo_measure);
        listView.setAdapter(mDiaryEmoMeasureAdapter);

        /*
        Riempimento delle TextViews relative ai parametri
         */
        tvDate = (TextView)findViewById(R.id.tv_date_diary);
        tvEmoFeverTemp = (TextView)findViewById(R.id.tv_temp_fever);
        tvEmoOtherAnnotation = (TextView)findViewById(R.id.tv_other_annotations);
        tvEmoIdealWeight = (TextView)findViewById(R.id.tv_ideal_weight);
        tvEmoDeltaWeight = (TextView)findViewById(R.id.tv_delta_weight);
        tvEmoProgrammingWeight = (TextView)findViewById(R.id.tv_prog_weight);
        tvEmoDialysisHour = (TextView)findViewById(R.id.tv_dialysis_hours);
        tvEmoFilter = (TextView)findViewById(R.id.tv_filter);
        tvEmoDuration = (TextView)findViewById(R.id.tv_treat_duration);
        tvEmoInitialWeight = (TextView)findViewById(R.id.tv_start_weight);
        tvEmoFinalWeight = (TextView)findViewById(R.id.end_weight);
        tvEmoDialysisTherapyProgress = (TextView)findViewById(R.id.tv_therapy_prog);
        tvEmoDialysisTherapyEnd = (TextView)findViewById(R.id.tv_therapy_end);
        tvEmoStartHour = (TextView)findViewById(R.id.tv_start_hour);
        tvEmoPreClosingHour = (TextView)findViewById(R.id.tv_pre_hour);
        tvEmoEndHour = (TextView)findViewById(R.id.tv_stop_hour);
        tvEmoHourCramp = (TextView)findViewById(R.id.tv_hour_cramps);
        tvEmoPressureStartMin = (TextView)findViewById(R.id.tv_pres_start_min);
        tvEmoPressureStartMax = (TextView)findViewById(R.id.tv_pres_start_max);
        tvEmoPressurePreMin = (TextView)findViewById(R.id.tv_pres_pre_min);
        tvEmoPressurePreMax = (TextView)findViewById(R.id.tv_pres_pre_max);
        tvEmoPressureEndMin = (TextView)findViewById(R.id.tv_pres_end_min);
        tvEmoPressureEndMax = (TextView)findViewById(R.id.tv_pres_end_max);
        tvEmoHRStartHour = (TextView)findViewById(R.id.tv_hr_start);
        tvEmoHRPreHour = (TextView)findViewById(R.id.tv_hr_pre);
        tvEmoHREndHour = (TextView)findViewById(R.id.tv_hr_end);
        tvEmoBloodStrStartHour = (TextView)findViewById(R.id.tv_blood_stream_start);
        tvEmoBloodStrEndHour = (TextView)findViewById(R.id.tv_blood_stream_end);
        tvEmoVenPressStartHour = (TextView)findViewById(R.id.tv_pres_ven_start);
        tvEmoVenPressEndHour = (TextView)findViewById(R.id.tv_pres_ven_end);
        tvEmoCramp = (TextView)findViewById(R.id.tv_bool_cramps);
        tvEmoFever = (TextView)findViewById(R.id.tv_bool_fever);
        tvEmoListDisMonitor = (TextView)findViewById(R.id.tv_dis_monitor);
        tvEmoListDisOsm = (TextView)findViewById(R.id.tv_dis_osm);
        tvEmoListOthAct = (TextView)findViewById(R.id.tv_other_actions);

        /*
        POST REQUEST
         */
        postPatientID();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaryEmoMeasure measure = (DiaryEmoMeasure) adapterView.getItemAtPosition(i);
                String date = measure.getDate();
                tvDate.setText(date);
                String emoFeverTemp = measure.getEmoFeverTemp();
                tvEmoFeverTemp.setText(emoFeverTemp);
                String emoOtherAnnotation = measure.getEmoOtherAnnotation();
                tvEmoOtherAnnotation.setText(emoOtherAnnotation);
                String emoIdealWeight = measure.getEmoIdealWeight();
                tvEmoIdealWeight.setText(emoIdealWeight);
                String emoDeltaWeight = measure.getEmoDeltaWeight();
                tvEmoDeltaWeight.setText(emoDeltaWeight);
                String emoProgrammingWeight = measure.getEmoProgrammingWeight();
                tvEmoProgrammingWeight.setText(emoProgrammingWeight);
                String emoDialysisHour = measure.getEmoDialysisHour();
                tvEmoDialysisHour.setText(emoDialysisHour);
                String emoFilter = measure.getEmoFilter();
                tvEmoFilter.setText(emoFilter);
                String emoDuration = measure.getEmoDuration();
                tvEmoDuration.setText(emoDuration);
                String emoInitialWeight = measure.getEmoInitialWeight();
                tvEmoInitialWeight.setText(emoInitialWeight);
                String emoFinalWeight = measure.getEmoFinalWeight();
                tvEmoFinalWeight.setText(emoFinalWeight);
                String emoDialysisTherapyProgress = measure.getEmoDialysisTherapyProgress();
                tvEmoDialysisTherapyProgress.setText(emoDialysisTherapyProgress);
                String emoDialysisTherapyEnd = measure.getEmoDialysisTherapyEnd();
                tvEmoDialysisTherapyEnd.setText(emoDialysisTherapyEnd);
                String emoStartHour = measure.getEmoStartHour();
                tvEmoStartHour.setText(emoStartHour);
                String emoPreClosingHour = measure.getEmoPreClosingHour();
                tvEmoPreClosingHour.setText(emoPreClosingHour);
                String emoHourCramp = measure.getEmoHourCramp();
                tvEmoHourCramp.setText(emoHourCramp);
                String emoEndHour = measure.getEmoEndHour();
                tvEmoEndHour.setText(emoEndHour);
                String emoPressureStartMin = measure.getEmoPressureStartMin();
                tvEmoPressureStartMin.setText(emoPressureStartMin);
                String emoPressureStartMax = measure.getEmoPressureStartMax();
                tvEmoPressureStartMax.setText(emoPressureStartMax);
                String emoPressurePreMin = measure.getEmoPressurePreMin();
                tvEmoPressurePreMin.setText(emoPressurePreMin);
                String emoPressurePreMax = measure.getEmoPressurePreMax();
                tvEmoPressurePreMin.setText(emoPressurePreMax);
                String emoPressureEndMin = measure.getEmoPressureEndMin();
                tvEmoPressureEndMin.setText(emoPressureEndMin);
                String emoPressureEndMax = measure.getEmoPressureEndMax();
                tvEmoPressureEndMax.setText(emoPressureEndMax);
                String emoHRStartHour = measure.getEmoHRStartHour();
                tvEmoHRStartHour.setText(emoHRStartHour);
                String emoHRPreHour = measure.getEmoHRPreHour();
                tvEmoHRPreHour.setText(emoHRPreHour);
                String emoHREndHour = measure.getEmoHREndHour();
                tvEmoHREndHour.setText(emoHREndHour);
                String emoBloodStrStartHour = measure.getEmoBloodStrStartHour();
                tvEmoBloodStrStartHour.setText(emoBloodStrStartHour);
                String emoBloodStrEndHour = measure.getEmoBloodStrEndHour();
                tvEmoBloodStrEndHour.setText(emoBloodStrEndHour);
                String emoVenPressStartHour = measure.getEmoVenPressStartHour();
                tvEmoVenPressStartHour.setText(emoVenPressStartHour);
                String emoVenPressEndHour = measure.getEmoVenPressEndHour();
                tvEmoVenPressEndHour.setText(emoVenPressEndHour);
                boolean emoCramp = measure.isEmoCramp();
                if (emoCramp){tvEmoCramp.setText(getString(R.string.yes_string));tvEmoCramp.setTextColor(0xffff0000);}else{tvEmoCramp.setText(getString(R.string.no_string));}
                boolean emoFever = measure.isEmoFever();
                if (emoFever){tvEmoFever.setText(getString(R.string.yes_string));tvEmoFever.setTextColor(0xffff0000);}else{tvEmoFever.setText(getString(R.string.no_string));}
                String emoListDisMonitor = measure.getEmoListDisMonitor();
                tvEmoListDisMonitor.setText(emoListDisMonitor);
                String emoListDisOsm = measure.getEmoListDisOsm();
                tvEmoListDisOsm.setText(emoListDisOsm);
                String emoListOthAct = measure.getEmoListOthAct();
                tvEmoListOthAct.setText(emoListOthAct);
            }
        });


    }

    private void setTabHost() {
        mTabHost = (TabHost) findViewById(R.id.tabHost_emo);
        mTabHost.setup();
        //Lets add the first Tab
        mSpec = mTabHost.newTabSpec("Dialisi");
        mSpec.setContent(R.id.lay_uno);
        mSpec.setIndicator("Dialisi");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Parametri");
        mSpec.setContent(R.id.lay_due);
        mSpec.setIndicator("Parametri");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Annotazioni");
        mSpec.setContent(R.id.lay_tre);
        mSpec.setIndicator("Annotazioni");
        mTabHost.addTab(mSpec);
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
            Intent settingsIntent = new Intent(DiaryEmoActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(DiaryEmoActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(DiaryEmoActivity.this, HomeActivity.class);
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
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(DiaryEmoActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(DiaryEmoActivity.this, LoginActivity.class);
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

    private void postPatientID() {

        final ProgressDialog pd = new ProgressDialog(DiaryEmoActivity.this);

        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/diaryEmo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        //Toast.makeText(DiaryDpActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            //trasforma la stringa in oggetto json
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("results");
                            String date;
                            String emoFeverTemp, emoOtherAnnotation, emoIdealWeight, emoDeltaWeight, emoProgrammingWeight;
                            String emoDialysisHour, emoFilter, emoDuration, emoInitialWeight, emoFinalWeight, emoDialysisTherapyProgress, emoDialysisTherapyEnd;
                            String emoStartHour, emoPreClosingHour, emoHourCramp, emoEndHour;
                            String emoPressureStartMin, emoPressureStartMax, emoPressurePreMin, emoPressurePreMax, emoPressureEndMin, emoPressureEndMax;
                            String emoHRStartHour, emoHRPreHour, emoHREndHour, emoBloodStrStartHour, emoBloodStrEndHour, emoVenPressStartHour, emoVenPressEndHour;
                            boolean emoCramp, emoFever;
                            String emoListDisMonitor, emoListDisOsm, emoListOthAct;


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONObject measurements = jsonObj.getJSONObject("measurements");
                                //dates
                                date = jsonObj.getString("eventDate");
                                emoFeverTemp = measurements.getString("editTextEmoFever");
                                emoOtherAnnotation = measurements.getString("editTextEmoOtherAnnotation");
                                emoIdealWeight = measurements.getString("editTextEmoIdealWeight");
                                emoDeltaWeight = measurements.getString("editTextEmoDeltaWeight");
                                emoProgrammingWeight = measurements.getString("editTextEmoProgrammingWeight");
                                emoDialysisHour = measurements.getString("editTextEmoDialysisHour");
                                emoFilter = measurements.getString("editTextEmoFilter");
                                emoDuration = measurements.getString("editTextEmoDuration");
                                emoInitialWeight = measurements.getString("editTextEmoInitialWeight");
                                emoFinalWeight = measurements.getString("editTextEmoFinalWeight");
                                emoDialysisTherapyProgress = measurements.getString("editTextEmoDialysisTherapyInProgress");
                                emoDialysisTherapyEnd = measurements.getString("editTextEmoDialysisTherapyEnd");
                                emoStartHour = measurements.getString("editTextEmoStartHour");
                                emoPreClosingHour = measurements.getString("editTextEmoPreClosingHour");
                                emoHourCramp = measurements.getString("editTextViewHourCramp");
                                emoEndHour = measurements.getString("editTextEmoEndHour");
                                emoPressureStartMin = measurements.getString("editTextEmoPressureStartMin");
                                emoPressureStartMax = measurements.getString("editTextEmoPressureStartMax");
                                emoPressurePreMin = measurements.getString("editTextEmoPressurePreMin");
                                emoPressurePreMax = measurements.getString("editTextEmoPressurePreMax");
                                emoPressureEndMin = measurements.getString("editTextEmoPressureEndMin");
                                emoPressureEndMax = measurements.getString("editTextEmoPressureEndMax");
                                emoHRStartHour = measurements.getString("editTextEmoFCStartHour");
                                emoHRPreHour = measurements.getString("editTextEmoFCPreHour");
                                emoHREndHour = measurements.getString("editTextEmoFCEndHour");
                                emoBloodStrStartHour = measurements.getString("editTextEmoBloodStreamStartHour");
                                emoBloodStrEndHour = measurements.getString("editTextEmoBloodStreamEndHour");
                                emoVenPressStartHour = measurements.getString("editTextEmoVenousPressureStartHour");
                                emoVenPressEndHour = measurements.getString("editTextEmoVenousPressureEndHour");
                                emoCramp = measurements.getBoolean("checkBoxEmoCramp");
                                emoFever = measurements.getBoolean("checkBoxEmoFever");
                                emoListDisMonitor = measurements.getString("listEmoDisinfectionMonitor");
                                emoListDisOsm = measurements.getString("listEmoDisinfectionOsmosis");
                                emoListOthAct = measurements.getString("listEmoOtherActionDialysis");





                                DiaryEmoMeasure diaryEmoMeasure = new DiaryEmoMeasure(date, emoFeverTemp, emoOtherAnnotation, emoIdealWeight, emoDeltaWeight, emoProgrammingWeight, emoDialysisHour, emoFilter, emoDuration, emoInitialWeight, emoFinalWeight, emoDialysisTherapyProgress, emoDialysisTherapyEnd, emoStartHour, emoPreClosingHour, emoEndHour, emoHourCramp, emoPressureStartMin, emoPressureStartMax, emoPressurePreMin, emoPressurePreMax, emoPressureEndMin, emoPressureEndMax, emoHRStartHour, emoHRPreHour, emoHREndHour, emoBloodStrStartHour, emoBloodStrEndHour, emoVenPressStartHour, emoVenPressEndHour, emoCramp, emoFever, emoListDisMonitor, emoListDisOsm, emoListOthAct);
                                mDiaryEmoMeasureAdapter.add(diaryEmoMeasure);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mDiaryEmoMeasureAdapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        //la riga successiva serve perchè altrimenti ad ogni richiesta accumulerebbe
                        //le patologie selezionate

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();

                        NetworkResponse err_ = error.networkResponse;
                        //String display_err_user_msg="\n\n\nError in sending request.";
                        if (err_ != null && err_.data != null) {
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

                            if (err_msg.equals("no_values")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(DiaryEmoActivity.this);
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_analysis))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();

                            }


                        }

                        else {

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(DiaryEmoActivity.this);
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


                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PAT_ID, pat_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
