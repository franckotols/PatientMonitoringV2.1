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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.SpinnerParams;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriValoriMediFragment extends Fragment {

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    Button searchMeanValuesButton;
    //Spinner meanSelectorSpinner;
    SpinnerParams p;
    ArrayList<SpinnerParams> params;
    SharedPreferences pref;

    //TextViews Parametri
    TextView firstBMI;
    TextView secondBMI;

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
        final String id_pat = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();
        tvNome = (TextView)rootview.findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)rootview.findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)rootview.findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        //textviews parametri
        firstBMI = (TextView)rootview.findViewById(R.id.bmi_mean_ult);
        secondBMI = (TextView)rootview.findViewById(R.id.bmi_mean_pen);

        searchMeanValuesButton = (Button)rootview.findViewById(R.id.search_mean_values);
        searchMeanValuesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendParams(id_pat);

            }
        });
        /*
         * Spinner Configuration

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
        /*
                sendParams(id_pat,mean_interval);
                //Toast.makeText(getActivity(),"ID: "+p.getId()+",  Name : "+p.getName(),Toast.LENGTH_LONG).show();
            }
        });
        */


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

    private void sendParams(final String id_pat) {

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        //URL DEL SERVER DI PROVA
        final String final_addr = url+"/test_parametri/valori_medi";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonServerResp = new JSONObject(response);
                            JSONObject jBMI = jsonServerResp.getJSONObject("BMI");
                            String BMI_first = jBMI.getString("1st_month");
                            //Toast.makeText(getContext(),BMI_first,Toast.LENGTH_SHORT).show();
                            String BMI_second = jBMI.getString(("2nd_month"));
                            //Toast.makeText(getContext(),BMI_second,Toast.LENGTH_SHORT).show();
                            firstBMI.setText(BMI_first);
                            secondBMI.setText(BMI_second);




                        }catch (JSONException e) {
                            e.printStackTrace();
                        }




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

                            if (err_msg.equals("no_device")) {

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                noServerAlert.setTitle("Attenzione!")
                                        .setMessage("I dispositivi di monitoraggio dei parametri non sono ancora associati al paziente!")
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }




}



