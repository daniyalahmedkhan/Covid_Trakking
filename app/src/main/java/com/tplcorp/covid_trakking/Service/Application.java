package com.tplcorp.covid_trakking.Service;

import android.content.ContextWrapper;

import com.tplcorp.covid_trakking.Helper.PrefsHelper;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Prefs class
        new PrefsHelper.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
