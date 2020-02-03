package com.krishibazaar.Popups;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.R;
import com.krishibazaar.Utils.LocationManagerActivity;
import com.krishibazaar.Utils.VolleyRequestMaker;

public class LocationChooser {
    private LocationManagerActivity context;
    private PopupListener listener;

    private EditText location;
    private TextView result;
    private ImageButton search;
    private ProgressBar progressBar;
    private ProgressBar gpsLoading;
    private View divider;

    public LocationChooser(LocationManagerActivity context, PopupListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void popup() {
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.DialogStyle);

        View view = LayoutInflater.from(context).inflate(R.layout.popup_location_chooser, null);
        dialog.setContentView(view);
        location = view.findViewById(R.id.search_box);
        Button gps = view.findViewById(R.id.gps);
        search = view.findViewById(R.id.searchButton);
        result = view.findViewById(R.id.result);
        progressBar = view.findViewById(R.id.progressBar);
        gpsLoading = view.findViewById(R.id.gpsProgress);
        divider = view.findViewById(R.id.divider);
        final LocationManagerActivity.LocationListener l = new LocationManagerActivity.LocationListener() {
            @Override
            public void onSuccess(final LocationDetails details) {
                setLocation(details.getName());
                unsetGpsLoading();
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
                unsetGpsLoading();
                setError(error);
            }
        };
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = location.getText().toString();
                if (!search.isEmpty() && search.length() > 3) {
                    setSearchLoading();
                    VolleyRequestMaker.getLocationByAddress(context, search, new VolleyRequestMaker.TaskFinishListener<LocationDetails>() {
                        @Override
                        public void onSuccess(final LocationDetails details) {
                            unsetSearchLoading();
                            setLocation(details.getName());
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
                            unsetSearchLoading();
                            setError(error);
                        }
                    });
                }
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGpsLoading();
                context.getLocation(l);
            }
        });
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    private void setLocation(String loc) {
        result.setTextColor(Color.BLACK);
        divider.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        result.setText(loc);
    }

    private void setSearchLoading() {
        search.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void unsetSearchLoading() {
        search.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void setGpsLoading() {
        gpsLoading.setVisibility(View.VISIBLE);
    }

    private void unsetGpsLoading() {
        gpsLoading.setVisibility(View.GONE);
    }
}

