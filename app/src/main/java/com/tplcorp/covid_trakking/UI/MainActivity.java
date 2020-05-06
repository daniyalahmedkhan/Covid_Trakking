package com.tplcorp.covid_trakking.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tplcorp.covid_trakking.Interface.BottomNavReselect;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;
import com.tplcorp.covid_trakking.UI.fragments.CasesFragment;
import com.tplcorp.covid_trakking.UI.fragments.ConnectionsFragment;
import com.tplcorp.covid_trakking.UI.fragments.Feedback;
import com.tplcorp.covid_trakking.UI.fragments.HomeFragment;
import com.tplcorp.covid_trakking.UI.fragments.Notification;
import com.tplcorp.covid_trakking.UI.fragments.PrecautionsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceFragmentCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import re.robz.bottomnavigation.circularcolorreveal.BottomNavigationCircularColorReveal;


public class MainActivity extends BaseActivity implements BottomNavReselect {

    boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    TextView TV_notification;

    TextView textNotification;
    private int mMenuId;


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
        TV_notification = toolbar.findViewById(R.id.TV_notification);


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
                addDockableFragment(new CasesFragment());
                break;
            case R.id.Feedback:
                //initToolbar("Feedback" , false);
                addDockableFragment(new Feedback());
                break;

        }

    }

    public void getActiveNotification() {
        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(this);
        List<Notifications> list = myDatabase.daoAccess().getActiveNotification();
        if (list.size() <= 0) {
            textNotification.setVisibility(View.GONE);
        } else {
            textNotification.setVisibility(View.VISIBLE);
            textNotification.setText(String.valueOf(list.size()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.notification);

        View actionView = menuItem.getActionView();
        textNotification = (TextView) actionView.findViewById(R.id.cart_badge);

        getActiveNotification();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.notification:
                addDockableFragment(Notification.newInstance());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActiveNotification();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("HOME"));
    }

    @Override
    public void SetNavState(int id) {
        bottomNavigation.getMenu().findItem(id).setChecked(true);
    }
}