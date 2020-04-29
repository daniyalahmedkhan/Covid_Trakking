package com.tplcorp.covid_trakking.Helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by ahsan.mohammad on 11/9/2018.
 */

public class NotificationClass {
    private String CHANNEL_ID = "1";
    private String CHANNEL_NAME = "TPL Covid-19 Tracing";
    private String CHANNEL_DESCRIPTION = "TPL App Notifications..";

    @RequiresApi(Build.VERSION_CODES.O)
    public void createMainNotificationChannel(Context c) {
        String id = CHANNEL_ID;
        String name = CHANNEL_NAME;
        String description = CHANNEL_DESCRIPTION;
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription (description);
        mChannel.enableLights(true);
        mChannel.setLightColor( Color.RED);
        mChannel.enableVibration(true);
        NotificationManager mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        // NotificationManager mNotificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        mNotificationManager.createNotificationChannel(mChannel);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public String getMainNotificationId() {
        return CHANNEL_ID;
    }

}
