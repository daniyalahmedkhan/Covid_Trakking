package com.tplcorp.covid_trakking.UI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.fragments.ConnectionsFragment;
import com.tplcorp.covid_trakking.UI.fragments.HomeFragment;
import com.tplcorp.covid_trakking.UI.fragments.PrecautionsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import re.robz.bottomnavigation.circularcolorreveal.BottomNavigationCircularColorReveal;


public class MainActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);


        int navigationBarHeight = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        bottomNavigation.setMinimumHeight(navigationBarHeight);


        configBottomNavigation();


        initFragment();

    }

    private void initFragment() {

        addDockableFragment(HomeFragment.newInstance());
    }


    private void configBottomNavigation() {
        int[] colors = getResources().getIntArray(R.array.menu_colors);

        BottomNavigationCircularColorReveal reveal = new BottomNavigationCircularColorReveal(colors);

        reveal.setuptWithBottomNavigationView(bottomNavigation);

        reveal.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switchScreen(item.getItemId());
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>1)
        {
            super.onBackPressed();
        }
        else
        {
            finish();
        }

//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
    }

    private void switchScreen(int id) {

        switch (id) {

            case R.id.Home:
             initFragment();
                break;

            case R.id.Connections:
//                Intent Connections = new Intent(MainActivity.this, ConnectionsActivity.class);
//                startActivity(Connections);

                addDockableFragment(ConnectionsFragment.newInstance());

                break;
            case R.id.Precautions:
//                Intent Precautions = new Intent(MainActivity.this, PrecautionsActivity.class);
//                startActivity(Precautions);

                addDockableFragment(new PrecautionsFragment());
                break;
            case R.id.About:
                Toast.makeText(this, "About not configured", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Feedback:
                Toast.makeText(this, "Feedback not configured", Toast.LENGTH_SHORT).show();
                break;

        }

    }


}