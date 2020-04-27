package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;

public class SplashActivity extends AppCompatActivity {


    Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start_button = findViewById(R.id.start_button);

        if (PrefsHelper.getBoolean(PrefConstants.AlreadyLoggedIn)){
            Intent i = new Intent(SplashActivity.this , MainActivity.class);
            startActivity(i);
            finish();
        }

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SplashActivity.this , SetupActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
}
