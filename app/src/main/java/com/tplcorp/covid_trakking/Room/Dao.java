package com.tplcorp.covid_trakking.Room;

import androidx.room.Insert;
import androidx.room.Query;

import com.tplcorp.covid_trakking.Room.Tables.TracingData;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT * FROM TracingData WHERE USER_MOBILE = :USERMOBILE and DATE_ = :TODAYDATE")
    boolean checkUserData(String USERMOBILE , String TODAYDATE);


    @Insert
    void insertRecord(TracingData tracingData);

}
