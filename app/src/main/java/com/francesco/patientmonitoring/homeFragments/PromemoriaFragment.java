package com.francesco.patientmonitoring.homeFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francesco.patientmonitoring.R;

public class PromemoriaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        View rootview = inflater.inflate(R.layout.fragment_promemoria, container, false);
        return rootview;
    }
}
