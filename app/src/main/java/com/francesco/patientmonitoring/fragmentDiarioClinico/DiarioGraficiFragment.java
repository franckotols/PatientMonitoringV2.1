package com.francesco.patientmonitoring.fragmentDiarioClinico;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francesco.patientmonitoring.R;

/**
 * Created by Fra on 03/11/2016.
 */
public class DiarioGraficiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        View rootview = inflater.inflate(R.layout.diario_grafici, container, false);
        return rootview;
    }

}
