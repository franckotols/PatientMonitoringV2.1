package com.francesco.patientmonitoring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

public class DiarioMedico extends AppCompatActivity {

    SharedPreferences pref;
    public static final String KEY_DATE = "date";
    public static final String KEY_PAT_ID = "pat_id";

    DatePicker selectedDate;
    Button searchDateBtn;
    TextView resultsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario_medico);

        selectedDate = (DatePicker)findViewById(R.id.diary_day);
        searchDateBtn = (Button)findViewById(R.id.search_date);
        resultsTV = (TextView)findViewById(R.id.tv_result_diary);



        Intent i = getIntent();
        //final String nome = i.getStringExtra("nome");
        //final String city = i.getStringExtra("città");
        //final String birthdate = i.getStringExtra("data_di_nascita");
        final String pat_id = i.getStringExtra("id");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String url = pref.getString("service_provider", "");

        searchDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = selectedDate.getDayOfMonth();
                int month = selectedDate.getMonth() + 1;
                int year = selectedDate.getYear();
                final String date = String.valueOf(day)+"-"+String.valueOf(month)+"-"+String.valueOf(year);

                Toast.makeText(DiarioMedico.this, date, Toast.LENGTH_LONG).show();

                final String final_addr = url+"/diarioclinico";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                resultsTV.setText(response);


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
                                    if (err_msg.equals("wrong_params")) {
                                        Toast.makeText(DiarioMedico.this, getString(R.string.toast_user_password_wrong), Toast.LENGTH_LONG).show();
                                    }
                                    if (err_msg.equals("no_server")) {
                                        Toast.makeText(DiarioMedico.this, getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                                    }

                                }
                                else{
                                    Toast.makeText(DiarioMedico.this, getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                                }

                                error.printStackTrace();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_DATE, date);
                        params.put(KEY_PAT_ID, pat_id);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(DiarioMedico.this);
                requestQueue.add(stringRequest);

            }
        });
    }
}
