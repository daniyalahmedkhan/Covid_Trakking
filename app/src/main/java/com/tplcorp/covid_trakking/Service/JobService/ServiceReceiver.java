package com.tplcorp.covid_trakking.Service.JobService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("JOBSERVICE" , "Recevier");
        Util.scheduleJob(context);
    }
}
