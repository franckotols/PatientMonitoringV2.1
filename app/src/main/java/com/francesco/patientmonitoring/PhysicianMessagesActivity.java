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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;
import com.francesco.patientmonitoring.utilities.PhysicianInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhysicianMessagesActivity extends BaseActivity implements View.OnClickListener {

    /*
    VOLLEY
     */
    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    public static final String KEY_PHY_ID = "physician_id";
    public static final String KEY_MESSAGE = "message";

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    TextView tvDest;
    private String pat_id;
    private String physician_id;
    Button sendMessButton;
    EditText etMessage;
    String message;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_messages);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();
        ArrayList<String> disease = PatientInfo.getList();

        physician_id = PhysicianInfo.getPhysician_id();

        setTitle(getString(R.string.send_message_label)+nome);




        /*
        set textviews with patient values
         */
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvDest = (TextView)findViewById(R.id.tv_nomedest);
        tvNome.setText(nome);
        tvDest.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        /*
        Other views
         */
        sendMessButton = (Button) findViewById(R.id.send_message);
        sendMessButton.setOnClickListener(this);

        etMessage = (EditText)findViewById(R.id.message_text);

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
            Intent settingsIntent = new Intent(PhysicianMessagesActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(PhysicianMessagesActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(PhysicianMessagesActivity.this, HomeActivity.class);
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
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(PhysicianMessagesActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(PhysicianMessagesActivity.this, LoginActivity.class);
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


    @Override
    public void onClick(View view) {

        if (view == sendMessButton) {
            sendMessagefromPhysician();
        }

    }

    private void sendMessagefromPhysician() {

        message = String.valueOf(etMessage.getText());
        if (message.equals("")){
            AlertDialog.Builder noMessageAlert = new AlertDialog.Builder(this);
            noMessageAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.no_message_string))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            noMessageAlert.show();
        }
        else {

            AlertDialog.Builder yesMessageAlert = new AlertDialog.Builder(this);
            yesMessageAlert.setTitle(getString(R.string.sending_string))
                    .setMessage(getString(R.string.continue_string))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restRequest();
                            etMessage.setText("");
                            dialogInterface.dismiss();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            yesMessageAlert.show();

        }

    }

    private void restRequest(){

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/messagefromphysician";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(PhysicianMessagesActivity.this, getString(R.string.message_sent), Toast.LENGTH_SHORT).show();

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
                                AlertDialog.Builder badRequestAlert = new AlertDialog.Builder(PhysicianMessagesActivity.this);
                                badRequestAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.sending_problem_string))
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

                            AlertDialog.Builder badRequestAlert = new AlertDialog.Builder(PhysicianMessagesActivity.this);
                            badRequestAlert.setTitle(getString(R.string.attention_message))
                                    .setMessage(getString(R.string.sending_problem_string))
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
                params.put(KEY_PHY_ID, physician_id);
                params.put(KEY_MESSAGE, message);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}






