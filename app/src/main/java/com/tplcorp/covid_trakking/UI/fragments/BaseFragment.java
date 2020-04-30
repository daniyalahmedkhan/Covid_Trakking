package com.tplcorp.covid_trakking.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tplcorp.covid_trakking.UI.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import butterknife.ButterKnife;

public abstract class BaseFragment extends PreferenceFragmentCompat {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).initToolbar(getTitleBarName(), isBackButton());

        return view;
    }

    public abstract int getFragmentLayout();

    public abstract String getTitleBarName();

    public abstract boolean isBackButton();
//    public abstract void setToolbar();

    /*public void setToolbar(String s) {
        ((MainActivity) getActivity()).initToolbar(getTitleBarName(), isBackButton());

    }*/

}
