package com.francesco.patientmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.adapters.DiaryDPMeasureAdapter;
import com.francesco.patientmonitoring.adapters.UrinMeasureAdapter;
import com.francesco.patientmonitoring.pojo.DiaryDpMeasure;
import com.francesco.patientmonitoring.pojo.UrinMeasure;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaryDpActivity extends BaseActivity {

    SharedPreferences pref;
    public static final String KEY_PAT_ID = "pat_id";
    private JSONObject jsonServerResp;

    String pat_id;
    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    TextView data;
    TabHost mTabHost;
    TabHost.TabSpec mSpec;

    ListView listView;
    DiaryDPMeasureAdapter mDiaryDPMeasureAdapter;

    /*
     * TextViews Parametri Del Test delle Urine
     */

    //generalità
    TextView tvDateMeas;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvVol1Disch;
    TextView tvUFtot;
    TextView tvSkipCyc;
    TextView tvTimeWaste;
    TextView tvAlarms;
    //conc. sacche
    TextView tvGlu150;
    TextView tvGlu250;
    TextView tvGlu350;
    TextView tvExtran;
    TextView tvEpar;
    TextView tvInsl;
    TextView tvAntib;
    TextView tvVolTot;
    //dati dialisi
    TextView tvVolCar;
    TextView tvTimeCarSos;
    TextView tvTimeDisch;
    TextView tvVolLastCh;
    TextView tvNumCyc;
    TextView tvVolCharTID;
    TextView tvTimeSosTId;
    TextView tvVolUFTid;
    TextView tvTotDischTIDnCyc;
    TextView tvSameConc;
    //scambi manuali
    TextView tvManEx;
    //sintomi
    TextView tvAbdPain;
    TextView tvTorbidDischLiquid;
    TextView tvNausea;
    TextView tvVomit;
    TextView tvDiarr;
    TextView tvDiffDisch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_dp);

        pat_id = PatientInfo.getPatient_id();
        final String nome = PatientInfo.getPatient_name();
        final String city = PatientInfo.getPatient_city();
        final String birthdate = PatientInfo.getPatient_birthdate();

        setTitle(nome+" - "+getString(R.string.diario_clinico));
        tvNome = (TextView)findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);
        data = (TextView)findViewById(R.id.tv_date_diary);
        setTabHost();


        /*
         * Riempimento delle TextView relative ai parametri
         */
        listView=(ListView)findViewById(R.id.list_diaryDP);
        mDiaryDPMeasureAdapter = new DiaryDPMeasureAdapter(this,R.layout.list_layout_diarydp_measure);
        listView.setAdapter(mDiaryDPMeasureAdapter);

        //generalità
        tvDateMeas = (TextView)findViewById(R.id.date_tv);
        tvStartTime = (TextView)findViewById(R.id.tv_terapy_start);
        tvEndTime = (TextView)findViewById(R.id.tv_terapy_end);
        tvVol1Disch = (TextView)findViewById(R.id.tv_vol1_disch);
        tvUFtot = (TextView)findViewById(R.id.tv_uf_tot);
        tvSkipCyc = (TextView)findViewById(R.id.tv_cycles_jumped);
        tvTimeWaste = (TextView)findViewById(R.id.tv_time_wasted);
        tvAlarms = (TextView)findViewById(R.id.tv_alarms);
        //conc. sacche
        tvGlu150 = (TextView)findViewById(R.id.glu150_tv);
        tvGlu250 = (TextView)findViewById(R.id.glu250_tv);
        tvGlu350 = (TextView)findViewById(R.id.glu350_tv);
        tvExtran = (TextView)findViewById(R.id.extran_tv);
        tvEpar = (TextView)findViewById(R.id.epa_tv);
        tvInsl = (TextView)findViewById(R.id.insul_tv);
        tvAntib = (TextView)findViewById(R.id.antib_tv);
        tvVolTot = (TextView)findViewById(R.id.vol_tot_tv);
        //dati dialisi
        tvVolCar = (TextView)findViewById(R.id.tv_charge_volume);
        tvTimeCarSos = (TextView)findViewById(R.id.tv_time_charge);
        tvTimeDisch = (TextView)findViewById(R.id.tv_time_discharge);
        tvVolLastCh = (TextView)findViewById(R.id.tv_last_vol);
        tvNumCyc = (TextView)findViewById(R.id.tv_cycles);
        tvVolCharTID = (TextView)findViewById(R.id.tv_tidal_charge);
        tvTimeSosTId = (TextView)findViewById(R.id.tv_tidal_break);
        tvVolUFTid = (TextView)findViewById(R.id.tv_tidal_vol);
        tvTotDischTIDnCyc = (TextView)findViewById(R.id.tv_tidal_cycles);
        tvSameConc = (TextView)findViewById(R.id.tv_same_conentration);
        //scambi manuali
        tvManEx = (TextView)findViewById(R.id.tv_manual_exchanges);
        //sintomi
        tvAbdPain = (TextView)findViewById(R.id.tv_abdominal_pain);
        tvTorbidDischLiquid = (TextView)findViewById(R.id.tv_turbid);
        tvNausea = (TextView)findViewById(R.id.tv_nausea);
        tvVomit = (TextView)findViewById(R.id.tv_vomit);
        tvDiarr = (TextView)findViewById(R.id.tv_diarrhea);
        tvDiffDisch = (TextView)findViewById(R.id.tv_discharge_problems);

        /*
         * POST REQUEST TO THE WEB SERVICE
         */
        postPatientID();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiaryDpMeasure measure = (DiaryDpMeasure) adapterView.getItemAtPosition(i);
                //string
                String date = measure.getDate();
                tvDateMeas.setText(date);
                data.setText(date);
                String startTime = measure.getStartTime();
                tvStartTime.setText(startTime);
                String endTime = measure.getEndTime();
                tvEndTime.setText(endTime);
                //booleans
                boolean abdPain = measure.isAbdPain();
                if (abdPain){tvAbdPain.setText(getString(R.string.yes_string));tvAbdPain.setTextColor(0xffff0000);} else {tvAbdPain.setText(getString(R.string.no_string));}
                boolean diarrhea = measure.isDiarrhea();
                if (diarrhea){tvDiarr.setText(getString(R.string.yes_string));tvDiarr.setTextColor(0xffff0000);} else {tvDiarr.setText(getString(R.string.no_string));}
                boolean dischProbs = measure.isDischProbs();
                if (dischProbs){tvDiffDisch.setText(getString(R.string.yes_string));tvDiffDisch.setTextColor(0xffff0000);} else {tvDiffDisch.setText(getString(R.string.no_string));}
                boolean nausea = measure.isNausea();
                if (nausea){tvNausea.setText(getString(R.string.yes_string));tvNausea.setTextColor(0xffff0000);} else {tvNausea.setText(getString(R.string.no_string));}
                boolean sameConc = measure.isSameConc();
                if (sameConc){tvSameConc.setText(getString(R.string.yes_string));} else {tvSameConc.setText(getString(R.string.no_string));}
                boolean turbidDisch = measure.isTurbidDisch();
                if (turbidDisch){tvTorbidDischLiquid.setText(getString(R.string.yes_string));tvTorbidDischLiquid.setTextColor(0xffff0000);} else {tvTorbidDischLiquid.setText(getString(R.string.no_string));}
                boolean vomit = measure.isVomit();
                if (vomit){tvVomit.setText(getString(R.string.yes_string));tvVomit.setTextColor(0xffff0000);} else {tvVomit.setText(getString(R.string.no_string));}
                //integers
                int ant_bags = measure.getAnt_bags();
                tvAntib.setText(String.valueOf(ant_bags));
                int cyclNumb = measure.getCyclNumb();
                tvNumCyc.setText(String.valueOf(cyclNumb));
                int dischTime = measure.getDischTime();
                tvTimeDisch.setText(String.valueOf(dischTime));
                int ex_bags = measure.getEx_bags();
                tvExtran.setText(String.valueOf(ex_bags));
                int glu150bags = measure.getGlu150bags();
                tvGlu150.setText(String.valueOf(glu150bags));
                int glu250bags = measure.getGlu250bags();
                tvGlu250.setText(String.valueOf(glu250bags));
                int glu350bags = measure.getGlu350bags();
                tvGlu350.setText(String.valueOf(glu350bags));
                int hepBags = measure.getHepBags();
                tvEpar.setText(String.valueOf(hepBags));
                int insBags = measure.getInsBags();
                tvInsl.setText(String.valueOf(insBags));
                int lastLoadVol = measure.getLastLoadVol();
                tvVolLastCh.setText(String.valueOf(lastLoadVol));
                int loadStopTime = measure.getLoadStopTime();
                tvTimeCarSos.setText(String.valueOf(loadStopTime));
                int loadVol = measure.getLoadVol();
                tvVolCar.setText(String.valueOf(loadVol));
                int loadVolTIDAL = measure.getLoadVolTIDAL();
                tvVolCharTID.setText(String.valueOf(loadVolTIDAL));
                int lostTime = measure.getLostTime();
                tvTimeWaste.setText(String.valueOf(lostTime));
                int skipCyc = measure.getSkipCyc();
                tvSkipCyc.setText(String.valueOf(skipCyc));
                int stopTimeTIDAL = measure.getStopTimeTIDAL();
                tvTimeSosTId.setText(String.valueOf(stopTimeTIDAL));
                int totVol = measure.getTotVol();
                tvVolTot.setText(String.valueOf(totVol));
                int totUF = measure.getTotUF();
                tvUFtot.setText(String.valueOf(totUF));
                int volum1Disch = measure.getVolum1Disch();
                tvVol1Disch.setText(String.valueOf(volum1Disch));
                int volumUFTidal = measure.getVolumUFTidal();
                tvVolUFTid.setText(String.valueOf(volumUFTidal));
                String alarmsMachine = measure.getAlarmsMachine();
                tvAlarms.setText(alarmsMachine);
                tvAlarms.setTextColor(0xffff0000);
                //String: this must be converted in json
                String manualExchanges = measure.getManualExchanges();
                tvManEx.setText(manualExchanges);
                int totDischnCycTIDAL = measure.getTotDischnCycTIDAL();
                tvTotDischTIDnCyc.setText(String.valueOf(totDischnCycTIDAL));
                tvDateMeas.setText(date);

            }
        });

  }



    private void setTabHost(){
        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        //Lets add the first Tab
        mSpec = mTabHost.newTabSpec("Generale");
        mSpec.setContent(R.id.lay_one);
        mSpec.setIndicator("Generale");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Conc. Sacche");
        mSpec.setContent(R.id.lay_two);
        mSpec.setIndicator("Conc. Sacche");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Dati Dialisi");
        mSpec.setContent(R.id.lay_three);
        mSpec.setIndicator("Dati Dialisi");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Azioni Manuali");
        mSpec.setContent(R.id.lay_four);
        mSpec.setIndicator("Azioni Manuali");
        mTabHost.addTab(mSpec);
        //Lets add the second Tab
        mSpec = mTabHost.newTabSpec("Sintomi");
        mSpec.setContent(R.id.lay_five);
        mSpec.setIndicator("Sintomi");
        mTabHost.addTab(mSpec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_paziente,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(DiaryDpActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_home){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(DiaryDpActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.back_home_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_home = new Intent(DiaryDpActivity.this, HomeActivity.class);
                            startActivity(i_home);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            logoutAlert.show();
        }

        if (id == R.id.action_logout){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(DiaryDpActivity.this);
            logoutAlert.setTitle(getString(R.string.attention_message))
                    .setMessage(getString(R.string.logout_advice))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(DiaryDpActivity.this, LoginActivity.class);
                            startActivity(i_logout);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            logoutAlert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void postPatientID() {

        final ProgressDialog pd = new ProgressDialog(DiaryDpActivity.this);

        pd.setMessage(getString(R.string.process_dialog_waiting));
        pd.show();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/api/diaryDp";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        //Toast.makeText(DiaryDpActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            //trasforma la stringa in oggetto json
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("results");
                            String date, startTime, endTime;
                            boolean abdPain,diarrhea,dischProbs,nausea,sameConc, turbidDisch,vomit;
                            int ant_bags, cyclNumb,dischTime,ex_bags,glu150bags,glu250bags,glu350bags,hepBags,insBags,totDischnCycTIDAL;
                            int lastLoadVol,loadStopTime,loadVol,loadVolTIDAL,lostTime,skipCyc,stopTimeTIDAL,totVol,totUF,volum1Disch, volumUFTidal;
                            String alarmsMachine;
                            String manualExchanges;


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONObject measurements = jsonObj.getJSONObject("measurements");
                                //dates
                                date = jsonObj.getString("eventDate");

                                startTime = measurements.getString("treatment_startTime");
                                endTime = measurements.getString("treatment_endTime");
                                //booleans
                                abdPain = measurements.getBoolean("abdominalPain");
                                //Toast.makeText(DiaryDpActivity.this,String.valueOf(abdPain),Toast.LENGTH_LONG).show();
                                diarrhea = measurements.getBoolean("diarrhea");
                                dischProbs = measurements.getBoolean("dischargeProblems");
                                nausea = measurements.getBoolean("nausea");
                                sameConc = measurements.getBoolean("sameConcentration");
                                turbidDisch = measurements.getBoolean("turbidDischargeLiquid");
                                vomit = measurements.getBoolean("vomit");

                                //integers
                                ant_bags = measurements.getInt("antibiotic_bags");
                                //Toast.makeText(DiaryDpActivity.this,"1",Toast.LENGTH_LONG).show();
                                cyclNumb = measurements.getInt("cyclesNumber");
                                //Toast.makeText(DiaryDpActivity.this,"2",Toast.LENGTH_LONG).show();
                                dischTime = measurements.getInt("dischargeTime");
                                ex_bags = measurements.getInt("extran_bags");
                                //Toast.makeText(DiaryDpActivity.this,"3",Toast.LENGTH_LONG).show();
                                glu150bags = measurements.getInt("glucose150_bags");
                                //Toast.makeText(DiaryDpActivity.this,"4",Toast.LENGTH_LONG).show();
                                glu250bags = measurements.getInt("glucose250_bags");
                                //Toast.makeText(DiaryDpActivity.this,"5",Toast.LENGTH_LONG).show();
                                glu350bags = measurements.getInt("glucose350_bags");
                                //Toast.makeText(DiaryDpActivity.this,"6",Toast.LENGTH_LONG).show();
                                hepBags = measurements.getInt("heparin_bags");
                                //Toast.makeText(DiaryDpActivity.this,"",Toast.LENGTH_LONG).show();
                                insBags = measurements.getInt("insulin_bags");
                                //Toast.makeText(DiaryDpActivity.this,"7",Toast.LENGTH_LONG).show();
                                lastLoadVol = measurements.getInt("lastLoadingVolume");
                                //Toast.makeText(DiaryDpActivity.this,"7",Toast.LENGTH_LONG).show();
                                loadStopTime = measurements.getInt("loadStopTime");
                                //Toast.makeText(DiaryDpActivity.this,"9",Toast.LENGTH_LONG).show();
                                loadVol = measurements.getInt("loadingVolume");
                                //Toast.makeText(DiaryDpActivity.this,"10",Toast.LENGTH_LONG).show();
                                loadVolTIDAL = measurements.getInt("loadingVolume_TIDAL");
                                //Toast.makeText(DiaryDpActivity.this,"11",Toast.LENGTH_LONG).show();
                                lostTime = measurements.getInt("lostTime");
                                //Toast.makeText(DiaryDpActivity.this,"12",Toast.LENGTH_LONG).show();
                                skipCyc = measurements.getInt("skippedCycles");
                                //Toast.makeText(DiaryDpActivity.this,"13",Toast.LENGTH_LONG).show();
                                stopTimeTIDAL = measurements.getInt("stopTime_TIDAL");
                                //Toast.makeText(DiaryDpActivity.this,"14",Toast.LENGTH_LONG).show();
                                totVol = measurements.getInt("totalVolume");
                                totUF = measurements.getInt("total_UF");
                                volum1Disch = measurements.getInt("vol_1_discharge");

                                volumUFTidal = measurements.getInt("volumeUF_TIDAL");
                                //string alarm
                                alarmsMachine = measurements.getString("machineDP_Alarms");
                                //Toast.makeText(DiaryDpActivity.this,alarmsMachine,Toast.LENGTH_LONG).show();
                                //String: this must be converted in json
                                manualExchanges = measurements.getString("manualExchanges");
                                totDischnCycTIDAL = measurements.getInt("totalDischarge_nCycles_TIDAL");



                                DiaryDpMeasure diaryDpMeasure = new DiaryDpMeasure(date, startTime, endTime, abdPain, diarrhea, dischProbs, nausea, sameConc, turbidDisch, vomit, ant_bags, cyclNumb, dischTime, ex_bags, glu150bags, glu250bags, hepBags, glu350bags, insBags, lastLoadVol, loadStopTime, loadVol, loadVolTIDAL, lostTime, skipCyc, stopTimeTIDAL, totVol, totUF, volum1Disch, volumUFTidal, alarmsMachine, manualExchanges,totDischnCycTIDAL);
                                mDiaryDPMeasureAdapter.add(diaryDpMeasure);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mDiaryDPMeasureAdapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        //la riga successiva serve perchè altrimenti ad ogni richiesta accumulerebbe
                        //le patologie selezionate

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();

                        NetworkResponse err_ = error.networkResponse;
                        //String display_err_user_msg="\n\n\nError in sending request.";
                        if (err_ != null && err_.data != null) {
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

                            if (err_msg.equals("no_values")) {

                                AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(DiaryDpActivity.this);
                                wrongParamsAlert.setTitle(getString(R.string.attention_message))
                                        .setMessage(getString(R.string.no_analysis))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                wrongParamsAlert.show();

                            }


                        }

                        else {

                            AlertDialog.Builder noServerAlert = new AlertDialog.Builder(DiaryDpActivity.this);
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
                params.put(KEY_PAT_ID, pat_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
