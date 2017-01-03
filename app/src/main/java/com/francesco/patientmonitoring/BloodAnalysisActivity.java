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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.adapters.BloodMeasureAdapter;
import com.francesco.patientmonitoring.adapters.UrinMeasureAdapter;
import com.francesco.patientmonitoring.pojo.BloodMeasure;
import com.francesco.patientmonitoring.pojo.UrinMeasure;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class BloodAnalysisActivity extends BaseActivity {

    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    private String pat_id;
    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    /**
     * Per rilevare la risposta del server e creare gli elementi per la ListView
     */
    private JSONObject jsonServerResp;
    BloodMeasureAdapter mBloodMeasureAdapter;
    ListView listView;

    /**
     *
     * PER RIEMPIRE L'INTERFACCIA CON I VALORI DELLE ANALISI
     */
    TextView tvDate;
    TextView tvKeys;
    TextView tvValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_analysis);

        //preparazione della listView con l'adapter delle analisi del sangue
        listView=(ListView)findViewById(R.id.list_blood);
        mBloodMeasureAdapter = new BloodMeasureAdapter(this,R.layout.list_layout_blood_measure);
        listView.setAdapter(mBloodMeasureAdapter);

        //Riempimento dei campi relativi al paziente selezionato
        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();
        setTitle(nome+" - "+getString(R.string.esami_sangue));
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        //REST request
        postPatientID();

        //TextView relativa alla singola analisi
        tvDate=(TextView)findViewById(R.id.date_blood_meas);
        tvKeys=(TextView)findViewById(R.id.string_keys);
        tvValues=(TextView)findViewById(R.id.string_values);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BloodMeasure bloodMeasure = (BloodMeasure) adapterView.getItemAtPosition(i);
                String date = bloodMeasure.getDate();
                String keys = bloodMeasure.getKeys();
                String values = bloodMeasure.getValues();
                tvDate.setText(date);
                tvKeys.setText(keys);
                tvValues.setText(values);

            }
        });




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
            Intent settingsIntent = new Intent(BloodAnalysisActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(BloodAnalysisActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(BloodAnalysisActivity.this, HomeActivity.class);
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
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(BloodAnalysisActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(BloodAnalysisActivity.this, LoginActivity.class);
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

        final ProgressDialog pd = new ProgressDialog(BloodAnalysisActivity.this);

        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/bloodanalysis";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();


                        //Toast.makeText(UrinAnalysisActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            //trasforma la stringa in oggetto json
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("results");
                            String date_meas;


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                date_meas = jsonObj.getString("eventDate");
                                String keys_string= "";
                                String values_string = "";
                                //measurements
                                JSONArray meas_array = jsonObj.getJSONArray("measurements");
                                for (int j=0; j<meas_array.length();j++){
                                    JSONObject key_value = meas_array.getJSONObject(j);
                                    String key = key_value.getString("key");
                                    String value = key_value.getString("value");
                                    keys_string=keys_string+"\n"+key;
                                    values_string=values_string+"\n"+value;
                                }

                                BloodMeasure bloodMeasure = new BloodMeasure(date_meas, keys_string,values_string);
                                mBloodMeasureAdapter.add(bloodMeasure);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mBloodMeasureAdapter.notifyDataSetChanged();

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

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(BloodAnalysisActivity.this);
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

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(BloodAnalysisActivity.this);
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
