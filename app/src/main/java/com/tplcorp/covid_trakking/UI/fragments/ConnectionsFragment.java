package com.tplcorp.covid_trakking.UI.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.tplcorp.covid_trakking.Adapter.ConnectionAdapter;
import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConnectionsFragment extends BaseFragment {

    
    List<Connections> connectionsList;
    ConnectionAdapter adapter;
    
    @BindView(R.id.loading)
    LottieAnimationView loading;
    @BindView(R.id.RV_connection)
    RecyclerView RVConnection;



    public static ConnectionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ConnectionsFragment fragment = new ConnectionsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public int getFragmentLayout() {
        return R.layout.activity_connections;
    }

    @Override
    public String getTitleBarName() {
        return "Active Connections";
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectionsList = new ArrayList<>();
        //connectionsList.add(new Connections("0" , "25" , "1" , "24.832223" , "67.076565" , 0));

        adapter = new ConnectionAdapter(connectionsList, getActivity());
        setUpConnectionList();
    }

    private void setUpConnectionList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RVConnection.setLayoutManager(mLayoutManager);
        RVConnection.setItemAnimator(new DefaultItemAnimator());
        RVConnection.setAdapter(adapter);

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
            List<Connections> connections = (List<Connections>) bundle.getSerializable("ConnectionList");
            for (int i = 0; i < connections.size(); i++) {
                connectionsList.add(new Connections(connections.get(i).getName(), connections.get(i).getDistance(), connections.get(i).getAffected(), connections.get(i).getLat(), connections.get(i).getLng(), connections.get(i).getTimeStamp()));
            }
            adapter.notifyDataSetChanged();
        }
    };





    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("Connections"));
    }


}
