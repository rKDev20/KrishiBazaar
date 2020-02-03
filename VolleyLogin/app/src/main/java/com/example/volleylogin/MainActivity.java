package com.example.volleylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText mob;
    ConstraintLayout l1, l2;
    OtpView otpView;
    ProgressBar loading;
    Button sendotp, loginbutton;
    String url1 = "http://192.168.43.151/generateOTP.php";
    String url2 = "http://192.168.43.151/verifyOTP.php";
    String mobileNumber;
    String otpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent i=new Intent(this,SellProducts.class);
//        startActivity(i);
        l1 = findViewById(R.id.login1);
        l2 = findViewById(R.id.login2);
        otpView = findViewById(R.id.otp_view);
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.INVISIBLE);
        mob = findViewById(R.id.num);
        sendotp = findViewById(R.id.otpbutton);
        loginbutton = findViewById(R.id.loginbutton);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = mob.getText().toString();
                if (mobileNumber.length() == 10) {
                    generateOTP(mobileNumber);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                }
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(final String enteredotp) {
                Log.d("abcd", "here mf");
                InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(MainActivity.this);
                }
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                loginbutton.setClickable(true);
                loginbutton.setFocusable(true);
                loginbutton.setBackground(getDrawable(R.drawable.login_button_background));
                loginbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifyOTP(mobileNumber, enteredotp);
                    }
                });
            }
        });
    }

    public void generateOTP(final String mobileNumber) {
        //loading.setVisibility(View.VISIBLE);
        //send.setVisibility(View.VISIBLE);
        sendotp.setClickable(false);
        mob.setFocusable(false);
        sendotp.setBackground(getDrawable(R.drawable.button_disable));
        JSONObject params = new JSONObject();
        try {
            params.put("mobileNumber", mobileNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("otp_sent")) {
                                l1.setVisibility(View.INVISIBLE);
                                l2.setVisibility(View.VISIBLE);
                                loginbutton.setClickable(false);
                                loginbutton.setFocusable(false);
                                loginbutton.setBackground(getDrawable(R.drawable.button_disable));


                                loginbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String otp = otpView.getText().toString();
                                        if (otp.length() == 6)
                                            verifyOTP(mobileNumber, otp);
                                        else
                                            Toast.makeText(MainActivity.this, "Enter otp", Toast.LENGTH_SHORT);
                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_LONG).show();
                            sendotp.setClickable(true);
                            mob.setFocusable(true);
                            sendotp.setBackground(getDrawable(R.drawable.login_button_background));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        sendotp.setClickable(true);
                        mob.setFocusable(true);
                        sendotp.setBackground(getDrawable(R.drawable.login_button_background));
                        Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void verifyOTP(final String mobileNumber, String otpPassword) {
        //loading.setVisibility(View.VISIBLE);
        //send.setVisibility(View.VISIBLE);
        Log.d("abcd", "otp called");
        loginbutton.setClickable(false);
        loginbutton.setFocusable(false);
        loginbutton.setBackground(getDrawable(R.drawable.button_disable));
        JSONObject params = new JSONObject();
        try {
            params.put("mobileNumber", mobileNumber);
            params.put("otpPassword", otpPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success_new")) {
                                //New User
                                Intent intent = new Intent(MainActivity.this, Register.class);
                                intent.putExtra("mobNum", mobileNumber);
                                startActivity(intent);
                            } else if (status.equals("success_exists")) {
                                //Old User
                                Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL !", Toast.LENGTH_LONG).show();
                            } else if (status.equals("fail")) {
                                //Wrong OTP
                                Toast.makeText(MainActivity.this, "Incorrect OTP !", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}

