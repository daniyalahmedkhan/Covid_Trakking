package com.tplcorp.covid_trakking.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tplcorp.covid_trakking.Helper.BluetoothHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Interface.LocationPermission;
import com.tplcorp.covid_trakking.R;

import java.util.List;

public class SetupActivity extends AppCompatActivity implements LocationPermission {

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
                } else {
                    locationPermissionAndGps();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 0) {
                BluetoothHelper.openBluetoothSettings(SetupActivity.this, SetupActivity.this);
            } else {
                locationPermissionAndGps();
            }
        }
    }

    private void locationPermissionAndGps() {
        GeneralHelper.locationPermission(SetupActivity.this, SetupActivity.this);
    }

    @Override
    public void locationPermission(boolean isEnable) {
        if (isEnable) {
            if (GeneralHelper.checkGPS(SetupActivity.this)) {
                Intent i = new Intent(SetupActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            } else {
                GeneralHelper.turnGPSOn(SetupActivity.this, SetupActivity.this);
            }
        }else{
            locationPermissionAndGps();
        }
    }
}

