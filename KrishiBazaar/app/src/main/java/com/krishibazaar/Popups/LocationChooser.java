package com.krishibazaar.Popups;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.R;
import com.krishibazaar.Utils.LocationManagerActivity;
import com.krishibazaar.Utils.VolleyRequestMaker;

public class LocationChooser {
    public static void popup(final LocationManagerActivity context, final PopupListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("EXIT", null);
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.popup_location_chooser, null);
        builder.setView(alertLayout);
        final AlertDialog dialog = builder.create();
        final EditText location = alertLayout.findViewById(R.id.locationText);
        ImageButton gpsButton = alertLayout.findViewById(R.id.gpsButton);
        Button searchButton = alertLayout.findViewById(R.id.search);
        final TextView result = alertLayout.findViewById(R.id.result);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!location.getText().toString().isEmpty()) {
                    VolleyRequestMaker.getLocationByAddress(context, location.getText().toString(), new VolleyRequestMaker.TaskFinishListener<LocationDetails>() {
                        @Override
                        public void onSuccess(final LocationDetails details) {

                            result.setText(details.getName());
                            result.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listener.onLocationSelected(details);
                                    dialog.cancel();

                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            Log.d("abcd", error);
                        }
                    });
                }
            }
        });
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getLocation(new LocationManagerActivity.LocationListener() {
                    @Override
                    public void onSuccess(final LocationDetails details) {
                        result.setText(details.getName());
                        result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.onLocationSelected(details);
                                dialog.cancel();
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
        dialog.show();
    }
}

