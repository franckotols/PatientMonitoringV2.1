package com.francesco.patientmonitoring.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "New_User";
    SharedPreferences pref;
    private Spinner spinSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String server_addr = pref.getString("service_provider", "");
        final String final_addr = server_addr+"/registration/specializzazioni";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject obj = response.getJSONObject("specializzazioni");
                            JSONArray jArray = response.getJSONArray("specializzazioni");
                            ArrayList<String> specializzazioni = new ArrayList<>();
                            if (jArray != null) {
                                for (int i=0;i<jArray.length();i++){
                                    specializzazioni.add(jArray.get(i).toString());
                                }
                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, specializzazioni);
                            spinSpec = (Spinner)findViewById(R.id.user_spec);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinSpec.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", "ERROR");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }

    public void riepilogo(View view) throws JSONException {
        Intent nuovo_intento = new Intent(this, VisualizzaRiepilogo.class);

        EditText firstnameText = (EditText) findViewById(R.id.user_first_name);
        String firstnameString = firstnameText.getText().toString();
        EditText lastnameText = (EditText) findViewById(R.id.user_last_name);
        String lastnameString = lastnameText.getText().toString();
        EditText email_Text = (EditText) findViewById(R.id.user_email);
        String email_String = email_Text.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.user_password);
        String passwordString = passwordText.getText().toString();
        EditText checkPasswordText = (EditText) findViewById(R.id.check_password);
        String checkPasswordString = checkPasswordText.getText().toString();
        Spinner sexSpinner = (Spinner)findViewById(R.id.user_sex);
        String sexString = sexSpinner.getSelectedItem().toString();

        //spinner per la specializzazione
        String specString = spinSpec.getSelectedItem().toString();

        DatePicker birthdateDatePicker = (DatePicker)findViewById(R.id.user_birthdate);
        int day = birthdateDatePicker.getDayOfMonth();
        int month = birthdateDatePicker.getMonth() + 1;
        int year = birthdateDatePicker.getYear();

        EditText phone_Text = (EditText) findViewById(R.id.user_phone);
        String phone_String = phone_Text.getText().toString();


        JSONObject user_dataJSON = new JSONObject();

        user_dataJSON.put("first_name",firstnameString);
        user_dataJSON.put("last_name",lastnameString);
        user_dataJSON.put("email",email_String);
        user_dataJSON.put("password",passwordString);
        user_dataJSON.put("checkpassword",checkPasswordString);
        user_dataJSON.put("sex",sexString);
        user_dataJSON.put("specializzazione", specString);

        user_dataJSON.put("day",day);
        user_dataJSON.put("month",month);
        user_dataJSON.put("year",year);
        user_dataJSON.put("phone",phone_String);

        String user_dataSTRING = user_dataJSON.toString();
        nuovo_intento.putExtra(EXTRA_MESSAGE, user_dataSTRING);
        startActivity(nuovo_intento);
    }



}

