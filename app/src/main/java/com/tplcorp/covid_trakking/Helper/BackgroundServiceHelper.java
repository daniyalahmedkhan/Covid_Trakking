package com.tplcorp.covid_trakking.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tplcorp.covid_trakking.Model.AffectedDataRequest;
import com.tplcorp.covid_trakking.Model.AffectedUser;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Service.BackgroundService;
import com.tplcorp.covid_trakking.UI.MainActivity;
import com.tplcorp.covid_trakking.retrofit.WebServiceFactory;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundServiceHelper {

    public static Notification showForegroundNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setContentTitle("TPL Contact Tracing Enabled")
                .setContentText("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
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
                        //BluetoothAdapter.getDefaultAdapter().enable();
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


    public static void uploadDataToServer(String mobileNumber , Context context) {

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);
        List<AffectedUser> list = DatabaseHelper.getAffectedUsersFromDB(context);

        Log.d("listKaSize" , String.valueOf(list.size()));

        if (GeneralHelper.isNetworkAvailable(context)){
            AffectedDataRequest model = new AffectedDataRequest();
            model.setUserPhoneNumber(mobileNumber);
            model.setData(list);

            WebServiceFactory.getInstance().postInteractionsData(model).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                    if (response.body() != null && response.body().get("RespMsg").toString().equalsIgnoreCase("Success")) {

                        // Toast.makeText(this, "Data Uploaded successfully", Toast.LENGTH_SHORT).show();
                        // myDatabase.daoAccess().deleteTracingData();
                        Log.d("DATADATA", "Date uploaded");
                        DatabaseHelper.updateUploadData(context);

                    } else {

                        // Toast.makeText(getActivity(), response.body().get("RespMsg").toString(), Toast.LENGTH_SHORT).show();
                        Log.d("DATADATA", "Data failed");
                    }
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    t.printStackTrace();
                    Log.d("DATADATA", "Data failed Failure" + t);

                }
            });
        }

    }

}
