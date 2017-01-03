package com.francesco.patientmonitoring.fragmentParametri;

import android.app.ProgressDialog;
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
import com.francesco.patientmonitoring.utilities.MeanValues;
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
    SharedPreferences pref;

    //TextViews Parametri
    TextView firstSysTV;
    TextView secondSysTV;
    TextView firstDiasTV;
    TextView secondDiasTV;
    TextView firstHRTV;
    TextView secondHRTV;
    TextView firstSpO2TV;
    TextView secondSpO2TV;
    TextView firstGlicTV;
    TextView secondGlicTV;
    TextView firstWeightTV;
    TextView secondWeightTV;
    TextView firstBMITV;
    TextView secondBMITV;
    TextView firstBodyFatTV;
    TextView secondBodyFatTV;
    TextView firstBodyWaterTV;
    TextView secondBodyWaterTV;
    TextView firstMuscleMassTV;
    TextView secondMuscleMassTV;
    TextView firstBoneMassTV;
    TextView secondBoneMassTV;

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
        firstSysTV = (TextView)rootview.findViewById(R.id.sist_mean_ult);
        secondSysTV = (TextView)rootview.findViewById(R.id.sist_mean_pen);
        firstDiasTV = (TextView)rootview.findViewById(R.id.diast_mean_ult);
        secondDiasTV = (TextView)rootview.findViewById(R.id.diast_mean_pen);
        firstHRTV = (TextView)rootview.findViewById(R.id.heart_mean_ult);
        secondHRTV = (TextView)rootview.findViewById(R.id.heart_mean_pen);
        firstSpO2TV = (TextView)rootview.findViewById(R.id.spo2_mean_ult);
        secondSpO2TV = (TextView)rootview.findViewById(R.id.spo2_mean_pen);
        firstGlicTV = (TextView)rootview.findViewById(R.id.glic_mean_ult);
        secondGlicTV = (TextView)rootview.findViewById(R.id.glic_mean_pen);
        firstWeightTV = (TextView)rootview.findViewById(R.id.weight_mean_ult);
        secondWeightTV = (TextView)rootview.findViewById(R.id.weight_mean_pen);
        firstBMITV = (TextView)rootview.findViewById(R.id.bmi_mean_ult);
        secondBMITV = (TextView)rootview.findViewById(R.id.bmi_mean_pen);
        firstBodyFatTV = (TextView)rootview.findViewById(R.id.fat_mean_ult);
        secondBodyFatTV = (TextView)rootview.findViewById(R.id.fat_mean_pen);
        firstBodyWaterTV = (TextView)rootview.findViewById(R.id.water_mean_ult);
        secondBodyWaterTV = (TextView)rootview.findViewById(R.id.water_mean_pen);
        firstMuscleMassTV = (TextView)rootview.findViewById(R.id.muscle_mean_ult);
        secondMuscleMassTV = (TextView)rootview.findViewById(R.id.muscle_mean_pen);
        firstBoneMassTV = (TextView)rootview.findViewById(R.id.bone_mean_ult);
        secondBoneMassTV = (TextView)rootview.findViewById(R.id.bone_mean_pen);

        searchMeanValuesButton = (Button)rootview.findViewById(R.id.search_mean_values);
        searchMeanValuesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendParams(id_pat);

            }
        });

        return rootview;

    }

    private void sendParams(final String id_pat) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        //URL DEL SERVER DI PROVA
        final String final_addr = url+"/test_parametri/valori_medi";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();
                        //Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();

                        try{
                            JSONObject jsonServerResp = new JSONObject(response);
                            JSONObject jBMI = jsonServerResp.getJSONObject("BMI");
                            JSONObject jSpO2 = jsonServerResp.getJSONObject("spo2");
                            JSONObject jBodyFat = jsonServerResp.getJSONObject("body_fat");
                            JSONObject jBodyWater = jsonServerResp.getJSONObject("body_water");
                            JSONObject jBoneMass = jsonServerResp.getJSONObject("bone_value");
                            JSONObject jDiast = jsonServerResp.getJSONObject("diastolic");
                            JSONObject jGlic = jsonServerResp.getJSONObject("glicemia");
                            JSONObject jHR = jsonServerResp.getJSONObject("heart_rate");
                            JSONObject jMuscleMass = jsonServerResp.getJSONObject("muscle_weight");
                            JSONObject jSyst = jsonServerResp.getJSONObject("systolic");
                            JSONObject jWeight = jsonServerResp.getJSONObject("weight");
                            float firstBMI = (float)jBMI.getDouble("1st_month");
                            float secondBMI = (float)jBMI.getDouble(("2nd_month"));
                            float firstSpO2 = (float)jSpO2.getDouble("1st_month");
                            float secondSpO2 = (float)jSpO2.getDouble(("2nd_month"));
                            float firstBodyFat = (float)jBodyFat.getDouble("1st_month");
                            float secondBodyFat = (float)jBodyFat.getDouble(("2nd_month"));
                            float firstBodyWater = (float)jBodyWater.getDouble("1st_month");
                            float secondBodyWater = (float)jBodyWater.getDouble(("2nd_month"));
                            float firstBoneMass = (float)jBoneMass.getDouble("1st_month");
                            float secondBoneMass = (float)jBoneMass.getDouble(("2nd_month"));
                            float firstDiast = (float)jDiast.getDouble("1st_month");
                            float secondDiast = (float)jDiast.getDouble(("2nd_month"));
                            float firstGlic = (float)jGlic.getDouble("1st_month");
                            float secondGlic = (float)jGlic.getDouble(("2nd_month"));
                            float firstHR = (float)jHR.getDouble("1st_month");
                            float secondHR = (float)jHR.getDouble(("2nd_month"));
                            float firstMuscleMass = (float)jMuscleMass.getDouble("1st_month");
                            float secondMuscleMass = (float)jMuscleMass.getDouble(("2nd_month"));
                            float firstSyst = (float) jSyst.getDouble("1st_month");
                            float secondSyst = (float) jSyst.getDouble(("2nd_month"));
                            float firstWeight = (float)jWeight.getDouble("1st_month");
                            float secondWeight = (float)jWeight.getDouble(("2nd_month"));
                            MeanValues.setFirstBMI(firstBMI);
                            MeanValues.setSecondBMI(secondBMI);
                            MeanValues.setFirstSpO2(firstSpO2);
                            MeanValues.setSecondSpO2(secondSpO2);
                            MeanValues.setFirstBodyFat(firstBodyFat);
                            MeanValues.setSecondBodyFat(secondBodyFat);
                            MeanValues.setFirstBodyWater(firstBodyWater);
                            MeanValues.setSecondBodyWater(secondBodyWater);
                            MeanValues.setFirstBoneMass(firstBoneMass);
                            MeanValues.setSecondBoneMass(secondBoneMass);
                            MeanValues.setFirstDias(firstDiast);
                            MeanValues.setSecondDias(secondDiast);
                            MeanValues.setFirstGlic(firstGlic);
                            MeanValues.setSecondGlic(secondGlic);
                            MeanValues.setFirstHR(firstHR);
                            MeanValues.setSecondHR(secondHR);
                            MeanValues.setFirstMuscleMass(firstMuscleMass);
                            MeanValues.setSecondMuscleMass(secondMuscleMass);
                            MeanValues.setFirstSys(firstSyst);
                            MeanValues.setSecondSys(secondSyst);
                            MeanValues.setFirstWeight(firstWeight);
                            MeanValues.setSecondWeight(secondWeight);
                            //10000 è il numero che viene restituito quando non ci sono valori per fare la media
                            //se lo si vuole cambiare basta cambiarlo in tutte queste righe e nel server nella funzione
                            //calcola media, nel metodo post della classe MeanValuesParametersWebService()
                            if (MeanValues.getFirstBMI()==10000){firstBMITV.setText("-");}else{firstBMITV.setText(String.valueOf(MeanValues.getFirstBMI()));}
                            if (MeanValues.getSecondBMI()==10000){secondBMITV.setText("-");}else{secondBMITV.setText(String.valueOf(MeanValues.getSecondBMI()));}
                            if (MeanValues.getFirstSpO2()==10000){firstSpO2TV.setText("-");}else{firstSpO2TV.setText(String.valueOf(MeanValues.getFirstSpO2()));}
                            if (MeanValues.getSecondSpO2()==10000){secondSpO2TV.setText("-");}else{secondSpO2TV.setText(String.valueOf(MeanValues.getSecondSpO2()));}
                            if (MeanValues.getFirstBodyFat()==10000){firstBodyFatTV.setText("-");}else{firstBodyFatTV.setText(String.valueOf(MeanValues.getFirstBodyFat()));}
                            if (MeanValues.getSecondBodyFat()==10000){secondBodyFatTV.setText("-");}else{secondBodyFatTV.setText(String.valueOf(MeanValues.getSecondBodyFat()));}
                            if (MeanValues.getFirstBodyWater()==10000){firstBodyWaterTV.setText("-");}else{firstBodyWaterTV.setText(String.valueOf(MeanValues.getFirstBodyWater()));}
                            if (MeanValues.getSecondBodyWater()==10000){secondBodyWaterTV.setText("-");}else{secondBodyWaterTV.setText(String.valueOf(MeanValues.getSecondBodyWater()));}
                            if (MeanValues.getFirstBoneMass()==10000){firstBoneMassTV.setText("-");}else{firstBoneMassTV.setText(String.valueOf(MeanValues.getFirstBoneMass()));}
                            if (MeanValues.getSecondBoneMass()==10000){secondBoneMassTV.setText("-");}else{secondBoneMassTV.setText(String.valueOf(MeanValues.getSecondBoneMass()));}
                            if (MeanValues.getFirstDias()==10000){firstDiasTV.setText("-");}else{firstDiasTV.setText(String.valueOf(MeanValues.getFirstDias()));}
                            if (MeanValues.getSecondDias()==10000){secondDiasTV.setText("-");}else{secondDiasTV.setText(String.valueOf(MeanValues.getSecondDias()));}
                            if (MeanValues.getFirstGlic()==10000){firstGlicTV.setText("-");}else{firstGlicTV.setText(String.valueOf(MeanValues.getFirstGlic()));}
                            if (MeanValues.getSecondGlic()==10000){secondGlicTV.setText("-");}else{secondGlicTV.setText(String.valueOf(MeanValues.getSecondGlic()));}
                            if (MeanValues.getFirstHR()==10000){firstHRTV.setText("-");}else{firstHRTV.setText(String.valueOf(MeanValues.getFirstHR()));}
                            if (MeanValues.getSecondHR()==10000){secondHRTV.setText("-");}else{secondHRTV.setText(String.valueOf(MeanValues.getSecondHR()));}
                            if (MeanValues.getFirstMuscleMass()==10000){firstMuscleMassTV.setText("-");}else{firstMuscleMassTV.setText(String.valueOf(MeanValues.getFirstMuscleMass()));}
                            if (MeanValues.getFirstMuscleMass()==10000){secondMuscleMassTV.setText("-");}else{secondMuscleMassTV.setText(String.valueOf(MeanValues.getSecondMuscleMass()));}
                            if (MeanValues.getFirstSys()==10000){firstSysTV.setText("-");}else{firstSysTV.setText(String.valueOf(MeanValues.getFirstSys()));}
                            if (MeanValues.getSecondSys()==10000){secondSysTV.setText("-");}else{secondSysTV.setText(String.valueOf(MeanValues.getSecondSys()));}
                            if (MeanValues.getFirstWeight()==10000){firstWeightTV.setText("-");}else{firstWeightTV.setText(String.valueOf(MeanValues.getFirstWeight()));}
                            if (MeanValues.getSecondWeight()==10000){secondWeightTV.setText("-");}else{secondWeightTV.setText(String.valueOf(MeanValues.getSecondWeight()));}

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }




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

                            if (err_msg.equals("no_device")) {

                                AlertDialog.Builder noDeviceAlert = new AlertDialog.Builder(getActivity());
                                noDeviceAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_devices_alert))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noDeviceAlert.show();
                            }

                        }
                        else{
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pat_id", id_pat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }




}



