package com.tplcorp.covid_trakking.UI;

import android.os.Bundle;
import android.view.MenuItem;

import com.tplcorp.covid_trakking.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;


public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(String title, boolean isButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (title != null)
            toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isButton);


    }


    public void addDockableFragment(PreferenceFragmentCompat fragment) {

        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = ((AppCompatActivity) this).getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, fragment , fragmentTag).
                    addToBackStack(backStateName)
                    .commit();
        }

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
