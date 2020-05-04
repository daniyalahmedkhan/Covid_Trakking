package com.tplcorp.covid_trakking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CovidStats {

    @SerializedName("UpdatedOn")
    @Expose
    private String UpdatedOn;


    @SerializedName("Province")
    @Expose
    private String Province;


    @SerializedName("Totalcases")
    @Expose
    private Integer Totalcases;


    @SerializedName("Totaldeaths")
    @Expose
    private Integer Totaldeaths;


    @SerializedName("Recovered")
    @Expose
    private Integer Recovered;


    @SerializedName("Active")
    @Expose
    private Integer Active;


    public String getUpdatedOn() {
        return UpdatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        UpdatedOn = updatedOn;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public Integer getTotalcases() {
        return Totalcases;
    }

    public void setTotalcases(Integer totalcases) {
        Totalcases = totalcases;
    }

    public Integer getTotaldeaths() {
        return Totaldeaths;
    }

    public void setTotaldeaths(Integer totaldeaths) {
        Totaldeaths = totaldeaths;
    }

    public Integer getRecovered() {
        return Recovered;
    }

    public void setRecovered(Integer recovered) {
        Recovered = recovered;
    }

    public Integer getActive() {
        return Active;
    }

    public void setActive(Integer active) {
        Active = active;
    }
}
