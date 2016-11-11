package com.francesco.patientmonitoring.fragmentParametri;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.SpinnerParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriValoriMediFragment extends Fragment {

    //TextView tvNome;
    //TextView tvCity;
    //TextView tvBirthdate;
    Button searchButton;
    Spinner meanSelectorSpinner;
    SpinnerParams p;
    ArrayList<SpinnerParams> params;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        View rootview = inflater.inflate(R.layout.parametri_valori_medi, container, false);

        /*
         * Riempimento delle textView relative alle info del paziente
         * Mi serve anche l'id del paziente
         */
        Intent i = getActivity().getIntent();
        final String nome = i.getStringExtra("nome");
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");
        final String id_pat = i.getStringExtra("id");
        //tvNome = (TextView)rootview.findViewById(R.id.tv_nomePaziente);
        //tvCity = (TextView)rootview.findViewById(R.id.tv_cittàPaziente);
        //tvBirthdate = (TextView)rootview.findViewById(R.id.tv_birthPaziente);
        //tvNome.setText(nome);
        //tvCity.setText(city);
        //tvBirthdate.setText(birthdate);
        /*
         * Spinner Configuration
         */
        params = setData();
        meanSelectorSpinner  = (Spinner)rootview.findViewById(R.id.mean_selector_spinner);
        ArrayAdapter<SpinnerParams> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,params);
        meanSelectorSpinner.setAdapter(adapter);
        meanSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p = (SpinnerParams)parent.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        searchButton = (Button)rootview.findViewById(R.id.mean_values_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mean_interval = p.getId();
                /*
                 * POST FUNCTION
                 */
                sendParams(id_pat,mean_interval);
                //Toast.makeText(getActivity(),"ID: "+p.getId()+",  Name : "+p.getName(),Toast.LENGTH_LONG).show();
            }
        });


        return rootview;

    }

    private ArrayList<SpinnerParams> setData(){
        ArrayList<SpinnerParams> params = new ArrayList<>();
        String[] ids = getResources().getStringArray(R.array.mean_selector_ids);
        String[] names = getResources().getStringArray(R.array.mean_selector_names);
        for (int i=0;i<ids.length;i++){
            params.add(new SpinnerParams(ids[i],names[i]));
        }
        return params;
    }

    private void sendParams(final String id_pat, final String mean_interval) {

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/parametri/valorimedi";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "params putted", Toast.LENGTH_LONG).show();
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
                            if (err_msg.equals("error")) {
                                Toast.makeText(getActivity(), "bad request", Toast.LENGTH_LONG).show();
                            }


                        }
                        else{
                            Toast.makeText(getActivity(), "bad request", Toast.LENGTH_LONG).show();
                        }

                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pat", id_pat);
                params.put("mean_interval", mean_interval);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}



