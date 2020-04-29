package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tplcorp.covid_trakking.R;
import com.tplmaps3d.LngLat;
import com.tplmaps3d.MapController;
import com.tplmaps3d.MapView;
import com.tplmaps3d.Marker;
import com.tplmaps3d.MarkerOptions;

import java.util.List;

public class MapActivity extends BaseActivity implements  MapView.OnMapReadyCallback{

    MapView MV;
    MapController MC2;
    Location myLngLat;
    TextView TV_home;
    ImageView IV_manu;
    Double LAT , LNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MV = findViewById(R.id.tpl_map);

        IV_manu = findViewById(R.id.IV_manu);
        TV_home = findViewById(R.id.TV_home);

        IV_manu.setVisibility(View.GONE);
        TV_home.setVisibility(View.GONE);
        initToolbar("Active Connections Locator");


        Intent intent = getIntent();
        LAT = Double.valueOf(intent.getStringExtra("LAT"));
        LNG = Double.valueOf(intent.getStringExtra("LNG"));

        initMap();
    }

    private void initMap(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(MapActivity.this)
                    .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        loadMap();
                    } else {
                        Toast.makeText(MapActivity.this, "We need permission to continue the work", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
        }else{
            loadMap();
        }
    }

    private void loadMap(){
        MV.onCreate(getIntent().getExtras());
        MV.loadMapAsync(this);
        // searchManager.setListener(this);
    }

    @Override
    public void onMapReady(MapController mapController) {
        MC2 = mapController;
        //MC2.setMyLocationEnabled(false);
        MC2.setMyLocationEnabled(true);
        MC2.getUiSettings().showMyLocationButton(true);


        MC2.setOnMyLocationChangeListener(new MapController.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChanged(Location location) {
                myLngLat = new Location(location);
                myLngLat.setLongitude(LNG);
                myLngLat.setLatitude(LAT);
                setMarker();
                setZoom();
            }

            @Override
            public void onMyLocationFirstFix(Location location) {

            }

        });


    }

    private void setMarker(){

        MC2.removeAllMarkers();

        Marker marker = MC2.addMarker(new MarkerOptions()

                .position(new LngLat(myLngLat.getLongitude(), myLngLat.getLatitude()))

                .title("Active Connection")

                .description(""));

    }

    private void setZoom(){

        MC2.setLngLatZoom(new LngLat(myLngLat.getLongitude() , myLngLat.getLatitude()), 14, 1000);
    }

    public void initToolbar(String title){
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);

        if(title != null)
            toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
