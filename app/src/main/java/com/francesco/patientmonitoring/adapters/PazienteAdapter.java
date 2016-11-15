package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.francesco.patientmonitoring.HomePazienteActivity;
import com.francesco.patientmonitoring.PhysicianMessagesActivity;
import com.francesco.patientmonitoring.pojo.Pazienti;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.utilities.PatientInfo;

import java.util.ArrayList;
import java.util.Arrays;
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
            row = layoutInflater.inflate(R.layout.list_layout_patient,parent,false);
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
                    PatientInfo.setPatient_name(pazienti.getFull_name());
                    PatientInfo.setPatient_city(pazienti.getCittà());
                    PatientInfo.setPatient_birthdate(pazienti.getBirthdate());
                    PatientInfo.setPatient_id(pazienti.getPat_id());

                    boolean[] reset = {false, false, false, false, false, false, false, false};
                    PatientInfo.setDiseases(reset);
                    PatientInfo.setList(pazienti.getList_disease());
                    ArrayList<String> list_dis = pazienti.getList_disease();
                    if(list_dis.contains("Dialisi Peritoneale")){
                        PatientInfo.getDiseases()[PatientInfo.Disease.peritoneale.ordinal()] = true;
                    }
                    else if(list_dis.contains("Emodialisi")){
                        PatientInfo.getDiseases()[PatientInfo.Disease.emodialisi.ordinal()] = true;
                    }
                    else if(list_dis.contains("Esami del Sangue")){
                        PatientInfo.getDiseases()[PatientInfo.Disease.sangue.ordinal()] = true;
                    }
                    else if (list_dis.contains("Test delle Urine")){
                        PatientInfo.getDiseases()[PatientInfo.Disease.urine.ordinal()] = true;
                    }


                    Intent intent= new Intent(getContext(), HomePazienteActivity.class);
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
