package com.tplcorp.covid_trakking.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.internal.Constants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MainActivity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();
    public static boolean running = false;
    public static final String ADVERTISING_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";
    public static final String ADVERTISING_FAILED_EXTRA_CODE = "failureCode";
    public static final int ADVERTISING_TIMED_OUT = 6;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private AdvertiseCallback mAdvertiseCallback;
    private Handler mHandler;
    private Runnable timeoutRunnable;
    private ScanCallback scanCallback;
    private BluetoothLeScanner mBluetoothLeScanner;

    /**
     * Length of time to allow advertising before automatically shutting off. (10 minutes)
     */

    private long TIMEOUT = TimeUnit.MILLISECONDS.convert(10000, TimeUnit.MILLISECONDS);

    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            startMyOwnForeground();
        }else{
            startForeground(1, new Notification());
        }


        running = true;
        initialize();
        scanningResult();
        startAdvertising();

        setTimeout();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //startService(new Intent(this, BackgroundService.class));

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void initialize() {
        if (mBluetoothLeAdvertiser == null) {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager != null) {
                BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
                if (mBluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                } else {
                    //Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setTimeout(){
        mHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "AdvertiserService has reached timeout of "+TIMEOUT+" milliseconds, stopping advertising.");
                sendFailureIntent(ADVERTISING_TIMED_OUT);
              //  stopSelf();
                stopAdvertising();
            }
        };
        mHandler.postDelayed(timeoutRunnable, TIMEOUT);



    }


    /**
     * Starts BLE Advertising.
     */
    private void startAdvertising() {
        Log.d(TAG, "Service: Starting Advertising");
        if (mAdvertiseCallback == null) {
            AdvertiseSettings settings = buildAdvertiseSettings();
            AdvertiseData data = buildAdvertiseData();
            mAdvertiseCallback = new SampleAdvertiseCallback();
            if (mBluetoothLeAdvertiser != null) {
                mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
            }
           // mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(scanCallback);
        }
    }


    /**
     * Stops BLE Advertising.
     */
    private void stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising");
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mAdvertiseCallback = null;
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(scanCallback);
        }
        showToast("Stopping Advertising");


                new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                running = true;
                initialize();
                scanningResult();
                startAdvertising();
                setTimeout();


            }
        }, 10000);


    }


    /**
     * Returns an AdvertiseData object which includes the Service UUID and Device Name.
     */
    private AdvertiseData buildAdvertiseData() {

        String userPhone = PrefsHelper.getString("MOBILE");
        String isAffected = PrefsHelper.getString("AFFECTED" , "0");
        String mServiceData = userPhone+"-"+isAffected;

       // ParcelUuid pUuid = new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63"));

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString("00001234-0000-1000-8000-00805F9B34FB"));


        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        //dataBuilder.addServiceUuid(pUuid);

        dataBuilder.addServiceData(pUuid, "03452081685|0|24.322,64.555".getBytes());
        dataBuilder.setIncludeDeviceName(false);

         dataBuilder.setIncludeTxPowerLevel(false);

        return dataBuilder.build();
    }

    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private AdvertiseSettings buildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        settingsBuilder.setConnectable(false);
        settingsBuilder.setTimeout(0);
        return settingsBuilder.build();
    }
    /**
     * Custom callback after Advertising succeeds or fails to start. Broadcasts the error code
     * in an Intent to be picked up by AdvertiserFragment and stops this Service.
     */
    private class SampleAdvertiseCallback extends AdvertiseCallback {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d(TAG, "Advertising failed");
            sendFailureIntent(errorCode);
           // stopSelf();
            showToast("Advertising failed");
        }
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "Advertising successfully started");
            showToast("Advertising successfully started");
        }
    }
    /**
     * Builds and sends a broadcast intent indicating Advertising has failed. Includes the error
     * code as an extra. This is intended to be picked up by the {@code AdvertiserFragment}.
     */
    private void sendFailureIntent(int errorCode){
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISING_FAILED);
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    private void scanningResult() {

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                //  ScanRecord.parseFromBytes(result.getScanRecord().getBytes());
                Log.d("##", "onScanResult() - " + result.toString());

                printScanResult(result);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                //textView.append("Received " + results.size() + " batch results:\n");
                for (ScanResult r : results) {
                    printScanResult(r);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                switch (errorCode) {
                    case ScanCallback.SCAN_FAILED_ALREADY_STARTED:
                        Log.d("##", "Scan failed: already started.\n");
                        break;
                    case ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                        Log.d("##", "Scan failed: app registration failed.\n");

                        //  textView.append("Scan failed: app registration failed.\n");
                        break;
                    case ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED:
                        Log.d("##", "Scan failed: feature unsupported.\n");

                        //  textView.append("Scan failed: feature unsupported.\n");
                        break;
                    case ScanCallback.SCAN_FAILED_INTERNAL_ERROR:
                        Log.d("##", "Scan failed: internal error.\n");

                        //   textView.append("Scan failed: internal error.\n");
                        break;
                }
            }

        };
    }

    private void printScanResult(ScanResult result) {

        Map<ParcelUuid, byte[]> data = result.getScanRecord().getServiceData();

        try {
            String s = new String(data.entrySet().iterator().next().getValue(), "UTF-8");


            Log.d("###", s);
            System.out.println("All keys are: " + s);
            showToast(s);


        } catch (Exception e) {
            Log.d("error", "" + e.getMessage());
            e.printStackTrace();
            showToast("error aya");
        }

    }


    private void showToast(final String msg){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(BackgroundService.this.getApplicationContext(), msg ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}