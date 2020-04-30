package com.tplcorp.covid_trakking.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.fragments.ConnectionsFragment;
import com.tplcorp.covid_trakking.UI.fragments.Feedback;
import com.tplcorp.covid_trakking.UI.fragments.HomeFragment;
import com.tplcorp.covid_trakking.UI.fragments.Notification;
import com.tplcorp.covid_trakking.UI.fragments.PrecautionsFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
           getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

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
                initToolbar("Feedback" , false);
                addDockableFragment(new Feedback());
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
            case R.id.notification:
                addDockableFragment(Notification.newInstance());
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}