package com.krishibazaar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.krishibazaar.Models.User;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity {
    EditText mobile;
    EditText name;
    EditText pincode;
    EditText address;
    Button edit;
    Button transactions;
    User user;
    ConstraintLayout error;

    boolean editMode=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        initUser();
    }

    private void initUser() {
        user = SharedPreferenceManager.getUser(this);
        if (user != null) {
            stopLoading();
            setUser();
        } else showLoading();
        updateProfile();
        setEdit(false);
    }

    private void setUser() {
        mobile.setText(String.valueOf(user.getMobile()));
        address.setText(user.getAddress());
        pincode.setText(String.valueOf(user.getPincode()));
        name.setText(user.getName());
    }

    private void setEdit(boolean enable) {
        mobile.setFocusable(false);
        mobile.setCursorVisible(false);
        editMode=enable;
        address.setFocusable(enable);
        address.setFocusableInTouchMode(enable);
        address.setCursorVisible(enable);
        pincode.setFocusable(enable);
        pincode.setFocusableInTouchMode(enable);
        pincode.setCursorVisible(enable);
        name.setFocusable(enable);
        name.setFocusableInTouchMode(enable);
        name.setCursorVisible(enable);
    }

    private void updateProfile() {
        //TODO
        VolleyRequestMaker.getUserDetails(this, "rishabh", new VolleyRequestMaker.TaskFinishListener<User>() {
            @Override
            public void onSuccess(User response) {
                user = response;
                SharedPreferenceManager.setUser(ProfileActivity.this, user);
                stopLoading();
                setUser();
            }

            @Override
            public void onError(String error) {
                if (user == null)
                    showError(error);
            }
        });
    }

    private void showError(String err) {
        error.setVisibility(View.VISIBLE);
        error.findViewById(R.id.progressBar).setVisibility(GONE);
        ((TextView) (error.findViewById(R.id.loadingText))).setText(err);
        error.findViewById(R.id.loadingText).setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        error.setVisibility(View.VISIBLE);
        error.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        error.findViewById(R.id.loadingText).setVisibility(GONE);
    }

    private void stopLoading() {
        error.setVisibility(GONE);
    }

    private void initViews() {
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        name = findViewById(R.id.name);
        edit = findViewById(R.id.edit);
        transactions = findViewById(R.id.transactions);
        error = findViewById(R.id.errorBox);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode){
                    changeProfile();
                    showLoading();
                }
                else {
                    edit.setText("Update");
                    setEdit(true);
                }
            }
        });
    }

    private void changeProfile() {
        String nameText=name.getText().toString();
        int pincodeText = Integer.valueOf(pincode.getText().toString());
        long mobileText = Long.valueOf(mobile.getText().toString());
        String addressText = address.getText().toString();
        final User user=new User(nameText,mobileText,addressText,pincodeText);
        //TODO
        VolleyRequestMaker.updateUserDetails(this,"rihsbah",user,new VolleyRequestMaker.TaskFinishListener<User>(){
            @Override
            public void onSuccess(User response) {
                ProfileActivity.this.user=response;
                editMode=false;
                edit.setText("Edit");
                setUser();
                SharedPreferenceManager.setUser(ProfileActivity.this,response);
                stopLoading();
            }

            @Override
            public void onError(String error) {
                showError(error);
            }
        });
    }
}
