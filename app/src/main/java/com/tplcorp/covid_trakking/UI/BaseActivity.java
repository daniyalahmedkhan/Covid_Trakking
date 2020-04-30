package com.tplcorp.covid_trakking.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.fragments.BaseFragment;


public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void initToolbar(String title , boolean isButton){
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);

        if(title != null)
            toolbar.setTitle(title);

        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(isButton);


    }


    public void addDockableFragment(BaseFragment fragment) {

        getFragmentManager().beginTransaction().
                replace(R.id.container, fragment).
                addToBackStack(fragment.getClass().getSimpleName())

                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
