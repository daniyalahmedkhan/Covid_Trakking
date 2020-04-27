package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tplcorp.covid_trakking.Helper.BluetoothHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.R;

public class SetupActivity extends AppCompatActivity {

    Button grant_location_access_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        grant_location_access_button = findViewById(R.id.grant_location_access_button);

        grant_location_access_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!BluetoothHelper.isBluetooth(SetupActivity.this)) {
                    BluetoothHelper.openBluetoothSettings(SetupActivity.this, SetupActivity.this);
                   // GeneralHelper.showToast(Setup.this, "Please open bluetooth setting");

                } else {
                    if (GeneralHelper.locationPermission(SetupActivity.this)) {
                        if (GeneralHelper.checkGPS(SetupActivity.this)){
                            Intent i = new Intent(SetupActivity.this , RegisterActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            GeneralHelper.turnGPSOn(SetupActivity.this, SetupActivity.this);
                        }
                    }
                }
            }
        });
    }
}
