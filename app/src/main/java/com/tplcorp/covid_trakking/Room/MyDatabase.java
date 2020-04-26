package com.tplcorp.covid_trakking.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tplcorp.covid_trakking.Room.Tables.TracingData;

@Database(entities = {TracingData.class} , version = 1 , exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract Dao daoAccess();
}
