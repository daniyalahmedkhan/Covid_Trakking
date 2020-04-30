package com.tplcorp.covid_trakking.UI.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.Helper.ProtectedHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Service.BackgroundService;
import com.tplcorp.covid_trakking.UI.MainActivity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HomeFragment extends BaseFragment {

    boolean doubleBackToExitPressedOnce = false;

    MyDatabase myDatabase;

    @BindView(R.id.textPositive)
    TextView textPositive;
    @BindView(R.id.imageView)
    LottieAnimationView imageView;
    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.home_subtitle)
    TextView homeSubtitle;
    @BindView(R.id.tested_button)
    Button testedButton;
    @BindView(R.id.tested_button_text)
    TextView testedButtonText;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;



//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        return view;
//    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        testedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(checkAffectedDate() >= 0 && checkAffectedDate() <= 14)) {
                    showAlertDialog();
                } else {
                    Toast.makeText(getActivity(), "You need to wait 14 days to mark again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        ProtectedHelper.startPowerSaverIntent(getActivity());

        if (!GeneralHelper.isTimeAutomatic(getActivity())) {
            mainLinear.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please change the Date&Time settings to automatically", Toast.LENGTH_LONG).show();
            getActivity().stopService(new Intent(getActivity(), BackgroundService.class));
        } else {
            mainLinear.setVisibility(View.VISIBLE);
            checkBannerState();
            getActivity().startService(new Intent(getActivity(), BackgroundService.class));
        }

    }

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }


    public void showAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("COVID-19 Test")
                .setMessage("Are you sure you want to declare yourself positive COVID-19?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PrefsHelper.putString(PrefConstants.AFFECTED, "1");
                        CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
                        myDatabase.daoAccess().deleteCovidAffects();
                        myDatabase.daoAccess().insertAffectedRecord(covidAffected);
                        checkBannerState();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PrefsHelper.putString(PrefConstants.AFFECTED, "0");
                        checkBannerState();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private long checkAffectedDate() {

        long days = -1;

        myDatabase = DatabaseClient.getDatabaseInstance(getActivity());
        List<CovidAffected> affectedList = myDatabase.daoAccess().affectedList();
        if (affectedList.size() > 0) {
            Date date = affectedList.get(0).getTIME_STAMP();
            days = GeneralHelper.daysDifferent(GeneralHelper.todayDate_DATE(), date);
            return days;
        }

        return days;
    }

    private void checkBannerState() {
        if (PrefsHelper.getString(PrefConstants.AFFECTED, "0").equals("1")) {
            long days = checkAffectedDate();
            if (days == 0) {
                textPositive.setText("You have marked yourself Covid-19 positive today");
            } else {
                textPositive.setText("You had marked yourself Covid-19 positive " + days + " day ago");
            }
            textPositive.setVisibility(View.VISIBLE);
        } else {
            textPositive.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public String getTitleBarName() {
        return "Home";
    }
}