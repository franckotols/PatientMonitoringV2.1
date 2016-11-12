package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.UrinMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 11/11/2016.
 */
public class UrinMeasureAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public UrinMeasureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(UrinMeasure object) {
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

        final UrinMeasure urinMeasure =(UrinMeasure) this.getItem(position);

        View row;
        row = convertView;
        UrinMeasHolder urinMeasHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_layout_urin_measure,parent,false);
            urinMeasHolder = new UrinMeasHolder();
            urinMeasHolder.tx_date = (TextView) row.findViewById(R.id.date_urin_event_tv);
            urinMeasHolder.tx_manufacturer = (TextView) row.findViewById(R.id.manufacturer_strip);

            row.setTag(urinMeasHolder);
        }
        else{
            urinMeasHolder = (UrinMeasHolder) row.getTag();
        }


        urinMeasHolder.tx_date.setText(urinMeasure.getDate());
        urinMeasHolder.tx_manufacturer.setText(urinMeasure.getManufacturer());

        return row;
    }

    static class UrinMeasHolder{

        TextView tx_date, tx_manufacturer;


    }


}


