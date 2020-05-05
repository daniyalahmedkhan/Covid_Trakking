package com.tplcorp.covid_trakking.Room;

import androidx.room.Insert;
import androidx.room.Query;

import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Room.Tables.Notifications;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;
import com.tplcorp.covid_trakking.UI.fragments.Notification;

import java.util.Date;
import java.util.List;

@androidx.room.Dao
public interface Dao {

    // ******** TracingData *********** //

    @Query("SELECT * FROM TracingData WHERE USER_MOBILE = :USERMOBILE and DATE_ = :TODAYDATE")
    boolean checkUserData(String USERMOBILE , String TODAYDATE);

    @Insert
    void insertRecord(TracingData tracingData);

    @Query("UPDATE TracingData set TIME_STAMP = :date , DATE_ = :date2 , IS_AFFECTED = :affected , LAT  = :lat , LNG = :lng where USER_MOBILE = :USERMOBILE")
    void updateData(long date , String USERMOBILE , String lat , String lng , String date2 , String affected);


    @Query("DELETE FROM TracingData WHERE TIME_STAMP < :date")
    void deleteData(long date);

    @Query("SELECT * FROM TracingData WHERE TIME_STAMP < :date")
    List<TracingData> testData(long date);

    // ******** CovidAffected *********** //

    @Insert
    void insertAffectedRecord(CovidAffected covidAffected);

    @Query("SELECT * FROM CovidAffected")
    List<CovidAffected> affectedList();

    @Query("SELECT * FROM TracingData where IS_UPLOADED == 'N'")
    List<TracingData> getTracingData();

    @Query("UPDATE TracingData SET IS_UPLOADED = 'Y'")
    void updateUploadList();

    @Query("Delete from CovidAffected")
    void deleteCovidAffects();


    @Query("Delete from TracingData")
    void deleteTracingData();


    // ******** Notifications *********** //

    @Insert
    void insertNotification(Notifications notifications);

    @Query("SELECT * FROM Notifications order by TIME_STAMP desc")
    List<Notifications> getNotificationList();

    @Query("update Notifications set IS_ACTIVE = 0")
    void updateNotification();


    @Query("SELECT * FROM Notifications where IS_ACTIVE == 1 order by TIME_STAMP desc ")
    List<Notifications> getActiveNotification();

    @Query("DELETE from notifications where TIME_STAMP < :date")
    void deleteOlderNotification(long date);
}
