package com.tplcorp.covid_trakking.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.UI.MainActivity;

public class PrecautionsFragment extends BaseFragment {



    @Override
    public int getFragmentLayout() {
        return R.layout.activity_precautions;
    }

    @Override
    public String getTitleBarName() {
        return "Precautions";
    }

    @Override
    public boolean isBackButton() {
        return false;
    }


}
