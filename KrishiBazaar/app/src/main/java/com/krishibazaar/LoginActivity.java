package com.krishibazaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.krishibazaar.Models.Authentication;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS_EXIST;
import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS_NEW;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class LoginActivity extends AppCompatActivity {
    EditText mob;
    ConstraintLayout l1, l2;
    OtpView otpView;
    Button sendOtp, loginButton;
    long mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l1 = findViewById(R.id.login1);
        l2 = findViewById(R.id.login2);
        otpView = findViewById(R.id.otp_view);
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.INVISIBLE);
        mob = findViewById(R.id.num);
        sendOtp = findViewById(R.id.otpbutton);
        loginButton = findViewById(R.id.loginbutton);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = mob.getText().toString();
                if (mobile.length() == 10) {
                    mobileNumber = Long.parseLong(mobile);
                    generateOTP(mobileNumber);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                }
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(final String enteredotp) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(LoginActivity.this);
                }
                assert manager != null;
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                loginButton.setClickable(true);
                loginButton.setFocusable(true);
                loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_background));
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifyOTP(mobileNumber, Integer.parseInt(enteredotp));
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpView.getText().toString();
                if (otp.length() == 6) {
                    verifyOTP(mobileNumber, Integer.parseInt(otp));
                } else
                    Toast.makeText(LoginActivity.this, "Enter otp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateOTP(long mobileNumber) {
        sendOtp.setClickable(false);
        mob.setFocusable(false);
        sendOtp.setBackground(getResources().getDrawable(R.drawable.button_disable));
        VolleyRequestMaker.sendOtp(this, mobileNumber, new VolleyRequestMaker.TaskFinishListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                l1.setVisibility(View.INVISIBLE);
                l2.setVisibility(View.VISIBLE);
                loginButton.setClickable(false);
                loginButton.setFocusable(false);
                loginButton.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                sendOtp.setClickable(true);
                mob.setFocusable(true);
                sendOtp.setBackground(getResources().getDrawable(R.drawable.login_button_background));
            }
        });
    }

    public void verifyOTP(long mobileNumber, int otpPassword) {
        loginButton.setClickable(false);
        loginButton.setFocusable(false);
        loginButton.setBackground(getResources().getDrawable(R.drawable.button_disable));
        VolleyRequestMaker.verifyOtp(this, mobileNumber, otpPassword, new VolleyRequestMaker.TaskFinishListener<Authentication>() {
            @Override
            public void onSuccess(Authentication response) {
                if (response.getStatus() == STATUS_SUCCESS_NEW) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra(TOKEN, response.getToken());
                    startActivity(intent);
                } else if (response.getStatus() == STATUS_SUCCESS_EXIST) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    SharedPreferenceManager.setToken(LoginActivity.this, response.getToken());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                loginButton.setClickable(true);
                loginButton.setFocusable(true);
                loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_background));
            }
        });
    }
}

