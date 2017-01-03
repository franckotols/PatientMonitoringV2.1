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
import android.widget.Button;
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
import com.francesco.patientmonitoring.adapters.PazienteAdapter;
import com.francesco.patientmonitoring.adapters.UrinMeasureAdapter;
import com.francesco.patientmonitoring.pojo.Pazienti;
import com.francesco.patientmonitoring.pojo.UrinMeasure;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UrinAnalysisActivity extends BaseActivity {

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
    UrinMeasureAdapter mUrinMeasureAdapter;
    ListView listView;

    /*
     * TextViews Parametri Del Test delle Urine
     */
    TextView tvDateMeas;
    TextView tvBil;
    TextView tvPro;
    TextView tvUro;
    TextView tvBlo;
    TextView tvLeu;
    TextView tvPh;
    TextView tvKet;
    TextView tvSg;
    TextView tvGlu;
    TextView tvNit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urin_analysis);

        listView=(ListView)findViewById(R.id.list_urin);
        mUrinMeasureAdapter = new UrinMeasureAdapter(this,R.layout.list_layout_urin_measure);
        listView.setAdapter(mUrinMeasureAdapter);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        setTitle(nome+" - "+getString(R.string.urin_analysis));

        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
        /*
         * POST REQUEST TO THE WEB SERVICE
         */
        postPatientID();

        /*
         * Riempimento delle TextView relative ai parametri
         */
        tvDateMeas = (TextView)findViewById(R.id.date_urin_meas);
        tvBil = (TextView)findViewById(R.id.bil_tv);
        tvPro = (TextView)findViewById(R.id.prot_tv);
        tvUro = (TextView)findViewById(R.id.uro_tv);
        tvBlo = (TextView)findViewById(R.id.blood_tv);
        tvLeu = (TextView)findViewById(R.id.leu_tv);
        tvPh = (TextView)findViewById(R.id.ph_tv);
        tvKet = (TextView)findViewById(R.id.ket_tv);
        tvSg = (TextView)findViewById(R.id.spec_grav_tv);
        tvGlu = (TextView)findViewById(R.id.glu_tv);
        tvNit = (TextView)findViewById(R.id.nit_tv);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UrinMeasure measure = (UrinMeasure) adapterView.getItemAtPosition(i);
                String date = measure.getDate();
                String bil = measure.getBil();
                String pro = measure.getPro();
                String uro = measure.getUro();
                String blo = measure.getBlo();
                String leu = measure.getLeu();
                String ph = measure.getPh();
                String ket = measure.getKet();
                String sg = measure.getSg();
                String glu = measure.getGlu();
                String nit = measure.getNit();
                tvDateMeas.setText(date);
                tvBil.setText(bil);
                tvPro.setText(pro);
                tvUro.setText(uro);
                tvBlo.setText(blo);
                tvLeu.setText(leu);
                tvPh.setText(ph);
                tvKet.setText(ket);
                tvSg.setText(sg);
                tvGlu.setText(glu);
                tvNit.setText(nit);
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
            Intent settingsIntent = new Intent(UrinAnalysisActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(UrinAnalysisActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(UrinAnalysisActivity.this, HomeActivity.class);
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
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(UrinAnalysisActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(UrinAnalysisActivity.this, LoginActivity.class);
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

        final ProgressDialog pd = new ProgressDialog(UrinAnalysisActivity.this);

        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/urinanalysis";

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
                            String date_meas, manufacturer;

                            String bil, pro, uro, blo, leu, ph, ket, sg, glu, nit;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONObject measurements = jsonObj.getJSONObject("measurements");
                                //properties
                                date_meas = jsonObj.getString("date");
                                manufacturer = jsonObj.getString("manufacturer");
                                //values
                                bil = measurements.getString("bil");
                                pro = measurements.getString("pro");;
                                uro = measurements.getString("uro");
                                blo = measurements.getString("blo");
                                leu = measurements.getString("leu");
                                ph = measurements.getString("ph");
                                ket = measurements.getString("ket");
                                sg = measurements.getString("sg");
                                glu = measurements.getString("glu");
                                nit = measurements.getString("nit");

                                UrinMeasure urinMeasure = new UrinMeasure(date_meas, manufacturer, bil, pro, blo, uro, leu, ph, sg, ket, glu, nit);
                                mUrinMeasureAdapter.add(urinMeasure);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mUrinMeasureAdapter.notifyDataSetChanged();

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

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(UrinAnalysisActivity.this);
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

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(UrinAnalysisActivity.this);
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
