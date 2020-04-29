package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.tplcorp.covid_trakking.Adapter.ConnectionAdapter;
import com.tplcorp.covid_trakking.Interface.ScanningCallback;
import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsActivity extends AppCompatActivity {

    RecyclerView RV_connection;
    List<Connections> connectionsList;
    ConnectionAdapter adapter;
    ImageView IV_manu;
    TextView TV_home;

    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);


        IV_manu = findViewById(R.id.IV_manu);
        TV_home = findViewById(R.id.TV_home);
        loading = findViewById(R.id.loading);

        IV_manu.setVisibility(View.GONE);
        TV_home.setVisibility(View.GONE);
        initToolbar("Active Connections");

        RV_connection = findViewById(R.id.RV_connection);
        connectionsList = new ArrayList<>();


        //connectionsList.add(new Connections("0" , "25" , "1" , "24.832223" , "67.076565" , 0));

        adapter = new ConnectionAdapter(connectionsList , this);
        setUpConnectionList();
    }


    private void setUpConnectionList(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RV_connection.setLayoutManager(mLayoutManager);
        RV_connection.setItemAnimator(new DefaultItemAnimator());
        RV_connection.setAdapter(adapter);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            loading.setVisibility(View.VISIBLE);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                }
            }, 5000);


            connectionsList.clear();
            Bundle bundle = intent.getExtras();
            List<Connections> connections = (List<Connections>)bundle.getSerializable("ConnectionList");
            for (int i=0; i<connections.size(); i++){
                connectionsList.add(new Connections(connections.get(i).getName() , connections.get(i).getDistance(), connections.get(i).getAffected() , connections.get(i).getLat() , connections.get(i).getLng() , connections.get(i).getTimeStamp()));
            }
            adapter.notifyDataSetChanged();
        }
    };

    public void initToolbar(String title){
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);

        if(title != null)
            toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("Connections"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mMessageReceiver);
    }
}
