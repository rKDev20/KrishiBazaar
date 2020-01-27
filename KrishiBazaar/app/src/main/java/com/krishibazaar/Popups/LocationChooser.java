package com.krishibazaar.Popups;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.SEARCH;
import static com.krishibazaar.Utils.Constants.SUCCESS;

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
                    try {
                        JSONObject params = new JSONObject();
                        params.put(SEARCH, location.getText().toString());
                        VolleyRequestMaker.makeRequest(context, params, new VolleyRequestMaker.RequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.has(SUCCESS)) {
                                        final JSONObject details = response.getJSONObject(SUCCESS);
                                        result.setText(details.getString(ADDRESS));
                                        result.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    LocationDetails ld = new LocationDetails();
                                                    ld.setName(details.getString(ADDRESS));
                                                    ld.setLatitude(details.getDouble(LATITUDE));
                                                    ld.setLongitude(details.getDouble(LONGITUDE));
                                                    listener.onLocationSelected(ld);
                                                    dialog.cancel();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Log.d("abcd","error");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.d("abcd", "errorr");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

