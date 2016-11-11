package com.francesco.patientmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UrinAnalysisActivity extends BaseActivity implements View.OnClickListener {

    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    private String pat_id;
    private Button searchUrinAnalysis;

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urin_analysis);

        searchUrinAnalysis = (Button)findViewById(R.id.search_urin_analysis);
        searchUrinAnalysis.setOnClickListener(this);

        Intent i = getIntent();
        pat_id = i.getStringExtra("id");
        final String nome = i.getStringExtra("nome");
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
        /*
         * POST REQUEST TO THE WEB SERVICE
         */
        //postPatientID();
    }

    private void postPatientID() {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/urinanalysis";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(UrinAnalysisActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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
                            /*
                            if (err_msg.equals("wrong_params")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(LoginActivity.this);
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.toast_user_password_wrong))
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

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(LoginActivity.this);
                                noServerAlert.setTitle("Attenzione!")
                                        .setMessage(getString(R.string.toast_server_wrong))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noServerAlert.show();

                            }
                            */


                            error.printStackTrace();

                        }

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

    @Override
    public void onClick(View view) {

        if  (view == searchUrinAnalysis){
            postPatientID();
        }
    }
}
