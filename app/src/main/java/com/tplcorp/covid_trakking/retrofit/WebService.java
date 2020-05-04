package com.tplcorp.covid_trakking.retrofit;


import com.tplcorp.covid_trakking.Model.AffectedDataRequest;
import com.tplcorp.covid_trakking.Model.CovidStats;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


// To do : make callbacks


public interface WebService {


    @POST(WebServiceConstants.KEY_USER_LOGIN)
    Call<Map<String, Object>> loginUser(
            @Body RequestBody model);


    @POST(WebServiceConstants.KEY_POST_INTERACTIONS)
    Call<Map<String, Object>> postInteractionsData(
            @Body AffectedDataRequest model);

    @POST(WebServiceConstants.KEY_POST_COVID_STATS)
    Call<List<CovidStats>> getCovidStats();

    @POST(WebServiceConstants.CHECK_IS_INFECTED)
    Call<Map<String, Object>> getInfected(@Body RequestBody body);
}
