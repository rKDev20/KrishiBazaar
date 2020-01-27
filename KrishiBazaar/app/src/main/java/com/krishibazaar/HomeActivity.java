package com.krishibazaar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Popups.LocationChooser;
import com.krishibazaar.Popups.PopupListener;
import com.krishibazaar.Utils.LocationManagerActivity;

public class HomeActivity extends LocationManagerActivity {

    LocationDetails locationDetails;
    TextView locationText;
    EditText searchBox;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        getLocation(new LocationListener() {
            @Override
            public void onSuccess(LocationDetails details) {
                locationText.setText(details.getName());
                locationDetails=details;
            }

            @Override
            public void onError(String error) {
                locationText.setText(error);
            }
        });
    }

    void initViews(){
        locationText=findViewById(R.id.location);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationChooser.popup(HomeActivity.this, new PopupListener() {
                    @Override
                    public void onLocationSelected(LocationDetails details) {
                        locationText.setText(details.getName());
                        locationDetails=details;
                    }
                });
            }
        });
        searchBox=findViewById(R.id.search_box);
    }

}
