package com.krishibazaar.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.R;

@SuppressLint("Registered")
public class LocationManagerActivity extends AppCompatActivity {

    int RC_LOCATION_REQUEST = 1;
    int RC_PERMISSION_LOCATION = 2;
    boolean onGoing = false;
    private LocationListener listener;

    public void getLocation(LocationListener listener) {
        this.listener = listener;
        if (!onGoing) {
            onGoing = true;
            checkForLocationPermission();
        }
    }

    private void checkForLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                permissionAvailable();
            else
                requestForPermission();
        }
    }

    private void permissionAvailable() {
        LocationRequest gps = new LocationRequest();
        gps.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        gps.setInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(gps);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                searchLocationPassively();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(LocationManagerActivity.this, RC_LOCATION_REQUEST);
                    } catch (IntentSender.SendIntentException e1) {
                        onError(getString(R.string.play_services_required));
                    }
                }
            }
        });
    }

    private void requestForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_PERMISSION_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abcd", "onAvtivityResult()");
        if (requestCode == RC_LOCATION_REQUEST && resultCode == RESULT_OK) {
            searchLocationPassively();
        } else {
            onError(getString(R.string.turn_on_location));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionAvailable();
            } else {
                onError(getString(R.string.enable_location));
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void searchLocationPassively() {
        Log.d("abcd", "searchLocationPassively()");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null)
                getLocationAddress(location.getLatitude(), location.getLongitude());
            else searchLocationActively();
        }).addOnFailureListener(this, e -> onError(getString(R.string.failed_location)));
    }

    @SuppressLint("MissingPermission")
    private void searchLocationActively() {
        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setInterval(1000);
        final LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                getLocationAddress(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                client.removeLocationUpdates(this);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d("abcd", "onLocationAvailable()");
                if (!locationAvailability.isLocationAvailable())
                    onError(getString(R.string.failed_location));
            }
        };
        client.requestLocationUpdates(request, callback, null);
    }

    private void getLocationAddress(final double latitude, final double longitude) {
        VolleyRequestMaker.getLocationByCoordinates(this, latitude, longitude, new VolleyRequestMaker.TaskFinishListener<LocationDetails>() {
            @Override
            public void onSuccess(LocationDetails details) {
                onGoing = false;
                listener.onSuccess(details);
            }

            @Override
            public void onError(String error) {
                LocationManagerActivity.this.onError(error);
            }
        });
    }

    private void onError(String error) {
        onGoing = false;
        listener.onError(error);
    }

    public interface LocationListener {
        void onSuccess(LocationDetails details);

        void onError(String error);
    }
}
