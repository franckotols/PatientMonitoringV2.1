package com.francesco.patientmonitoring.homeFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.adapters.AlertAdapter;
import com.francesco.patientmonitoring.pojo.Alerts;
import com.francesco.patientmonitoring.pojo.SpinnerParams;
import com.francesco.patientmonitoring.utilities.PhysicianInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificheFragment extends Fragment {

    /**
     * Volley Needs
     **/
    SharedPreferences pref;

    /**
     * Per rilevare la risposta del server e creare gli elementi per la ListView
     */
    private JSONObject jsonServerResp;
    AlertAdapter alertAdapter;
    ListView listView;

    /**
     * Layout
     */
    Spinner typeAlertsSelector;
    SpinnerParams typeAlerts;
    ArrayList<SpinnerParams> typeAlertsArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        View rootview = inflater.inflate(R.layout.fragment_notifiche, container, false);
        //populating listView
        listView=(ListView)rootview.findViewById(R.id.listv_alerts);

        /*
         * SPINNER POPULATING
         */
        typeAlertsArray = setSpinnerAlerts();
        typeAlertsSelector = (Spinner)rootview.findViewById(R.id.spinner_alerts);
        ArrayAdapter<SpinnerParams> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,typeAlertsArray);
        typeAlertsSelector.setAdapter(adapter1);
        typeAlertsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeAlerts = (SpinnerParams)parent.getSelectedItem();
                getAlerts(typeAlerts.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         * REST REQUEST DEFAULT
         * di default mostra tutti gli alerts
         **/
        //getAlerts("get_alerts_read_and_notread");



        return rootview;

    }

    private ArrayList<SpinnerParams> setSpinnerAlerts(){
        ArrayList<SpinnerParams> params = new ArrayList<>();
        String[] ids = getResources().getStringArray(R.array.alerts_search_ids);
        String[] names = getResources().getStringArray(R.array.alerts_search_text);
        for (int i=0;i<ids.length;i++){
            params.add(new SpinnerParams(ids[i],names[i]));
        }
        return params;
    }


    private void getAlerts(String alert_selector_id){

        //initialize adapter in every request
        alertAdapter = new AlertAdapter(getContext(),R.layout.list_layout_alert);
        listView.setAdapter(alertAdapter);
        //get server url
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = pref.getString("service_provider", "");
        final String final_addr = url + "/alarms?physician_ID="+ PhysicianInfo.getPhysician_id()+"&api="+alert_selector_id;
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.process_dialog_waiting));

        JsonObjectRequest jobjRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();

                        try {

                            JSONArray jsonArray = response.getJSONArray("results");
                            String patient_name,date,type,message,status,id;

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                patient_name = jsonObj.getString("assetName");
                                date = jsonObj.getString("eventDate");
                                type = jsonObj.getString("type");
                                message = jsonObj.getString("message");
                                status = jsonObj.getString("status");
                                id = jsonObj.getString("id");

                                Alerts alert = new Alerts(patient_name,date,type,message,status,id);
                                alertAdapter.add(alert);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        alertAdapter.notifyDataSetChanged();
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
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }
                            if (err_msg.equals("no_values_not_read")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getActivity());
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_unread_alerts_dialog))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();

                            }
                            if (err_msg.equals("no_values")) {

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                noServerAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_alerts_dialog))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noServerAlert.show();
                            }

                            if (err_msg.equals("error")) {

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
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

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jobjRequest);

    }



}


