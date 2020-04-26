package com.tplcorp.covid_trakking.Helper;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.ParcelUuid;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.UUID;

public class BluetoothHelper {

    public static boolean isBluetooth(Context context){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            return false;
        } else if (!mBluetoothAdapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    public static void openBluetoothSettings(Context context , Activity activity){
//        Intent intentBluetooth = new Intent();
//        intentBluetooth.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
//        context.startActivity(intentBluetooth);

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);
    }



    public static  boolean hasBlePermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static  void requestBlePermissions(final Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    public static  boolean checkGrantResults(String[] permissions, int[] grantResults) {
        int granted = 0;

        if (grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) || permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        granted++;
                    }
                }
            }
        } else { // if cancelled
            return false;
        }

        return granted == 2;
    }

    public static boolean areLocationServicesEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    public static AdvertiseSettings buildAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        settingsBuilder.setConnectable(false);
        settingsBuilder.setTimeout(0);
        return settingsBuilder.build();
    }



    /**
     * Returns an AdvertiseData object which includes the Service UUID and Device Name.
     */
    public static AdvertiseData buildAdvertiseData(Context context) {

        LocationHelper locationHelper = new LocationHelper();
        locationHelper.LocationInitialize(context);

        String userPhone = PrefsHelper.getString(PrefConstants.MOBILE);
        userPhone = userPhone.replace("+92" ,"0");
        String isAffected = PrefsHelper.getString(PrefConstants.AFFECTED, "0");
        String lat = String.valueOf(PrefsHelper.getDouble(PrefConstants.LAT , 0));
        String lng = String.valueOf(PrefsHelper.getDouble(PrefConstants.LNG , 0));

        String mServiceData = userPhone + "|" + isAffected+"|"+lat+","+lng;

        //03452081685|0|24.322,64.555

        // ParcelUuid pUuid = new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63"));

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString("00001234-0000-1000-8000-00805F9B34FB"));


        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        //dataBuilder.addServiceUuid(pUuid);

        dataBuilder.addServiceData(pUuid, mServiceData.getBytes());
        dataBuilder.setIncludeDeviceName(false);

        dataBuilder.setIncludeTxPowerLevel(false);

        return dataBuilder.build();
    }

    public static String distanceCalculate(String lat , String lng , Double lat2 , Double lon2){

        Double lat1  = Double.parseDouble(lat);
        Double lon1  = Double.parseDouble(lng);

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return String.valueOf((String.format("%.3f",dist)));
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
