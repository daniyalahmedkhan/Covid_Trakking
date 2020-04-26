package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Service.BroadcastReceiver;

public class RegisterActivity extends AppCompatActivity  {

    Button submit;
    TextInputLayout textlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submit = findViewById(R.id.submit);
        textlayout = findViewById(R.id.textlayout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textlayout.getEditText().getText().toString().isEmpty() || textlayout.getEditText().getText().toString().length() != 10){
                    GeneralHelper.showToast(RegisterActivity.this , "Please fill number correctly");
                }else{
                    registerBroadCast();
                    PrefsHelper.putString("MOBILE" , textlayout.getEditText().getText().toString().trim());
                    Intent i = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void registerBroadCast(){

        AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),100000 ,
                pendingIntent);
    }
}
