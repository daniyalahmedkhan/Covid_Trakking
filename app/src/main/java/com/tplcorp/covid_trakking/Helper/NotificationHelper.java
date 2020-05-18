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
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Service.NotificationDismissedReceiver;
import com.tplcorp.covid_trakking.UI.MainActivity;

import static android.content.Context.VIBRATOR_SERVICE;

public class NotificationHelper {


    public static int notifyID = 1;

    public static void sendNotification(Context context, String title, String message) {
        String GROUP_KEY_ALERTS = "com.tplcorp.covid_trakking";
        Intent intent;


        intent = new Intent(context.getApplicationContext(), MainActivity.class);

        NotificationClass nc = new NotificationClass();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        //  ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);


        //  intent = new Intent(getApplicationContext(), com.tpltrakker.main.Activities.MainMenuActivities.TrackMe.Notification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //getting notification object from notification builder.
        Notification n;

        NotificationManager mNotificationManager;


        notifyID++;
        Log.d("FIRSTIME", String.valueOf(notifyID));

        if (notifyID == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Creating Channel
                nc.createMainNotificationChannel(context.getApplicationContext());
                //building Notification.

                Notification.Builder notifi = new Notification.Builder(context.getApplicationContext(), nc.getMainNotificationId());

                notifi.setSmallIcon(R.drawable.shield);
                notifi.setContentTitle(title);
                notifi.setContentText(message);
                notifi.setAutoCancel(true);
                notifi.setContentIntent(pendingIntent);
                notifi.setGroupSummary(true);
                notifi.setGroup(GROUP_KEY_ALERTS);
                notifi.setStyle(new Notification.BigTextStyle().bigText(message));
                notifi.setDeleteIntent(createOnDismissedIntent(context, notifyID));
                notifi.setNumber(notifyID);
                notifi.setSound(alarmSound, AudioAttributes.USAGE_NOTIFICATION);


                //getting notification object from notification builder.
                n = notifi.build();

                mNotificationManager =
                        (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


                mNotificationManager.notify(notifyID, n);
                // toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
                //    ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);

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
                            .setGroupSummary(true)
                            .setGroup(GROUP_KEY_ALERTS)
                            .build();

                    mNotificationManager =
                            (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                    mNotificationManager.notify(notifyID, notification);
                    /// toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
                    //  ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);

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
                            .setGroupSummary(true)
                            .setGroup(GROUP_KEY_ALERTS)
                            .build();

                    mNotificationManager =
                            (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                    mNotificationManager.notify(notifyID, notification);
                    // toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
                    //  ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
                }


            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //Creating Channel
                //nc.createMainNotificationChannel(context.getApplicationContext());
                //building Notification.

                Notification.Builder notifi = new Notification.Builder(context.getApplicationContext(), nc.getMainNotificationId());

                notifi.setSmallIcon(R.drawable.shield);
                notifi.setContentTitle(title);
                notifi.setContentText(message);
                notifi.setContentIntent(pendingIntent);
                notifi.setGroup(GROUP_KEY_ALERTS);
                notifi.setSound(alarmSound, AudioAttributes.USAGE_NOTIFICATION);


                n = notifi.build();

                mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(notifyID, n);

                //   NotificationManagerCompat.from(context).notify(notifyID,notifi);


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

                    mNotificationManager =
                            (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                    mNotificationManager.notify(notifyID, notification);
                    /// toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
                    //  ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);

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
                            .setGroup(GROUP_KEY_ALERTS)
                            .build();

                    mNotificationManager =
                            (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Issue the notification.
                    mNotificationManager.notify(notifyID, notification);
                    // toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
                    //  ((Vibrator)context.getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
                }


            }
        }


    }


    private static PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.my.app.notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }


}
