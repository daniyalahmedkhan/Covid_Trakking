package com.tplcorp.covid_trakking.Service.JobService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.tplcorp.covid_trakking.Service.BackgroundService;

public class JobServiceMain extends JobService {

    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("JOBSERVICE" , "JobServiceMain");
        Intent service = new Intent(getApplicationContext(), BackgroundService.class);
        getApplicationContext().startService(service);
        Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("JOBSERVICE" , "JobServiceMainStop");
        return true;
    }

}
