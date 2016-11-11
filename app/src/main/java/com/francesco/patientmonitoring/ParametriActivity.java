package com.francesco.patientmonitoring;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.francesco.patientmonitoring.adapters.PagerAdapter;
import com.francesco.patientmonitoring.fragmentParametri.ParametriGraficiFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriMediFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriPuntualiFragment;

import java.util.List;
import java.util.Vector;

public class ParametriActivity extends BaseActivity{

    private String nome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametri);
        Intent i = getIntent();
        nome = i.getStringExtra("nome");
        setTitle(nome);
        /*
         * per ora non mi servono
         */
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");
        final String pat_id = i.getStringExtra("id");

        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, ParametriValoriMediFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ParametriValoriPuntualiFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ParametriGraficiFragment.class.getName()));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager_home_paziente);
        pager.setAdapter(adapter);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){

            }

        };

        actionBar.addTab(actionBar.newTab().setText(getString(R.string.label_valori_medi)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.label_valori_puntuali)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.label_valori_puntuali)).setTabListener(tabListener));


        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position){
                actionBar.setSelectedNavigationItem(position);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.nav_parameters:
                return true;

            case R.id.action_settings: {
                Toast.makeText(this, "ciao", Toast.LENGTH_LONG).show();
            }
        }


            return super.onOptionsItemSelected(item);
        }
}


