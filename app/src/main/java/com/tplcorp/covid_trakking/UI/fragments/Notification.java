package com.tplcorp.covid_trakking.UI.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tplcorp.covid_trakking.Adapter.NotificationAdapter;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;

import java.util.List;

import butterknife.BindView;

public class Notification extends BaseFragment {

    @BindView(R.id.RV_notification)
    RecyclerView RV_notification;
    @BindView(R.id.Notification)
    LinearLayout textNotification;

    NotificationAdapter adapter;


    public static Notification newInstance() {

        Bundle args = new Bundle();

        Notification fragment = new Notification();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotifications();
    }


    private void getNotifications() {

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(getActivity());
        List<Notifications> notificationsList = myDatabase.daoAccess().getNotificationList();
        if (notificationsList.size() == 0) {
            textNotification.setVisibility(View.VISIBLE);
            RV_notification.setVisibility(View.GONE);
        } else {
            textNotification.setVisibility(View.GONE);
            RV_notification.setVisibility(View.VISIBLE);
            adapter = new NotificationAdapter(getActivity(), notificationsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            RV_notification.setLayoutManager(mLayoutManager);
            RV_notification.setItemAnimator(new DefaultItemAnimator());
            RV_notification.setAdapter(adapter);
            myDatabase.daoAccess().updateNotification();
        }
    }

   // private void updateNotification()


    @Override
    public int getFragmentLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public String getTitleBarName() {
        return "Notifications";
    }

    @Override
    public boolean isBackButton() {
        return true;
    }
}
