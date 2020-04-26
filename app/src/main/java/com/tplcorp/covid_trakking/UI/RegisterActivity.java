package com.tplcorp.covid_trakking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.tplcorp.covid_trakking.Helper.GeneralHelper;
import com.tplcorp.covid_trakking.Helper.PrefsHelper;
import com.tplcorp.covid_trakking.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button submit;
    TextInputLayout textlayout;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkIfUserAlreadyLoggedIn()) {
            moveToMainActivity();

        } else {

            setContentView(R.layout.activity_register);
            submit = findViewById(R.id.submit);
            textlayout = findViewById(R.id.textlayout);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textlayout.getEditText().getText().toString().isEmpty() || textlayout.getEditText().getText().toString().length() != 11) {
                        GeneralHelper.showToast(RegisterActivity.this, "Please fill the mobile number correctly");
                    } else {

                        requestPin(textlayout.getEditText().getText().toString());

                    }
                }
            });
        }
    }

    private void requestPin(String mobileNumber) {
        // removing the leading zeros from the mobile number.
        final String updated_mobilenumber = mobileNumber.replaceAll("^0*", "");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + updated_mobilenumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
// verification completed automatically
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        GeneralHelper.showToast(RegisterActivity.this, e.getMessage());

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        GeneralHelper.showToast(RegisterActivity.this, "Code sent");

                      //  mVerificationId = verificationId;

                        Intent i = new Intent(RegisterActivity.this, ValidatePin.class);
                        i.putExtra("verificationId", verificationId);
                        i.putExtra("mobileNumber", "+92"+updated_mobilenumber);
                        startActivity(i);

                    }
                });


    }

    boolean checkIfUserAlreadyLoggedIn() {
        return PrefsHelper.getBoolean("AlreadyLoggedIn", false);


    }

    private void moveToMainActivity() {

        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}

