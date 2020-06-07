package com.krishibazaar.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Models.User;

public class SharedPreferenceManager {

    private final static String SP_USER = "user";
    private final static String SP_LOCATION = "location";
    private final static String NAME = "name";
    private final static String MOBILE = "mobile";
    private final static String ADDRESS = "address";
    private final static String PINCODE = "pincode";
    private final static String TOKEN = "token";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";

    public static LocationDetails getLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_LOCATION, 0);
        try {
            return new LocationDetails(preferences.getString(NAME, null),
                    (double) preferences.getFloat(LATITUDE, -1),
                    (double) preferences.getFloat(LONGITUDE, -1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void setLocation(Context context, LocationDetails details) {
        SharedPreferences preferences = context.getSharedPreferences(SP_LOCATION, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, details.getName());
        editor.putFloat(LATITUDE, (float) details.getLatitude());
        editor.putFloat(LONGITUDE, (float) details.getLongitude());
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_USER, 0);
        try {
            return new User(preferences.getString(NAME, null),
                    preferences.getLong(MOBILE, -1),
                    preferences.getString(ADDRESS, null),
                    preferences.getInt(PINCODE, -1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void setUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(SP_USER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, user.getName());
        editor.putLong(MOBILE, user.getMobile());
        editor.putString(ADDRESS, user.getAddress());
        editor.putInt(PINCODE, user.getPincode());
        editor.apply();
    }

    public static void setToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences(SP_USER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_USER, 0);
        return preferences.getString(TOKEN, null);
    }

    public static void clear(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_USER, 0).edit();
        edit.clear();
        edit.apply();
        edit = context.getSharedPreferences(SP_LOCATION, 0).edit();
        edit.clear();
        edit.apply();
    }
}
