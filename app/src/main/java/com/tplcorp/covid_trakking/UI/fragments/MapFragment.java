package com.tplcorp.covid_trakking.UI.fragments;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MapActivity;
import com.tplmaps3d.LngLat;
import com.tplmaps3d.MapController;
import com.tplmaps3d.MapView;
import com.tplmaps3d.Marker;
import com.tplmaps3d.MarkerOptions;

import java.util.List;

import butterknife.BindView;

public class MapFragment extends BaseFragment implements  MapView.OnMapReadyCallback {

    @BindView(R.id.tpl_map)
    MapView MV;

    @BindView(R.id.TV_home)
    TextView TV_home;
    @BindView(R.id.IV_manu)
    ImageView IV_manu;

    MapController MC2;
    Double LAT , LNG;
    Location myLngLat;


    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        LAT = Double.valueOf(intent.getStringExtra("LAT"));
        LNG = Double.valueOf(intent.getStringExtra("LNG"));

        initMap();
    }

    private void initMap(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(getActivity())
                    .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        loadMap();
                    } else {
                        Toast.makeText(getActivity(), "We need permission to continue the work", Toast.LENGTH_SHORT).show();
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
        MV.onCreate(getActivity().getIntent().getExtras());
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
    
    
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_map;
    }

    @Override
    public String getTitleBarName() {
        return "Connection Locator";
    }

    @Override
    public boolean isBackButton() {
        return true;
    }
}
