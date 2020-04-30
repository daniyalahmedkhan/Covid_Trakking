package com.tplcorp.covid_trakking.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffectedDataRequest {

@SerializedName("UserPhoneNumber")
@Expose
private String userPhoneNumber;
@SerializedName("Data")
@Expose
private List<AffectedUser> data = null;

public String getUserPhoneNumber() {
return userPhoneNumber;
}

public void setUserPhoneNumber(String userPhoneNumber) {
this.userPhoneNumber = userPhoneNumber;
}

public List<AffectedUser> getData() {
return data;
}

public void setData(List<AffectedUser> data) {
this.data = data;
}

}