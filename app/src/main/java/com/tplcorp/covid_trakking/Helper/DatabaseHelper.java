package com.tplcorp.covid_trakking.Helper;

import android.content.Context;

import com.tplcorp.covid_trakking.Model.AffectedUser;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper {

    public  static void insertInDB(Context context , String Mobile , String Affected , String Lat, String Lng) {

        LocationHelper locationHelper = new LocationHelper();
        locationHelper.LocationInitialize(context);


        Double OwnLat = PrefsHelper.getDouble(PrefConstants.LAT , 0);
        Double OwnLng = PrefsHelper.getDouble(PrefConstants.LNG , 0);


        String DIS = "-";

        if (!(Lat.equals("0") || Lng.equals("0") || OwnLat == 0 || OwnLng == 0 || Lat.equals("0.0") || Lng.equals("0.0"))) {
            DIS  = BluetoothHelper.distanceCalculate(Lat , Lng , OwnLat , OwnLng);
            PrefsHelper.putString(PrefConstants.TEMP_DISTANCE , DIS);
        }

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);
        if (!myDatabase.daoAccess().checkUserData(Mobile, GeneralHelper.todayDate())) {
            TracingData tracingData = new TracingData(Mobile, Affected, Lat, Lng, DIS ,GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate(), "N");
            myDatabase.daoAccess().insertRecord(tracingData);
        }else{
            myDatabase.daoAccess().updateData(GeneralHelper.todayDate_Long() , Mobile , Lat , Lng , GeneralHelper.todayDate() , Affected);
        }

        // delete data older than 28 days
        myDatabase.daoAccess().deleteData(GeneralHelper.dateToDelete(28));

    }

    public static void insertNotificationDB(Context context , String text , String affected , Date date , String date_ , int isActive){

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);
        Notifications notifications = new Notifications(text , affected , date , date_ , isActive);
        myDatabase.daoAccess().insertNotification(notifications);

    }


    public static List<AffectedUser> getAffectedUsersFromDB(Context context) {

        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);

        List<AffectedUser> arrData = new ArrayList<>();
        myDatabase = DatabaseClient.getDatabaseInstance(context);
        List<TracingData> affectedList = myDatabase.daoAccess().getTracingData();

     //   List<TracingData> affectedList = new ArrayList<>();
     //   affectedList.add(new TracingData("+923432929045" , "0" , "0.25" , "0.25" , "25" , GeneralHelper.todayDate_DATE() , GeneralHelper.todayDate() , "N"));

        if (affectedList.size() > 0) {

            for (TracingData model : affectedList) {

                AffectedUser entity = new AffectedUser();
                entity.setPhoneNumber(model.getUSER_MOBILE());
                entity.setInteractionTime(String.valueOf(model.getTIME_STAMP()));
                entity.setIsAffected(Integer.valueOf(model.getIS_AFFECTED()));
                entity.setDistance(model.getDISTANCE());
                arrData.add(entity);

            }

        }

        return arrData;
    }


    public static void updateUploadData(Context context){
        MyDatabase myDatabase = DatabaseClient.getDatabaseInstance(context);
        myDatabase.daoAccess().updateUploadList();
    }
}
