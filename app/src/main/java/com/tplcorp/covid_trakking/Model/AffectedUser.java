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

}