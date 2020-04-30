package com.tplcorp.covid_trakking.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.Helper.ProtectedHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Service.BackgroundService;

import java.util.Date;
import java.util.List;

import re.robz.bottomnavigation.circularcolorreveal.BottomNavigationCircularColorReveal;


public class MainActivityOld extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer_layout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView IV_manu;
    Button tested_button;
    MyDatabase myDatabase;
    TextView textPositive;
    LinearLayout mainLinear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        IV_manu = findViewById(R.id.IV_manu);
        tested_button = findViewById(R.id.tested_button);
        textPositive = findViewById(R.id.textPositive);
        mainLinear = findViewById(R.id.mainLinear);

        tested_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(checkAffectedDate() >= 0 && checkAffectedDate() <= 14)) {
                    showAlertDialog();
                } else {
                    Toast.makeText(MainActivityOld.this, "You need to wait 14 days to mark again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolBarHandling();

    }

    @Override
    protected void onResume() {
        super.onResume();

        ProtectedHelper.startPowerSaverIntent(this);

        if (!GeneralHelper.isTimeAutomatic(this)) {
            mainLinear.setVisibility(View.GONE);
            Toast.makeText(this, "Please change the Date&Time settings to automatically", Toast.LENGTH_LONG).show();
            stopService(new Intent(this, BackgroundService.class));
        } else {
            mainLinear.setVisibility(View.VISIBLE);
            checkBannerState();
            startService(new Intent(this, BackgroundService.class));
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void switchScreen(int id) {

        switch (id) {

            case R.id.Connections:
                Intent Connections = new Intent(MainActivityOld.this, ConnectionsActivity.class);
                startActivity(Connections);
                break;
            case R.id.Precautions:
                Intent Precautions = new Intent(MainActivityOld.this, PrecautionsActivity.class);
                startActivity(Precautions);
                break;
            case R.id.About:
                Toast.makeText(this, "About not configured", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Feedback:
                Toast.makeText(this, "Feedback not configured", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void toolBarHandling() {

        try {

            IV_manu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getDrawer_layout().isDrawerOpen(Gravity.LEFT)) {
                        getDrawer_layout().closeDrawer(Gravity.RIGHT);
                    } else {
                        getDrawer_layout().openDrawer(Gravity.LEFT);
                    }
                }
            });

            toggle = new ActionBarDrawerToggle(
                    MainActivityOld.this, drawer_layout, R.string.app_name, R.string.app_name);
            drawer_layout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switchScreen(item.getItemId());
                    return false;
                }
            });

        } catch (Exception e) {
        }

    }

    public DrawerLayout getDrawer_layout() {
        return drawer_layout;
    }

    public void showAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Report COVID-19")
                .setMessage("Are you sure ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PrefsHelper.putString(PrefConstants.AFFECTED, "1");
                        CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
                        myDatabase.daoAccess().deleteCovidAffects();
                        myDatabase.daoAccess().insertAffectedRecord(covidAffected);
                        checkBannerState();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PrefsHelper.putString(PrefConstants.AFFECTED, "0");
                        checkBannerState();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private long checkAffectedDate() {

        long days = -1;

        myDatabase = DatabaseClient.getDatabaseInstance(this);
        List<CovidAffected> affectedList = myDatabase.daoAccess().affectedList();
        if (affectedList.size() > 0) {
            Date date = affectedList.get(0).getTIME_STAMP();
            days = GeneralHelper.daysDifferent(GeneralHelper.todayDate_DATE(), date);
            return days;
        }

        return days;
    }

    private void checkBannerState() {
        if (PrefsHelper.getString(PrefConstants.AFFECTED, "0").equals("1")) {
            long days = checkAffectedDate();
            if (days == 0) {
                textPositive.setText("You have marked yourself Covid-19 positive today");
            } else {
                textPositive.setText("You had marked yourself Covid-19 positive " + days + " day ago");
            }
            textPositive.setVisibility(View.VISIBLE);
        } else {
            textPositive.setVisibility(View.GONE);
        }

    }


}