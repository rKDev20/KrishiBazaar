package com.krishibazaar.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krishibazaar.Models.LocationDetails;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.SEARCH;
import static com.krishibazaar.Utils.Constants.SUCCESS;

public class VolleyRequestMaker {
    private static RequestQueue queue;

    public static void getLocationByAddress(Context context, String search, final FetchLocationListener listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(SEARCH, search);
            getLocation(context, params, listener);
        } catch (JSONException e) {
            listener.onError("Some error called");
        }
    }

    public static void getLocationByCoordinates(Context context, double latitude, double longitude, final FetchLocationListener listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(LATITUDE, latitude);
            params.put(LONGITUDE, longitude);
            getLocation(context, params, listener);
        } catch (JSONException e) {
            listener.onError("Some error called");
        }
    }

    private static void getLocation(Context context, JSONObject params, final FetchLocationListener listener) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GEOCODE, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has(SUCCESS)) {
                                final JSONObject details = response.getJSONObject(SUCCESS);
                                LocationDetails ld = new LocationDetails();
                                ld.setName(details.getString(ADDRESS));
                                ld.setLatitude(details.getDouble(LATITUDE));
                                ld.setLongitude(details.getDouble(LONGITUDE));
                                listener.onSuccess(ld);
                            } else listener.onError("Could'nt find location");
                        } catch (JSONException e) {
                            listener.onError("Some error occured");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Network error" + error.networkResponse);
                    }
                });
        queue.add(request);
    }

    public interface FetchLocationListener {
        void onSuccess(LocationDetails details);
        void onError(String error);
    }
}
