package com.tplcorp.covid_trakking.Helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Service.BackgroundService;
import com.tplcorp.covid_trakking.UI.MainActivity;

public class BackgroundServiceHelper {

    public static Notification showForegroundNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setContentTitle("TPL Covid-19 is running in background")
                .setContentText("")
                .setSmallIcon(R.drawable.shield)
                .setContentIntent(pendingIntent)
                .build();

        return notification;

    }

    public static BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                if (!GeneralHelper.checkGPS(context)) {
                    NotificationHelper.sendNotification(context, "Turn on GPS", "Please turn on the GPS for COVID-19 tracing");
                }
            }
        }
    };

    public static BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        //NotificationHelper.sendNotification(BackgroundService.this , "Turn on GPS" , "Please turn on the GPS for COVID-19 tracing");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        NotificationHelper.sendNotification(context , "Turn on Bluetooth" , "Please turn on the Bluetooth for COVID-19 tracing");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

}
