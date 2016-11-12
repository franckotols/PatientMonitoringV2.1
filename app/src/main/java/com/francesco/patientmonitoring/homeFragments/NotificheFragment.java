package com.francesco.patientmonitoring.homeFragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.adapters.AlertAdapter;
import com.francesco.patientmonitoring.pojo.Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        View rootview = inflater.inflate(R.layout.fragment_notifiche, container, false);



        //get server url
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = pref.getString("service_provider", "");
        final String final_addr = url + "/notifications";

        //populating listView
        listView=(ListView)rootview.findViewById(R.id.listv_alerts);
        alertAdapter = new AlertAdapter(getContext(),R.layout.list_layout_alert);
        listView.setAdapter(alertAdapter);

        /**
         * parte REST per riempire la sezione dei checkBox relativi alle varie malattie (GET)
         * In questo caso la richiesta deve essere sincrona
         */

        JsonObjectRequest jobjRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                        Log.e("VOLLEY", "ERROR");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jobjRequest);

        return rootview;

    }



}


