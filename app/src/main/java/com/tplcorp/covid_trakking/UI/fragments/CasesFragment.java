package com.tplcorp.covid_trakking.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tplcorp.covid_trakking.R;

import butterknife.BindView;

public class CasesFragment extends BaseFragment {


    @BindView(R.id.pbCalories)
    ProgressBar pbCalories;

    public static CasesFragment newInstance() {

        Bundle args = new Bundle();

        CasesFragment fragment = new CasesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbCalories.setProgress(2500);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.covid_cases;
    }

    @Override
    public String getTitleBarName() {
        return "Dashboard";
    }

    @Override
    public boolean isBackButton() {
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
