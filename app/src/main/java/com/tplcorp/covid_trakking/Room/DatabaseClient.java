package com.tplcorp.covid_trakking.Room;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    static  MyDatabase myDatabase;

    public  static MyDatabase getDatabaseInstance(Context context){

        if (myDatabase == null){

            myDatabase = Room.databaseBuilder(context, MyDatabase.class, "TracingDB")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build();

        }

        return myDatabase;
    }
}
