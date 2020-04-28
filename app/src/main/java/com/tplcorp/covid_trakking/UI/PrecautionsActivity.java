package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplcorp.covid_trakking.R;

public class PrecautionsActivity extends BaseActivity {

    ImageView IV_manu;
    TextView TV_home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precautions);
        IV_manu = findViewById(R.id.IV_manu);
        TV_home = findViewById(R.id.TV_home);

        IV_manu.setVisibility(View.GONE);
        TV_home.setVisibility(View.GONE);
        initToolbar("Precautions");

    }
}
