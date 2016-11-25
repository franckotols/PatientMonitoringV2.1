package com.francesco.patientmonitoring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.DiaryDpMeasure;
import com.francesco.patientmonitoring.pojo.DiaryEmoMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 21/11/2016.
 */
public class DiaryEmoMeasureAdapter extends ArrayAdapter {


    List list = new ArrayList();

    public DiaryEmoMeasureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(DiaryEmoMeasure object) {
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

        final DiaryEmoMeasure diaryEmoMeasure =(DiaryEmoMeasure) this.getItem(position);

        View row;
        row = convertView;
        DiaryEmoMeasHolder diaryEmoMeasHolder;

        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_layout_diaryemo_measure,parent,false);
            diaryEmoMeasHolder = new DiaryEmoMeasHolder();
            diaryEmoMeasHolder.tx_date = (TextView) row.findViewById(R.id.date_diaryemo_event_tv);

            row.setTag(diaryEmoMeasHolder);
        }
        else{
            diaryEmoMeasHolder = (DiaryEmoMeasHolder) row.getTag();
        }


        diaryEmoMeasHolder.tx_date.setText(diaryEmoMeasure.getDate());

        return row;
    }

    static class DiaryEmoMeasHolder{

        TextView tx_date;


    }


}
