package com.francesco.patientmonitoring.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.pojo.Alerts;
import com.francesco.patientmonitoring.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fra on 29/09/2016.
 */
public class AlertAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public static final String KEY_ID_ALERT= "alert_id";
    public static final String KEY_STATUS_ALERT = "alert_status";

    public AlertAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Alerts object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        AlertHolder alertHolder;
        final Alerts alerts =(Alerts) this.getItem(position);
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_layout_alert,parent,false);
            alertHolder = new AlertHolder();
            alertHolder.tx_pat_name = (TextView) row.findViewById(R.id.tv_pat_name);
            alertHolder.tx_date = (TextView) row.findViewById(R.id.tv_date);
            alertHolder.tx_type = (TextView) row.findViewById(R.id.tv_type);
            alertHolder.tx_message = (TextView) row.findViewById(R.id.tv_message);
            alertHolder.cbRead = (CheckBox) row.findViewById(R.id.cb_read);
            alertHolder.cbRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        //Toast.makeText(getContext(), "checked", Toast.LENGTH_LONG).show();
                        sendParams(alerts.getId(),"checked");
                    }
                    else
                    {
                        //Toast.makeText(getContext(), "not checked", Toast.LENGTH_LONG).show();
                        sendParams(alerts.getId(),"not_checked");
                    }
                }
            });

            row.setTag(alertHolder);
        }
        else{
            alertHolder = (AlertHolder)row.getTag();
        }


        alertHolder.tx_pat_name.setText(alerts.getPatient_name());
        alertHolder.tx_date.setText(alerts.getDate());
        alertHolder.tx_type.setText(alerts.getType());
        alertHolder.tx_message.setText(alerts.getMessage());
        String status = alerts.getRead_status();
        if (status.equals("true")) {
            alertHolder.cbRead.setChecked(true);
        }

        return row;
    }

    static class AlertHolder{

        TextView tx_pat_name,tx_date,tx_type,tx_message;
        CheckBox cbRead;

    }

    private void sendParams(final String id, final String status){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/notifications";
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getContext().getString(R.string.process_dialog_waiting));
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

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
                            if (err_msg.equals("wrong_params")) {
                                Toast.makeText(getContext(), "no params", Toast.LENGTH_SHORT).show();
                            }
                            if (err_msg.equals("no_server")) {
                                Toast.makeText(getContext(), "no server", Toast.LENGTH_SHORT).show();
                            }
                                                    }
                        else{
                            Toast.makeText(getContext(), "other", Toast.LENGTH_SHORT).show();
                        }

                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID_ALERT, id);
                params.put(KEY_STATUS_ALERT, status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
