package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.BloodMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 17/11/2016.
 */
public class BloodMeasureAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public BloodMeasureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(BloodMeasure object) {
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

        final BloodMeasure bloodMeasure =(BloodMeasure) this.getItem(position);

        View row;
        row = convertView;
        BloodMeasureHolder bloodMeasureHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_layout_blood_measure,parent,false);
            bloodMeasureHolder = new BloodMeasureHolder();
            bloodMeasureHolder.tx_date = (TextView) row.findViewById(R.id.date_blood_event_tv);
            row.setTag(bloodMeasureHolder);
        }
        else{
            bloodMeasureHolder = (BloodMeasureHolder) row.getTag();
        }


        bloodMeasureHolder.tx_date.setText(bloodMeasure.getDate());

        return row;
    }

    static class BloodMeasureHolder{

        TextView tx_date;


    }
    
    
}
