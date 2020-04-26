package com.tplcorp.covid_trakking.Helper;

import android.content.Context;

import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;

public class DatabaseHelper {

    public  static void insertInDB(Context context , String Mobile , String Affected , String Lat, String Lng) {

        LocationHelper locationHelper = new LocationHelper();
        locationHelper.LocationInitialize(context);


        Double OwnLat = PrefsHelper.getDouble(PrefConstants.LAT , 0);
        Double OwnLng = PrefsHelper.getDouble(PrefConstants.LNG , 0);


        String DIS = "0";

        if (!(Lat.equals("0") || Lng.equals("0") || OwnLat == 0 || OwnLng == 0)){
            DIS  = BluetoothHelper.distanceCalculate(Lat , Lng , OwnLat , OwnLng);
        }

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);
        if (!myDatabase.daoAccess().checkUserData(Mobile, GeneralHelper.todayDate())) {
            TracingData tracingData = new TracingData(Mobile, Affected, Lat, Lng, DIS ,GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate(), "N");
            myDatabase.daoAccess().insertRecord(tracingData);
        }

    }
}
