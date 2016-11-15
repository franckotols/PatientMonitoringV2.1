package com.francesco.patientmonitoring.fragmentParametri;

import android.app.DatePickerDialog;
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
import com.jjoe64.graphview.series.LineGraphSeries;

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
    String fromDateStr;
    String toDateStr;

    Spinner paramSelectorSpinner;
    Spinner dateIntervalSelectorSpinner;
    Button searchButton;
    SpinnerParams paramSpinner;
    SpinnerParams intervalSpinner;
    ArrayList<SpinnerParams> params;
    ArrayList<SpinnerParams> intervals;
    SharedPreferences pref;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    //private DatePickerDialog fromDatePickerDialog;
    //private DatePickerDialog toDatePickerDialog;
    //private SimpleDateFormat dateFormatter;

    private LineGraphSeries<DataPoint> mSeries1;
    private GraphView graph;
    int dim_array;
    String[] dates;
    Date firstDate;
    Date endDate;
    JSONObject jsonServerResp;


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
         * DatePickerInitialization


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        fromDateEtxt = (EditText) rootview.findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) rootview.findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);
    */


        /*
         * Graph initialization
         */


        graph = (GraphView)rootview.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();



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

    //per la selezione della data
    /*
    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                fromDateStr = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                toDateStr = dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    */
    //Volley request

    private void sendParams(final String id_pat, final String interval_id, final String param_id) {

        final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        LineGraphSeries<DataPoint> series;
        graph.clearSecondScale();



        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/testgrafici";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //cancella i grafici precedenti
                        graph.removeAllSeries();
                        //ArrayList<Date> dates= new ArrayList<Date>();



                        //Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT).show();
                        JSONObject jsonServerResp = null;
                        try {
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("results");
                            dim_array = jsonArray.length();
                            dates = new String[dim_array];
                            final DataPoint[] points =new DataPoint[dim_array];
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject json_item = jsonArray.getJSONObject(i);
                                String date_meas = json_item.getString("measurementDate");
                                //Date dataMisura = format.parse(date_meas);
                                Integer value = json_item.getInt("value");
                                points[i] = new DataPoint(i, value);
                                dates[i] = date_meas;

                            }
                            mSeries1 = new LineGraphSeries<>(points);
                            //firstDate = dates.get(0);
                            //endDate = dates.get(dim_array);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }/* catch (ParseException b) {
                            b.printStackTrace();
                        }*/

                        graph.addSeries(mSeries1);
                        graph.getGridLabelRenderer().setNumHorizontalLabels(dim_array);
                        // use static labels for horizontal and vertical labels
                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                        staticLabelsFormatter.setHorizontalLabels(dates);
                        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                        //graph.getViewport().setMinX(firstDate.getTime());
                        //graph.getViewport().setMaxX(endDate.getTime());
                        //graph.getViewport().setXAxisBoundsManual(true);
                        graph.getGridLabelRenderer().setNumHorizontalLabels(dim_array);
                        graph.getViewport().setScrollable(true); // enables horizontal scrolling
                        graph.getViewport().setScrollableY(true); // enables vertical scrolling
                        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling



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
                            if (err_msg.equals("empty")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getActivity());
                                wrongParamsAlert.setTitle("Attenzione!")
                                        .setMessage("Non ci sono valori da mostrare nell'intervallo selezionato ")
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
                                    noServerAlert.setTitle("Attenzione!")
                                            .setMessage("Problema di Connessione al Server, attendere!")
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
                            noServerAlert.setTitle("Attenzione!")
                                    .setMessage("Problema di Connessione al Server, attendere!")
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
                params.put("id_pat", id_pat);
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
