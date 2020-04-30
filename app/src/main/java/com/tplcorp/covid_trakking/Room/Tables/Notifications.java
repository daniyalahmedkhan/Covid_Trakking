package com.tplcorp.covid_trakking.Room.Tables;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tplcorp.covid_trakking.Helper.TimeStamp_Converter;

import java.util.Date;

@Entity
public class Notifications {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String IS_AFFECTED;
    @TypeConverters({TimeStamp_Converter.class})
    private Date TIME_STAMP;
    private String DATE_;


    public Notifications(String IS_AFFECTED, Date TIME_STAMP, String DATE_) {
        this.IS_AFFECTED = IS_AFFECTED;
        this.TIME_STAMP = TIME_STAMP;
        this.DATE_ = DATE_;
    }

    public String getIS_AFFECTED() {
        return IS_AFFECTED;
    }

    public void setIS_AFFECTED(String IS_AFFECTED) {
        this.IS_AFFECTED = IS_AFFECTED;
    }

    public Date getTIME_STAMP() {
        return TIME_STAMP;
    }

    public void setTIME_STAMP(Date TIME_STAMP) {
        this.TIME_STAMP = TIME_STAMP;
    }

    public String getDATE_() {
        return DATE_;
    }

    public void setDATE_(String DATE_) {
        this.DATE_ = DATE_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
