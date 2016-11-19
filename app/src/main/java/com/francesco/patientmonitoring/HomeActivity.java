package com.francesco.patientmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.francesco.patientmonitoring.activities.LoginActivity;
import com.francesco.patientmonitoring.adapters.PagerAdapter;
import com.francesco.patientmonitoring.homeFragments.PromemoriaFragment;
import com.francesco.patientmonitoring.homeFragments.NotificheFragment;
import com.francesco.patientmonitoring.homeFragments.RicercaPazienteFragment;
import com.francesco.patientmonitoring.sharedPreferences.SettingsActivity;

import java.util.List;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {


    /*per tenere in memoria l'id del medico mi conviene usare SharedPreferences*/
    String physician_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        List<Fragment> fragments =new Vector<>();
        fragments.add(Fragment.instantiate(this, NotificheFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RicercaPazienteFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, PromemoriaFragment.class.getName()));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager_home);

        pager.setAdapter(adapter);
        //pager.setOffscreenPageLimit(2);

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

        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_first_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_second_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.title_third_tab)).setTabListener(tabListener));


        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position){
                actionBar.setSelectedNavigationItem(position);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }


        if (id == R.id.action_logout){
            AlertDialog.Builder logoutAlert = new AlertDialog.Builder(HomeActivity.this);
            logoutAlert.setTitle("Attenzione!")
                    .setMessage("Vuoi effettuare il logout?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent i_logout = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i_logout);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            logoutAlert.show();
        }

        return super.onOptionsItemSelected(item);
    }


}