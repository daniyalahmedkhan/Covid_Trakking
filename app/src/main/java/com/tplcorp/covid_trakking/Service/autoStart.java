package com.tplcorp.covid_trakking.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class autoStart extends BroadcastReceiver {

    public void onReceive(Context context, Intent arg1) {

        Log.d("youwillnevekill" , "ahahha");
        Intent startServiceIntent = new Intent(context, BackgroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startServiceIntent);
        } else {
            //  displayLocationSettingsRequest(TrackMe_Home.this);
            context.startService(startServiceIntent);
        }
    }
}
