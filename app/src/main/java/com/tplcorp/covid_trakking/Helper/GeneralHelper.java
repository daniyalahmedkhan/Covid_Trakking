package com.tplcorp.covid_trakking.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Service.BackgroundService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GeneralHelper {

   public static boolean isPermissionGranted = false;


    public static void showToast(Context context , String msg){
        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLooper(final String msg , final Context context){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context .getApplicationContext(), msg ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean locationPermission(final Context context){

        Dexter.withContext(context).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                if (multiplePermissionsReport.areAllPermissionsGranted()){
                    isPermissionGranted = true;
                }else {
                    isPermissionGranted = false;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
            }
        }).check();

        return isPermissionGranted;
    }


    public static boolean buildAlertMessageNoGps(final Context context) {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }else{
            return true;
        }
        return false;
    }

    public static  boolean turnGPSOn(final Context context , final Activity activity)
    {
         SettingsClient mSettingsClient;
         LocationSettingsRequest mLocationSettingsRequest;
         final int REQUEST_CHECK_SETTINGS = 214;
         final int REQUEST_ENABLE_GPS = 516;


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(context);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS","Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS","checkLocationSettings -> onCanceled");
                    }
                });

        return true;
    }

    public static String todayDate(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static Date todayDate_DATE(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = df.format(c);

        return c;
    }

}
