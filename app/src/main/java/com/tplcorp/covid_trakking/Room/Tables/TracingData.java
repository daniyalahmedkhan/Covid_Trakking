package com.tplcorp.covid_trakking.Room.Tables;


import android.print.PrinterId;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tplcorp.covid_trakking.Helper.TimeStamp_Converter;

import java.util.Date;

@Entity
public class TracingData {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String USER_MOBILE;
    private String IS_AFFECTED;
    private String LAT;
    private String LNG;
    private String DISTANCE;
    @TypeConverters({TimeStamp_Converter.class})
    private Date  TIME_STAMP;
    private String DATE_;
    private String IS_UPLOADED;


    public TracingData(String USER_MOBILE, String IS_AFFECTED, String LAT, String LNG, String DISTANCE, Date TIME_STAMP, String DATE_, String IS_UPLOADED) {
        this.USER_MOBILE = USER_MOBILE;
        this.IS_AFFECTED = IS_AFFECTED;
        this.LAT = LAT;
        this.LNG = LNG;
        this.DISTANCE = DISTANCE;
        this.TIME_STAMP = TIME_STAMP;
        this.DATE_ = DATE_;
        this.IS_UPLOADED = IS_UPLOADED;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUSER_MOBILE() {
        return USER_MOBILE;
    }

    public void setUSER_MOBILE(String USER_MOBILE) {
        this.USER_MOBILE = USER_MOBILE;
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

    public String getIS_UPLOADED() {
        return IS_UPLOADED;
    }

    public void setIS_UPLOADED(String IS_UPLOADED) {
        this.IS_UPLOADED = IS_UPLOADED;
    }

    public String getDATE_() {
        return DATE_;
    }

    public void setDATE_(String DATE_) {
        this.DATE_ = DATE_;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getDISTANCE() {
        return DISTANCE;
    }

    public void setDISTANCE(String DISTANCE) {
        this.DISTANCE = DISTANCE;
    }
}
