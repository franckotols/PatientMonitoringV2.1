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
import com.francesco.patientmonitoring.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriValoriPuntualiFragment extends Fragment {

    //TextView tvNome;
    //TextView tvCity;
    //TextView tvBirthdate;
    SharedPreferences pref;
    DatePicker dateP;
    Button searchDayButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        View rootview = inflater.inflate(R.layout.parametri_valori_puntuali, container, false);
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
        dateP = (DatePicker)rootview.findViewById(R.id.params_day);
        searchDayButton = (Button)rootview.findViewById(R.id.search_date2);
        searchDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = dateP.getDayOfMonth();
                int month = dateP.getMonth() + 1;
                int year = dateP.getYear();
                final String date = String.valueOf(day)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
                sendParams(id_pat,date);

            }
        });

        return rootview;
    }

    private void sendParams(final String id_pat, final String date) {

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/test";
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
                            if (err_msg.equals("wrong_params")) {
                                Boolean cb = pref.getBoolean("show_dialogs", false);
                                if (cb.equals(true)){
                                    AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getActivity());
                                    wrongParamsAlert.setTitle("Attenzione!")
                                            .setMessage("Not succesfull Request")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();
                                    wrongParamsAlert.show();
                                }
                                else{
                                    Toast.makeText(getActivity(), getString(R.string.toast_user_password_wrong), Toast.LENGTH_LONG).show();
                                }
                            }
                            if (err_msg.equals("no_server")) {
                                Boolean cb = pref.getBoolean("show_dialogs", false);
                                if (cb.equals(true)) {
                                    AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                    noServerAlert.setTitle("Attenzione!")
                                            .setMessage("server is down")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();
                                    noServerAlert.show();
                                }
                                else{
                                    Toast.makeText(getActivity(), getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                        else{
                            Boolean cb = pref.getBoolean("show_dialogs", false);
                            if (cb.equals(true)) {
                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
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
                            else{
                                Toast.makeText(getActivity(), getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                            }
                        }

                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pat", id_pat);
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }




}
