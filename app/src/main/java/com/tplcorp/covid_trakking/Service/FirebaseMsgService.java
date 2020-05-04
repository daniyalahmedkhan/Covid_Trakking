package com.tplcorp.covid_trakking.Service;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tplcorp.covid_trakking.Helper.BackgroundServiceHelper;
import com.tplcorp.covid_trakking.Helper.NotificationHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;

import java.util.Map;

public class FirebaseMsgService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        try {

            // To handle backend notification -- determine if there is key is_Silent available then it's from backend.
            if (remoteMessage.getData().get("is_Silent") != null) {
                Map<String, String> data = remoteMessage.getData();

                String content = data.get("content");
                String title = data.get("title");
                String is_Silent = data.get("is_Silent");

            //    startService(new Intent(this, BackgroundService.class));

                if (is_Silent.equals("1")){
                    PrefsHelper.putString(PrefConstants.AFFECTED , "1");
                    BackgroundServiceHelper.uploadDataToServer(PrefsHelper.getString(PrefConstants.MOBILE) , this);
                }else{
                    NotificationHelper.sendNotification(this, title , content);
                }

            } else {

                // To handle firebase console notification

                String msg = remoteMessage.getNotification().getBody();
                String title = remoteMessage.getNotification().getTitle();

                NotificationHelper.sendNotification(this , title, msg);
            }

        } catch (Exception e) {
        }
    }
}
