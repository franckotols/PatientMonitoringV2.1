package com.francesco.patientmonitoring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;

    String pat_id;
    String nome;
    String city;
    String birthdate;
    String physician_id;






    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        Intent i = getIntent();
        pat_id = i.getStringExtra("id");
        nome = i.getStringExtra("nome");
        city = i.getStringExtra("città");
        birthdate = i.getStringExtra("data_di_nascita");
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);


        if (useToolbar())
        {
            setSupportActionBar(toolbar);
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }


        setUpNavView();
    }

    /**
     * Helper method that can be used by child classes to
     * specify that they don't want a {@link Toolbar}
     * @return true
     */
    protected boolean useToolbar()
    {
        return true;
    }

    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        if( useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);

            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_material));
        }
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */
    protected boolean useDrawerToggle()
    {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_send_message:
                Intent i= new Intent(this, PhysicianMessagesActivity.class);
                i.putExtra("nome",nome);
                i.putExtra("città",city);
                i.putExtra("data_di_nascita",birthdate);
                i.putExtra("id",pat_id);
                i.putExtra("physician_id",physician_id);
                return true;
            /*
            L'INTERFACCIA DEI PARAMETRI ANCORA NON FUNZIONA

            case R.id.nav_parameters:
                Intent intent= new Intent(this, ParametriActivity.class);
                intent.putExtra("nome",nome);
                intent.putExtra("città",city);
                intent.putExtra("data_di_nascita",birthdate);
                intent.putExtra("id",pat_id);
                intent.putExtra("physician_id",physician_id);
                startActivity(intent);
                return true;
                */


            case R.id.nav_diary_dp :
                Intent ii = new Intent(this, DiaryDpActivity.class);
                ii.putExtra("nome",nome);
                ii.putExtra("città",city);
                ii.putExtra("data_di_nascita",birthdate);
                ii.putExtra("id",pat_id);
                ii.putExtra("physician_id",physician_id);
                startActivity(ii);
                return true;

            /*
            case R.id.nav_blood:
                startActivity(new Intent(this, BloodActivity.class));
                return true;
                */

            case R.id.nav_urin_analysis:
                Intent iii = new Intent(this, UrinAnalysisActivity.class);
                iii.putExtra("nome",nome);
                iii.putExtra("città",city);
                iii.putExtra("data_di_nascita",birthdate);
                iii.putExtra("id",pat_id);
                iii.putExtra("physician_id",physician_id);
                startActivity(iii);
                return true;



        }


        return super.onOptionsItemSelected(item);
    }
}
