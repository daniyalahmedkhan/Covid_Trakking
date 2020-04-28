package com.tplcorp.covid_trakking.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Connections implements Serializable {

    private String Name;
    private String Distance;
    private String Affected;

    public Connections(String name, String distance, String affected) {
        Name = name;
        Distance = distance;
        Affected = affected;
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


}
