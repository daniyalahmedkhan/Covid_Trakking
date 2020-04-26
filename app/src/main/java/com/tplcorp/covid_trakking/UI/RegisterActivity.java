package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;

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

                    PrefsHelper.putString(PrefConstants.MOBILE, textlayout.getEditText().getText().toString().trim());
                    Intent i = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
