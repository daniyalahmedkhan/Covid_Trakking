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
    private ProgressDialog dialog;


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
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        loginUer(PrefsHelper.getString(PrefConstants.MOBILE));

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


//    public void showAlertDialog() {
//
//        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setTitle("Report COVID-19")
//                .setMessage("Are you sure ?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        PrefsHelper.putString(PrefConstants.AFFECTED, "1");
//                        CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
//                        myDatabase.daoAccess().deleteCovidAffects();
//                        myDatabase.daoAccess().insertAffectedRecord(covidAffected);
//                        checkBannerState();
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        PrefsHelper.putString(PrefConstants.AFFECTED, "0");
//                        checkBannerState();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//
//    }


    private void uploadDataToServer(String mobileNumber) {


        AffectedDataRequest model = new AffectedDataRequest();
        model.setUserPhoneNumber(mobileNumber);
        model.setData(getAffectedUsersFromDB());


        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Please wait data uploading ...");
        dialog.show();

        WebServiceFactory.getInstance().postInteractionsData(model).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {


                dialog.dismiss();
                if (response.body() != null && response.body().get("RespMsg").toString().equalsIgnoreCase("Success")) {
                    Toast.makeText(getActivity(), "Data Uploaded successfully", Toast.LENGTH_SHORT).show();
                    myDatabase.daoAccess().deleteTracingData();
                } else {
                    Toast.makeText(getActivity(), response.body().get("RespMsg").toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();

            }
        });
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
            } else if (days > 0){
                textPositive.setText("You had marked yourself COVID-19 positive " + days + " day ago.");
                textPositive.setVisibility(View.VISIBLE);
            }else{
                textPositive.setVisibility(View.GONE);
            }

        } else {
            textPositive.setVisibility(View.GONE);
        }

    }

    private List<AffectedUser> getAffectedUsersFromDB() {

        List<AffectedUser> arrData = new ArrayList<>();
        myDatabase = DatabaseClient.getDatabaseInstance(getActivity());
        List<TracingData> affectedList = myDatabase.daoAccess().getTracingData();
        if (affectedList.size() > 0) {

            for (TracingData model : affectedList) {

                AffectedUser entity = new AffectedUser();
                entity.setPhoneNumber(model.getUSER_MOBILE());
                entity.setInteractionTime(String.valueOf(model.getTIME_STAMP()));
                entity.setIsAffected(Integer.valueOf(model.getIS_AFFECTED()));
                entity.setDistance(model.getDISTANCE());
                arrData.add(entity);

            }

        }

        return arrData;
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
                PrefsHelper.putString(PrefConstants.AFFECTED, "1");
                CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
                myDatabase.daoAccess().deleteCovidAffects();
                myDatabase.daoAccess().insertAffectedRecord(covidAffected);
                checkBannerState();
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

    private void loginUer(String phoneNumer)
    {

        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put("PhoneNumber", phoneNumer);
        jsonParams.put("DeviceToken", FirebaseInstanceId.getInstance().getToken());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());


        WebServiceFactory.getInstance().loginUser(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {


                if(response.body()!=null&&response.body().get("RespMsg").equals("Success"))
                {

                }
                else
                {
                    Toast.makeText(getActivity(), response.body().get("RespMsg").toString()+"", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }
}