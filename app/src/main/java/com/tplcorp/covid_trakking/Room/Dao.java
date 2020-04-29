package com.tplcorp.covid_trakking.Room;

import androidx.room.Insert;
import androidx.room.Query;

import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;

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

    @Query("Delete from CovidAffected")
    void deleteCovidAffects();
}
