package com.tplcorp.covid_trakking.UI.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Interface.BottomNavReselect;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MainActivity;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class Feedback extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.settings, rootKey);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (key.equals(PrefConstants.Notifications)) {
            if (pref instanceof SwitchPreferenceCompat) {
                if (((SwitchPreferenceCompat) pref).isChecked()) {
                    Log.d("##", "notification on");
                } else {

                    Log.d("##", "notification off");

                }

            } else if (key.equals(PrefConstants.Feedback)) {
                if (pref instanceof EditTextPreference) {
                    //  Log.d("##", pref.);
                    //  pref.text = ""
                    Toast.makeText(getActivity(), "Thankyou for providing the feedback.", Toast.LENGTH_SHORT).show();


                }


            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).initToolbar("Settings", false);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        ((BottomNavReselect) getActivity()).SetNavState(R.id.Feedback);
        //   getPreferenceScreen().setTitle("Feedback");

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }


}
