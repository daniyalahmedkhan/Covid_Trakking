package com.tplcorp.covid_trakking.Room;

import androidx.room.Insert;
import androidx.room.Query;

import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;
import com.tplcorp.covid_trakking.UI.fragments.Notification;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    // ******** TracingData *********** //

    @Query("SELECT * FROM TracingData WHERE USER_MOBILE = :USERMOBILE and DATE_ = :TODAYDATE")
    boolean checkUserData(String USERMOBILE , String TODAYDATE);

    @Insert
    void insertRecord(TracingData tracingData);


    // ******** CovidAffected *********** //

    @Insert
    void insertAffectedRecord(CovidAffected covidAffected);

    @Query("SELECT * FROM CovidAffected")
    List<CovidAffected> affectedList();

    @Query("SELECT * FROM TracingData")
    List<TracingData> getTracingData();

    @Query("Delete from CovidAffected")
    void deleteCovidAffects();


    @Query("Delete from TracingData")
    void deleteTracingData();


    // ******** Notifications *********** //

    @Insert
    void insertNotification(Notifications notifications);

    @Query("SELECT * FROM Notifications")
    List<Notifications> getNotificationList();
}
