package com.francesco.patientmonitoring.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Fra on 14/10/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i){
        return fragments.get(i);
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

}
