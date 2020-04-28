package com.tplcorp.covid_trakking.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContextWrapper;
import android.os.Build;

import com.tplcorp.covid_trakking.Helper.PrefsHelper;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();


        createNotificationChannel();

        // Initialize the Prefs class
        new PrefsHelper.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
// suno... i am calling u om skype

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "CHANNEL_ID",
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
