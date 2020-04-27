package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tplcorp.covid_trakking.Interface.ScanningCallback;
import com.tplcorp.covid_trakking.Model.Connections;
import com.tplcorp.covid_trakking.R;

import java.util.List;

public class ConnectionsActivity extends AppCompatActivity implements ScanningCallback {

    RecyclerView RV_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        RV_connection = findViewById(R.id.RV_connection);
    }


    @Override
    public void updateScanningData(List<Connections> connectionsList) {
        try {
            for (int i =0 ; i<connectionsList.size(); i++){
                Log.d("connectionsList" , ""+connectionsList.get(i).getMobile());
            }

        }catch (Exception e){}

        //Toast.makeText(this, ""+connectionsList, Toast.LENGTH_SHORT).show();
    }
}
