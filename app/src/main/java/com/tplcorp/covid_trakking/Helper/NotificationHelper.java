package com.tplcorp.covid_trakking.Helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MainActivity;

import static android.content.Context.VIBRATOR_SERVICE;

public class NotificationHelper {


    @RequiresApi(Build.VERSION_CODES.O)
    public static Notification startMyOwnForeground(Context context)
    {
        String NOTIFICATION_CHANNEL_ID = "COVID";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.shield)
                .setContentTitle("TPL Covid Tracing is working")
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        return notification;

    }

    private static NotificationManager notifManager;
    public static Notification foregroundNotification(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = ""; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(R.drawable.shield)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
       return notification;
    }


    public static void sendNotification(Context context , String title , String message) {
        String GROUP_KEY_ALERTS = "com.tplcorp.covid_trakking";
        Intent intent;


        intent = new Intent(context.getApplicationContext(), MainActivity.class);

        NotificationClass nc = new NotificationClass();

        int notifyID = 3;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);


        //  intent = new Intent(getApplicationContext(), com.tpltrakker.main.Activities.MainMenuActivities.TrackMe.Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Creating Channel
            nc.createMainNotificationChannel(context.getApplicationContext());
            //building Notification.

            Notification.Builder notifi =
                    new Notification.Builder(context.getApplicationContext(), nc.getMainNotificationId());

            notifi.setSmallIcon(R.drawable.shield);
            notifi.setContentTitle(title);
            notifi.setContentText(message);
            notifi.setAutoCancel(true);
            notifi.setContentIntent(pendingIntent);
            notifi.setGroup(GROUP_KEY_ALERTS);
            notifi.setStyle(new Notification.BigTextStyle()
                    .bigText(message));
            notifi.setSound(alarmSound, AudioAttributes.USAGE_NOTIFICATION);


            //getting notification object from notification builder.
            Notification n = notifi.build();

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


            mNotificationManager.notify(notifyID, n);
            toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 3000);
            ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);

        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //String GROUP_KEY_ALERTS = "com.tpltrakker.main.ALERTS";

                //for devices less than API Level 26
                Notification notification = new Notification.Builder(context.getApplicationContext())

                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.shield)
                        .setAutoCancel(true)
                        .setSound(alarmSound)

                        .setStyle(new Notification.BigTextStyle()
                                .bigText(message))
                        .setContentIntent(pendingIntent).setPriority(Notification.PRIORITY_DEFAULT)
                        .setGroup(GROUP_KEY_ALERTS)
                        .build();

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                mNotificationManager.notify(notifyID, notification);
                toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 3000);
                ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);

            } else {
                // String GROUP_KEY_ALERTS = "com.tpltrakker.main.ALERTS";

                //for devices less than API Level 26
                Notification notification = new Notification.Builder(context.getApplicationContext())


                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.shield)
                        .setAutoCancel(true)
                        .setSound(alarmSound)

                        .setStyle(new Notification.BigTextStyle()
                                .bigText(message))
                        .setContentIntent(pendingIntent).setPriority(Notification.PRIORITY_DEFAULT)
                        //.setGroup(GROUP_KEY_ALERTS)
                        .build();

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                mNotificationManager.notify(notifyID, notification);
                toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 3000);
                ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
            }


        }

    }


}
