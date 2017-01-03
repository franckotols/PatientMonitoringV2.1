package com.francesco.patientmonitoring.fragmentParametri;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.francesco.patientmonitoring.utilities.PatientInfo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriGraficiFragment extends Fragment implements View.OnClickListener{

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;


    String id_pat;

    Spinner paramSelectorSpinner;
    Spinner dateIntervalSelectorSpinner;
    Button searchButton;
    SpinnerParams paramSpinner;
    SpinnerParams intervalSpinner;
    ArrayList<SpinnerParams> params;
    ArrayList<SpinnerParams> intervals;
    SharedPreferences pref;


    private LineGraphSeries<DataPoint> mSeries1;
    private GraphView graph;
    int dim_array;


    JSONObject jsonServerResp;

    //per la gestione con le stringhe nell'asse x
    String[] datesStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        View rootview = inflater.inflate(R.layout.parametri_grafici, container, false);

        searchButton = (Button)rootview.findViewById(R.id.search_graphs);
        searchButton.setOnClickListener(this);

        /*
         * Riempimento delle textView relative alle info del paziente
         */
        id_pat = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        tvNome = (TextView)rootview.findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)rootview.findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)rootview.findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        /*
         * Spinner Configuration
         */
        params = setParamType();
        paramSelectorSpinner  = (Spinner)rootview.findViewById(R.id.spinner_params_graph);
        ArrayAdapter<SpinnerParams> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,params);
        paramSelectorSpinner.setAdapter(adapter1);
        paramSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paramSpinner = (SpinnerParams)parent.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        intervals = setDateInterval();
        dateIntervalSelectorSpinner = (Spinner)rootview.findViewById(R.id.spinner_date_interval);
        ArrayAdapter<SpinnerParams> adapter2 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,intervals);
        dateIntervalSelectorSpinner.setAdapter(adapter2);
        dateIntervalSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intervalSpinner = (SpinnerParams)parent.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
         * Graph initialization
         */
        graph = (GraphView)rootview.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();
        /**
        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(),String.valueOf(dataPoint),Toast.LENGTH_SHORT).show();
            }
        });*/

        return rootview;
    }

    private ArrayList<SpinnerParams> setDateInterval(){
        ArrayList<SpinnerParams> params = new ArrayList<>();
        String[] ids = getResources().getStringArray(R.array.mean_selector_ids);
        String[] names = getResources().getStringArray(R.array.mean_selector_names);
        for (int i=0;i<ids.length;i++){
            params.add(new SpinnerParams(ids[i],names[i]));
        }
        return params;
    }

    //riempimento spinner
    private ArrayList<SpinnerParams> setParamType(){
        ArrayList<SpinnerParams> params = new ArrayList<>();
        String[] ids = getResources().getStringArray(R.array.params_ids);
        String[] names = getResources().getStringArray(R.array.params_names);
        for (int i=0;i<ids.length;i++){
            params.add(new SpinnerParams(ids[i],names[i]));
        }
        return params;
    }


    //Volley request

    private void sendParams(final String id_pat, final String interval_id, final String param_id) {

        //final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        LineGraphSeries<DataPoint> series;
        mSeries1 = new LineGraphSeries<>();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        //ATTENZIONE
        //E' l'URL del server di prova
        final String final_addr = url+"/test_parametri/grafico";
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.process_dialog_waiting));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //cancella i grafici precedenti



                        //cancella i grafici precedenti
                        graph.removeAllSeries();
                        pd.dismiss();

                        //*
                        ArrayList<Date> dates = new ArrayList<>();
                        //Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT).show();

                        try {
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("results");
                            dim_array = jsonArray.length();

                            final DataPoint[] points =new DataPoint[dim_array];
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject json_item = jsonArray.getJSONObject(i);
                                String date_meas = json_item.getString("measurementDate");
                                Date dataMisura = format.parse(date_meas);
                                //*
                                dates.add(dataMisura);
                                Integer value = json_item.getInt("value");
                                points[i] = new DataPoint(dataMisura, value);


                            }
                            //*
                            int lenDates = dates.size();
                            mSeries1 = new LineGraphSeries<>(points);
                            //*
                            Date firstDate = dates.get(0);
                            Date endDate = dates.get(lenDates-1);
                            graph.addSeries(mSeries1);
                            // use static labels for horizontal and vertical labels
                            //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                            //staticLabelsFormatter.setHorizontalLabels(dates);
                            //graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                            graph.getGridLabelRenderer().setNumHorizontalLabels(lenDates);
                            //*
                            graph.getViewport().setMinX(firstDate.getTime());
                            graph.getViewport().setMaxX(endDate.getTime());
                            //*
                            graph.getViewport().setXAxisBoundsManual(true);

                            //COMMENTANDO QUESTE RIGHE IL  GRAFICO SPARISCE
                            graph.getGridLabelRenderer().setNumHorizontalLabels(dim_array);
                            graph.getViewport().setScrollable(true); // enables horizontal scrolling
                            graph.getViewport().setScrollableY(true); // enables vertical scrolling
                            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException b) {
                            b.printStackTrace();
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
                            if (err_msg.equals("empty")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getActivity());
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.graph_alert_no_values))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();

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

                            if (err_msg.equals("no_device")) {

                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                noServerAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.graph_alert_no_devices))
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pat_id", id_pat);
                params.put("interval_duration", interval_id);
                params.put("param_id", param_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View view) {
        /*
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
        */
        if(view == searchButton) {

            final String param_id = paramSpinner.getId();
            final String interval_id = intervalSpinner.getId();

            sendParams(id_pat,interval_id,param_id);
        }
    }
}
