package com.francesco.patientmonitoring.homeFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.adapters.PazienteAdapter;
import com.francesco.patientmonitoring.pojo.Pazienti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RicercaPazienteFragment extends Fragment implements View.OnClickListener {

    View rootview;

    /**
     * Volley Needs
     **/
    SharedPreferences pref;
    public static final String KEY_NAME = "patient_name";
    public static final String KEY_DISEASE = "diseases";

    /**
     * Layout
     */
    private Button bSearch;
    private EditText etName;


    /**
     * per inserire i checkBox relativi alle patologie
     **/
    private CheckBox checkBoxDisease;
    private ArrayList<String> diseases = new ArrayList<>();
    private ArrayList<CheckBox> checkboxes = new ArrayList<>();
    private ArrayList<String> checked_disease = new ArrayList<>();

    /**
     * Per rilevare la risposta del server e creare gli elementi per la ListView
     */
    private JSONObject jsonServerResp;
    PazienteAdapter pazienteAdapter;
    ListView listView;
    String physician_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        rootview = inflater.inflate(R.layout.fragment_ricerca_paziente, container, false);

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String server_addr = pref.getString("service_provider", "");
        final String final_addr = server_addr + "/diseases";
        bSearch = (Button) rootview.findViewById(R.id.search_button);
        bSearch.setOnClickListener(this);
        etName = (EditText) rootview.findViewById(R.id.patient_name);

        Intent i = getActivity().getIntent();
        physician_id = i.getStringExtra("physician_id");


        /**
         * parte REST per riempire la sezione dei checkBox relativi alle varie malattie (GET)
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jArray = response.getJSONArray("diseases");
                            diseases = new ArrayList<>();
                            if (jArray != null) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    diseases.add(jArray.get(i).toString());
                                }
                            }
                            ViewGroup checkboxContainer = (ViewGroup) rootview.findViewById(R.id.checkbox_container);
                            for (int i = 0; i < diseases.size(); i++) {
                                checkBoxDisease = new CheckBox(getContext());
                                checkBoxDisease.setText(diseases.get(i));
                                checkboxContainer.addView(checkBoxDisease);
                                checkboxes.add(checkBoxDisease);
                            }
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

        return rootview;


    }

    private void sendParams() {

        //get server url
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = pref.getString("service_provider", "");

        //populating listView
        listView=(ListView)rootview.findViewById(R.id.listv);
        pazienteAdapter = new PazienteAdapter(getContext(),R.layout.list_layout_patient,physician_id);
        listView.setAdapter(pazienteAdapter);

        /**
         * Prima di far vedere la listView la funzione va a vedere quali sono i checkboxes
         * selezionati per mandare le patologie al server come params. In base a questi il
         * server darà una certa risposta piuttosto che un'altra
         */
        final String patname_string = etName.getText().toString();
        for (CheckBox item : checkboxes) {
            if (item.isChecked()) {
                String check_text = item.getText().toString();
                checked_disease.add(check_text);
            }
        }

        final String final_addr = url + "/searchPatient";
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        try {
                            //trasforma la stringa in oggetto json
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("server_response");
                            String name, city, birthdate, pat_id;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONObject properties = jsonObj.getJSONObject("properties");
                                //username = jsonObj.getString("userName");
                                name = jsonObj.getString("name");
                                //properties
                                city = properties.getString("city");
                                birthdate = properties.getString("birthdate");
                                pat_id = jsonObj.getString("id");

                                Pazienti paziente = new Pazienti(name,city,birthdate,pat_id);
                                pazienteAdapter.add(paziente);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pazienteAdapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        //la riga successiva serve perchè altrimenti ad ogni richiesta accumulerebbe
                        //le patologie selezionate
                        checked_disease.clear();

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
                            /**
                             * elaborazione del file html della risposta del server
                             * per estrapolare il return del web service
                             */
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }
                            if (err_msg.equals("no_selected")) {
                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getContext());
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.toast_no_patient))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();
                            }
                            if (err_msg.equals("no_server")) {
                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getContext());
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.toast_no_server))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();}
                        }
                        else{
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        checked_disease.clear();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_NAME, patname_string);
                params.put(KEY_DISEASE, String.valueOf(checked_disease));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.search_button:
                sendParams();
                break;
        }
    }


}





