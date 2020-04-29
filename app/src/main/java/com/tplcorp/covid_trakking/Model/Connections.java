package com.tplcorp.covid_trakking.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Connections implements Serializable {

    private String Name;
    private String Distance;
    private String Affected;
    private String Lat;
    private String Lng;
    private Long   TimeStamp;

    public Connections(String name, String distance, String affected , String lat, String lng , long timeStamp) {
        Name = name;
        Distance = distance;
        Affected = affected;
        Lat = lat;
        Lng = lng;
        TimeStamp = timeStamp;
    }

    protected Connections(Parcel in) {
        Name = in.readString();
        Distance = in.readString();
        Affected = in.readString();
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getAffected() {
        return Affected;
    }

    public void setAffected(String affected) {
        Affected = affected;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}
