package com.tplcorp.covid_trakking.UI.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tplcorp.covid_trakking.Helper.BackgroundServiceHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.Helper.ProtectedHelper;
import com.tplcorp.covid_trakking.Model.AffectedDataRequest;
import com.tplcorp.covid_trakking.Model.AffectedUser;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.Room.DatabaseClient;
import com.tplcorp.covid_trakking.Room.MyDatabase;
import com.tplcorp.covid_trakking.Room.Tables.CovidAffected;
import com.tplcorp.covid_trakking.Room.Tables.TracingData;
import com.tplcorp.covid_trakking.Service.BackgroundService;
import com.tplcorp.covid_trakking.UI.ValidatePinActivity;
import com.tplcorp.covid_trakking.retrofit.WebService;
import com.tplcorp.covid_trakking.retrofit.WebServiceFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    boolean firstOpen = true;


    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        if (!(PrefsHelper.getBoolean(PrefConstants.REGISTER, false))) {
            loginUer(PrefsHelper.getString(PrefConstants.MOBILE));
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        testedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(checkAffectedDate() >= 0 && checkAffectedDate() <= 14)) {
                    showCustomDialog();
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
            Toast.makeText(getActivity(), "Please change the Date&Time settings to automatic", Toast.LENGTH_LONG).show();
            getActivity().stopService(new Intent(getActivity(), BackgroundService.class));
        } else {
            mainLinear.setVisibility(View.VISIBLE);
            checkBannerState();
            getActivity().startService(new Intent(getActivity(), BackgroundService.class));
            if (firstOpen){
                checkUserIsInfected();
            }

        }

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
                textPositive.setText("You have marked yourself COVID-19 positive today.");
                textPositive.setVisibility(View.VISIBLE);
            } else if (days > 0) {
                textPositive.setText("You had marked yourself COVID-19 positive " + days + " day ago.");
                textPositive.setVisibility(View.VISIBLE);
            } else {
                textPositive.setVisibility(View.GONE);
            }

        } else {
            textPositive.setVisibility(View.GONE);
        }

    }

    public void showCustomDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);


        Button dialogNo = (Button) dialog.findViewById(R.id.no);
        Button dialogYes = (Button) dialog.findViewById(R.id.yes);

        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  PrefsHelper.putString(PrefConstants.AFFECTED, "1");
                CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
                myDatabase.daoAccess().deleteCovidAffects();
                myDatabase.daoAccess().insertAffectedRecord(covidAffected);
              //  checkBannerState();
                dialog.dismiss();
            }
        });


        dialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsHelper.putString(PrefConstants.AFFECTED, "0");
                checkBannerState();
                dialog.dismiss();
            }
        });


        dialog.show();

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

    @Override
    public boolean isBackButton() {
        return false;
    }

    private void loginUer(String phoneNumer) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put("PhoneNumber", phoneNumer);
        jsonParams.put("DeviceToken", FirebaseInstanceId.getInstance().getToken());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        WebServiceFactory.getInstance().loginUser(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {


                if (response.body() != null && response.body().get("RespMsg").equals("Success")) {
                    PrefsHelper.putBoolean(PrefConstants.REGISTER, true);
                } else {
                    //Toast.makeText(getActivity(), response.body().get("RespMsg").toString() + "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void checkUserIsInfected() {

        firstOpen = false;
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("PhoneNumber", PrefsHelper.getString(PrefConstants.MOBILE));

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        WebServiceFactory.getInstance().getInfected(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                if (response.body() != null) {
                    if (response.body().get("RespCode").equals(1) || response.body().get("RespCode").equals(1.0)) {
                        BackgroundServiceHelper.uploadDataToServer(PrefsHelper.getString(PrefConstants.MOBILE) , getActivity());
                    }
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });

    }
}