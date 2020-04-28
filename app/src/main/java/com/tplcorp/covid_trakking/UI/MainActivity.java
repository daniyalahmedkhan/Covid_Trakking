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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Service.BackgroundService;


public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer_layout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView IV_manu;
    Button tested_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = (DrawerLayout ) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView =  navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        IV_manu = findViewById(R.id.IV_manu);
        tested_button = findViewById(R.id.tested_button);

        tested_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        toolBarHandling();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, BackgroundService.class));
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

    private void switchScreen(int id){

        switch (id){

            case R.id.Connections:
                Intent Connections = new Intent(MainActivity.this , ConnectionsActivity.class);
                startActivity(Connections);
                break;
            case R.id.Precautions:
                Intent Precautions = new Intent(MainActivity.this , PrecautionsActivity.class);
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

    private void toolBarHandling(){

        try {

            IV_manu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getDrawer_layout().isDrawerOpen(Gravity.LEFT))
                    {
                        getDrawer_layout().closeDrawer(Gravity.RIGHT);
                    }
                    else {
                        getDrawer_layout().openDrawer(Gravity.LEFT);
                    }
                }
            });

            toggle = new ActionBarDrawerToggle(
                    MainActivity.this, drawer_layout, R.string.app_name, R.string.app_name);
            drawer_layout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switchScreen(item.getItemId());
                    return false;
                }
            });

        }catch (Exception e){}

    }

    public DrawerLayout getDrawer_layout() {
        return drawer_layout;
    }

    public void showAlertDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("COVID-19 Test")
                .setMessage("Are you sure you want to declare yourself positive COVID-19?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PrefsHelper.putString(PrefConstants.AFFECTED , "1");
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PrefsHelper.putString(PrefConstants.AFFECTED , "0");
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}