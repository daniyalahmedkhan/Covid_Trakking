package com.tplcorp.covid_trakking.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tplcorp.covid_trakking.Helper.NotificationHelper;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.notifyID = 1;
    }
}
