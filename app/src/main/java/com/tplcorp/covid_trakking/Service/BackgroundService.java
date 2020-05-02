package com.tplcorp.covid_trakking.Service;

import android.app.Notification;
import android.app.ProgressDialog;
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
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import com.tplcorp.covid_trakking.Helper.BackgroundServiceHelper;
import com.tplcorp.covid_trakking.Helper.BluetoothHelper;
import com.tplcorp.covid_trakking.Helper.DatabaseHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.NotificationHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.Interface.ScanningCallback;
import com.tplcorp.covid_trakking.Model.AffectedDataRequest;
import com.tplcorp.covid_trakking.Model.AffectedUser;
import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;
import com.tplcorp.covid_trakking.retrofit.WebServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundService extends Service {

    public static final String ADVERTISING_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";
    public static final String ADVERTISING_FAILED_EXTRA_CODE = "failureCode";
    public static final int ADVERTISING_TIMED_OUT = 6;
    private static final String TAG = BackgroundService.class.getSimpleName();
    public static boolean running = false;
    ScanningCallback scanningCallback;
    List<Connections> connectionsList;
    String Mobile;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private AdvertiseCallback mAdvertiseCallback;
    private Handler mHandler;
    private Runnable timeoutRunnable;
    private ScanCallback scanCallback;
    private BluetoothLeScanner mBluetoothLeScanner;
    /**
     * Length of time to allow advertising before automatically shutting off. (30 seconds)
     */

    private long TIMEOUT = TimeUnit.MILLISECONDS.convert(30000, TimeUnit.MILLISECONDS);

    @Override
    public void onCreate() {

        Notification notification = BackgroundServiceHelper.showForegroundNotification(this);
        startForeground(1, notification);


        connectionsList = new ArrayList<>();
        running = true;
        initialize();
        scanningResult();
        startAdvertising();
        setTimeout();

        registerReceiver(BackgroundServiceHelper.mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        registerReceiver(BackgroundServiceHelper.mBluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

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
        unregisterReceiver(BackgroundServiceHelper.mGpsSwitchStateReceiver);
        unregisterReceiver(BackgroundServiceHelper.mBluetoothStateReceiver);
        startService(new Intent(this, BackgroundService.class));
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

    private void setTimeout() {
        mHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "AdvertiserService has reached timeout of " + TIMEOUT + " milliseconds, stopping advertising.");
                sendFailureIntent(ADVERTISING_TIMED_OUT);
                stopAdvertising();
            }
        };
        mHandler.postDelayed(timeoutRunnable, TIMEOUT);
// u there ? yes 0,0 lat long p map na khule ye b kradena..wait abhi kr dta hn

    }

    private void startAdvertising() {
        Log.d(TAG, "Service: Starting Advertising");
        if (mAdvertiseCallback == null) {
            AdvertiseSettings settings = BluetoothHelper.buildAdvertiseSettings();
            AdvertiseData data = BluetoothHelper.buildAdvertiseData(this);
            mAdvertiseCallback = new SampleAdvertiseCallback();
            if (mBluetoothLeAdvertiser != null) {
                if (!(BluetoothHelper.isBluetooth(BackgroundService.this) && GeneralHelper.checkGPS(this))) {
                    // just show toast
                    Toast.makeText(this, "Please enable your device's Bluetooth / GPS", Toast.LENGTH_LONG).show();
                } else {
                    mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
                    BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(scanCallback);
                }

            }
            connectionsList.clear();


        }
    }

    private void stopAdvertising() {
        Log.d(TAG, "Service: Stopping Advertising");
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            mAdvertiseCallback = null;
            // BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(scanCallback);
        }
        GeneralHelper.showToastLooper("Stopping Advertising", this);


        // ** Again start advertising after 5 mins.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                running = true;
                initialize();
                scanningResult();
                startAdvertising();
                setTimeout();

                //uploadDataToServer("+923432929045");

            }
        }, 10000);

//300000
    }

    private void sendFailureIntent(int errorCode) {
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISING_FAILED);
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
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

            String userData[] = s.split("\\|");
            String Mobile = userData[0] != null ? userData[0] : "";
            String Affected = userData[1] != null ? userData[1] : "";

            String LatData = userData[2] != null ? userData[2] : "";
            String LATLNG[] = LatData.split(",");

            String Lat = LATLNG[0] != null ? LATLNG[0] : "0";
            String Lng = LATLNG[1] != null ? LATLNG[1] : "0";

            Log.d("###", s);
            System.out.println("All keys are: " + s);
            GeneralHelper.showToastLooper(s, this);

            DatabaseHelper.insertInDB(this, Mobile, Affected, Lat, Lng);


            // show notification to user if mobile no is not added in the list in current scan
            if (Affected.equals("1") && !isUserExist(Mobile) && PrefsHelper.getBoolean(PrefConstants.Notifications)) {
                NotificationHelper.sendNotification(BackgroundService.this, "TPL Contact Tracing Alert", "Someone found positive nearby");
                DatabaseHelper.insertNotificationDB(this, "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
            }


            //  add user in the list if not exist in current scan
            if (!isUserExist(Mobile)) {
                connectionsList.add(new Connections(Mobile, PrefsHelper.getString(PrefConstants.TEMP_DISTANCE, "0"), Affected, Lat, Lng, result.getTimestampNanos()));
            }
            GeneralHelper.sendMessageToActivity(this, connectionsList);


        } catch (Exception e) {


            Log.d("error", "" + e.getMessage());
            e.printStackTrace();
            GeneralHelper.showToastLooper("error while parsing", this);
        }

    }

    private boolean isUserExist(String Mobile) {
        for (int i = 0; i < connectionsList.size(); i++) {
            if (connectionsList.get(i).getName().equals(Mobile)) {
                return true;
            }
        }
        return false;
    }

    private class SampleAdvertiseCallback extends AdvertiseCallback {
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d(TAG, "Advertising failed");
            sendFailureIntent(errorCode);
            // stopSelf();
            GeneralHelper.showToastLooper("Advertising failed", BackgroundService.this);
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG, "Advertising successfully started");
            GeneralHelper.showToastLooper("Advertising successfully started", BackgroundService.this);
        }
    }

}