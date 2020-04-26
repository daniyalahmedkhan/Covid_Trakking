package com.tplcorp.covid_trakking.UI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;

import com.tplcorp.covid_trakking.Helper.BluetoothHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.LocationHelper;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;
import com.tplcorp.covid_trakking.Service.BackgroundService;


public class MainActivity extends AppCompatActivity {


    ArrayList<ScanFilter> filters = new ArrayList();
    ScanFilter filter;
    ScanSettings settings;
    BluetoothLeAdvertiser advertiser;
    AdvertiseCallback advertisingCallback;
    private ScanCallback scanCallback;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BluetoothHelper.isBluetooth(this)) {
            BluetoothHelper.openBluetoothSettings(this, this);
            GeneralHelper.showToast(this, "Please open bluetooth setting");
        } else {

            if (GeneralHelper.locationPermission(this)) {
                if (GeneralHelper.turnGPSOn(this, this)) {

//                    advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
//
//                    scanningResult();
//                    advertiseData();
//
//                    mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
//                    BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(scanCallback);

                    startService(new Intent(this, BackgroundService.class));



                }
            }
        }
    }

    private void advertiseData() {

        String userPhone = PrefsHelper.getString("MOBILE");
        String isAffected = PrefsHelper.getString("AFFECTED" , "0");
        String mServiceData = userPhone+"-"+isAffected;

        mHandler = new Handler();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(false)
                .setTimeout(0)
                .build();

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63"));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)

                //.//addManufacturerData()
                .setIncludeTxPowerLevel(false)

                //.addServiceUuid(pUuid)
                .addServiceData(pUuid, mServiceData.getBytes())
                .build();


        advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.e("BLE", "Advertising onStart: ");

            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, data, advertisingCallback);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(scanCallback);
                advertiser.stopAdvertising(advertisingCallback);
            }
        }, 12000);

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
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.d("error", "" + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}