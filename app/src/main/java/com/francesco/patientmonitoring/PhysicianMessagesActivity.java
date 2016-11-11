package com.francesco.patientmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
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
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;

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

        physician_id = PreferenceManager.getDefaultSharedPreferences(this).getString("physician_id", "defaultStringIfNothingFound");

        /*
        From the intent
         */

        Intent i = getIntent();
        pat_id = i.getStringExtra("id");
        //physician_id = i.getStringExtra("physician_id");
        final String nome = i.getStringExtra("nome");
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");

        /*
        set textviews with intent values
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(PhysicianMessagesActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home) {
            Intent settingsIntent = new Intent(PhysicianMessagesActivity.this, HomeActivity.class);
            startActivity(settingsIntent);
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
            noMessageAlert.setTitle("Attenzione!")
                    .setMessage("Nessun messaggio inserito")
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
            yesMessageAlert.setTitle("Invio messaggio...")
                    .setMessage("Continuare?")
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

                        Toast.makeText(PhysicianMessagesActivity.this, "Messaggio Inviato", Toast.LENGTH_SHORT).show();

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
                                badRequestAlert.setTitle("Attenzione!")
                                        .setMessage("Problema di connessione!\nImpossibile inviare il messaggio!")
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
                            badRequestAlert.setTitle("Attenzione!")
                                    .setMessage("Problema di connessione!\nImpossibile inviare il messaggio!")
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






