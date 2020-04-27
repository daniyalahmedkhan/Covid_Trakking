package com.tplcorp.covid_trakking.Model;

public class Connections {

    private String Name;
    private String Mobile;
    private String Distance;
    private String Affected;


    public Connections(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
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
