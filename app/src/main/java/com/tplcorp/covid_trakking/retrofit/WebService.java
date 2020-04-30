package com.tplcorp.covid_trakking.retrofit;



import com.tplcorp.covid_trakking.Model.AffectedDataRequest;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


// To do : make callbacks


public interface WebService {


    @POST(WebServiceConstants.KEY_USER_LOGIN)
    Call<Map<String, Object>> loginUser(
            @Body RequestBody model
    );


    @POST(WebServiceConstants.KEY_POST_INTERACTIONS)
    Call<Map<String, Object>> postInteractionsData(
            @Body AffectedDataRequest model
    );
}
