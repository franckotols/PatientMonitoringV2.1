package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.francesco.patientmonitoring.BaseActivity;
import com.francesco.patientmonitoring.ParametriActivity;
import com.francesco.patientmonitoring.PhysicianMessagesActivity;
import com.francesco.patientmonitoring.UrinAnalysisActivity;
import com.francesco.patientmonitoring.pojo.Pazienti;
import com.francesco.patientmonitoring.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 20/09/2016.
 */
public class PazienteAdapter extends ArrayAdapter {
    List list = new ArrayList();
    String mString; //questa stringa mi serve per mettere nell'intent anche l'id del medico

    public PazienteAdapter(Context context, int resource,final String mString) {
        super(context, resource);
        this.mString = mString;
    }

    public void add(Pazienti object) {
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

        final Pazienti pazienti =(Pazienti)this.getItem(position);

        View row;
        row = convertView;
        PatientHolder patientHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.patient_list_layout,parent,false);
            patientHolder = new PatientHolder();
            patientHolder.tx_name = (TextView) row.findViewById(R.id.tx_name);
            patientHolder.tx_city = (TextView) row.findViewById(R.id.tx_city);
            patientHolder.tx_birthdate = (TextView) row.findViewById(R.id.tx_birthdate);
            patientHolder.listItemBtn = (Button) row.findViewById(R.id.list_item_button);
            patientHolder.listItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * per passare all'activity specifica del paziente
                     */
                    Intent intent= new Intent(getContext(), PhysicianMessagesActivity.class);
                    intent.putExtra("nome",pazienti.getFull_name());
                    intent.putExtra("città",pazienti.getCittà());
                    intent.putExtra("data_di_nascita",pazienti.getBirthdate());
                    intent.putExtra("id",pazienti.getPat_id());
                    intent.putExtra("physician_id",mString);
                    getContext().startActivity(intent);

                }
            });
            row.setTag(patientHolder);
        }
        else{
            patientHolder = (PatientHolder)row.getTag();
        }


        patientHolder.tx_name.setText(pazienti.getFull_name());
        patientHolder.tx_city.setText(pazienti.getCittà());
        patientHolder.tx_birthdate.setText(pazienti.getBirthdate());

        return row;
    }

    static class PatientHolder{

        TextView tx_name, tx_city,tx_birthdate;
        Button listItemBtn;

    }


}
