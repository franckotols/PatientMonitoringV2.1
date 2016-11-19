package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.DiaryDpMeasure;
import com.francesco.patientmonitoring.pojo.UrinMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 15/11/2016.
 */
public class DiaryDPMeasureAdapter extends ArrayAdapter{

    List list = new ArrayList();

    public DiaryDPMeasureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(DiaryDpMeasure object) {
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

        final DiaryDpMeasure diaryDpMeasure =(DiaryDpMeasure) this.getItem(position);

        View row;
        row = convertView;
        DiaryDpMeasHolder diaryDpMeasHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_layout_diarydp_measure,parent,false);
            diaryDpMeasHolder = new DiaryDpMeasHolder();
            diaryDpMeasHolder.tx_date = (TextView) row.findViewById(R.id.date_diarydp_event_tv);

            row.setTag(diaryDpMeasHolder);
        }
        else{
            diaryDpMeasHolder = (DiaryDpMeasHolder) row.getTag();
        }


        diaryDpMeasHolder.tx_date.setText(diaryDpMeasure.getDate());

        return row;
    }

    static class DiaryDpMeasHolder{

        TextView tx_date;


    }

}
