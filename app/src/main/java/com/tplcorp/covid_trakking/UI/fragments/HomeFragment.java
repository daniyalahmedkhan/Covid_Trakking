package com.tplcorp.covid_trakking.UI.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tplcorp.covid_trakking.Helper.BackgroundServiceHelper;
import com.tplcorp.covid_trakking.Helper.DatabaseHelper;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.Path_from_Uri;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.Helper.ProtectedHelper;
import com.tplcorp.covid_trakking.Interface.BottomNavReselect;
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

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    File file;
    Button attachment;
    private Context context;

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
//                if (!(checkAffectedDate() >= 0 && checkAffectedDate() <= 14)) {
                    Dexter.withContext(getActivity()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                showCustomDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
//                } else {
//                    Toast.makeText(getActivity(), "You need to wait 14 days to mark again", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ((BottomNavReselect) getActivity()).SetNavState(R.id.Home);

        context = getActivity();
        ProtectedHelper.startPowerSaverIntent(getActivity());
        myDatabase = DatabaseClient.getDatabaseInstance(getActivity());

        if (!GeneralHelper.isTimeAutomatic(getActivity())) {
            mainLinear.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please change the Date&Time settings to automatic", Toast.LENGTH_LONG).show();
            getActivity().stopService(new Intent(getActivity(), BackgroundService.class));
        } else {
            mainLinear.setVisibility(View.VISIBLE);
            checkBannerState();
            getActivity().startService(new Intent(getActivity(), BackgroundService.class));
            if (firstOpen) {
                checkUserIsInfected();
            }

        }
    }


    private long checkAffectedDate() {

        long days = -1;
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
        attachment = (Button) dialog.findViewById(R.id.attachment);
        LinearLayout LL_Selection = (LinearLayout) dialog.findViewById(R.id.LL_Selection);

        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file = null;
                LL_Selection.setVisibility(View.GONE);
                attachment.setVisibility(View.VISIBLE);
                //  PrefsHelper.putString(PrefConstants.AFFECTED, "1");
                //  checkBannerState();
                // dialog.dismiss();
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

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    if (attachment.getText().toString().equalsIgnoreCase("done")){
                        dialog.dismiss();
                    }else{
                        uploadReport();
                    }

                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1);
                }

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
                        BackgroundServiceHelper.uploadDataToServer(PrefsHelper.getString(PrefConstants.MOBILE), getActivity());
                    }
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });

    }

    private void uploadReport() {

        attachment.setText("File is Uploading");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("uploaded_file", "uploaded_file", requestFile);

        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"), PrefsHelper.getString(PrefConstants.MOBILE));
       // MultipartBody.Part phone = MultipartBody.Part.createFormData("PhoneNumber", "PhoneNumber", phoneNumber);

        WebServiceFactory.getInstance().uploadReport(phoneNumber, fileData).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                if (response.body() != null && response.body().get("RespCode").equals(1) || response.body().get("RespCode").equals(1.0)) {

                    Toast.makeText(context, "Your request submitted successfully", Toast.LENGTH_SHORT).show();
                     CovidAffected covidAffected = new CovidAffected(PrefsHelper.getString(PrefConstants.MOBILE), "1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate());
                     myDatabase.daoAccess().deleteCovidAffects();
                     myDatabase.daoAccess().insertAffectedRecord(covidAffected);
                     DatabaseHelper.insertNotificationDB(context, "Your request submitted successfully","1", GeneralHelper.todayDate_DATE(), GeneralHelper.todayDate() , 1);
                     attachment.setText("Done");

                } else {
                    attachment.setText("Upload Failed! Try Again");
                    Toast.makeText(context, "Your request failed to submit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.d("REPORT" , "FAILED " + t);
                attachment.setText("Upload Failed! Try Again");
                Toast.makeText(context, "Failed: "+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data.getData() != null) {

                try {
                    Uri imageUri = data.getData();
                    file = new File(Path_from_Uri.getPath(getActivity(), imageUri));
                    file = GeneralHelper.CompressPic(file , getActivity());
                    attachment.setText("Do You Want To Submit Report?");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Failed due to compress image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}