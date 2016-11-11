package com.francesco.patientmonitoring;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fra on 14/10/2016.
 */
public class EsamiFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        return inflater.inflate(R.layout.esami_frag2, container, false);
    }

}
