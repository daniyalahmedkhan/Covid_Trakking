package com.tplcorp.covid_trakking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffectedUser {

    @SerializedName("Is_Affected")
    @Expose
    private Integer isAffected;
    @SerializedName("Distance")
    @Expose
    private String distance;
    @SerializedName("InteractionTime")
    @Expose
    private String interactionTime;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Latitude")
    @Expose
    private String Latitude;
    @SerializedName("Longitude")
    @Expose
    private String Longitude;

    public Integer getIsAffected() {
        return isAffected;
    }

    public void setIsAffected(Integer isAffected) {
        this.isAffected = isAffected;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getInteractionTime() {
        return interactionTime;
    }

    public void setInteractionTime(String interactionTime) {
        this.interactionTime = interactionTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}