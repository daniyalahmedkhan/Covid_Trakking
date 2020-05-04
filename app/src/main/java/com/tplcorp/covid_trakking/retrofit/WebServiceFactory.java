package com.tplcorp.covid_trakking.retrofit;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServiceFactory

{

    private static WebService mInstance;
    private static WebService mInstance_LIFE;



    private WebServiceFactory() {
        // Exists only to defeat instantiation.

    }


    public static WebService getInstance() {
        if (mInstance == null) {

// set your desired log level
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
            /**
             * To avoid time out exception
             */
            httpclient.connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS);


            httpclient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebServiceConstants.SERVER_URL)
                    .client(httpclient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            mInstance = retrofit.create(WebService.class);

        }
        return mInstance;
    }

    public static WebService getInstanceCOVID() {
        if (mInstance_LIFE == null) {

// set your desired log level
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
            /**
             * To avoid time out exception
             */
            httpclient.connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS);


            httpclient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebServiceConstants.LIVE_COVID_STATS)
                    .client(httpclient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            mInstance_LIFE = retrofit.create(WebService.class);

        }
        return mInstance_LIFE;
    }


}
