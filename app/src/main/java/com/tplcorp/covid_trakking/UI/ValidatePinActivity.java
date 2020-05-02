package com.tplcorp.covid_trakking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefConstants;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;
import com.tplcorp.covid_trakking.retrofit.WebService;
import com.tplcorp.covid_trakking.retrofit.WebServiceFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidatePinActivity extends AppCompatActivity {
    String verificationId = "";
    String mobileNumber = "";
    TextInputLayout pinCode;
    boolean result;
    Button submit , resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validatepin);
        pinCode = findViewById(R.id.pincode);
        submit = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);

        Intent intent = getIntent();

        verificationId = intent.getStringExtra("verificationId");
        mobileNumber = intent.getStringExtra("mobileNumber");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePinCode(verificationId);

            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPin(mobileNumber);
            }
        });


    }

    void validatePinCode(String verificationId) {
        if (verificationId != "" || verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinCode.getEditText().getText().toString());
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                //PrefsHelper.putString(PrefConstants.MOBILE, mobileNumber.trim());
                                PrefsHelper.putBoolean(PrefConstants.AlreadyLoggedIn, true);
                                Intent i = new Intent(ValidatePinActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                               // loginUer(mobileNumber.trim());

                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                }
                                GeneralHelper.showToast(ValidatePinActivity.this, "Error occured.");


                            }
                        }
                    });


        }


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
                    Toast.makeText(ValidatePinActivity.this, response.body().get("RespMsg").toString()+"", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void requestPin(final String mobileNumber) {
        // removing the leading zeros from the mobile number.

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                0,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        PrefsHelper.putString(PrefConstants.MOBILE, mobileNumber.trim());
                        PrefsHelper.putBoolean(PrefConstants.AlreadyLoggedIn, true);
                        Intent i = new Intent(ValidatePinActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        GeneralHelper.showToast(ValidatePinActivity.this, e.getMessage());

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        GeneralHelper.showToast(ValidatePinActivity.this, "Code sent");

                    }
                });


    }

}
