package com.krishibazaar.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyRequestMaker {
    private static RequestQueue queue;

    public static void makeRequest(Context context, JSONObject params, final RequestListener listener) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GEOCODE, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Network error"+error.networkResponse);
                    }
                });
        queue.add(request);
    }
    public interface RequestListener{
        void onResponse(JSONObject response);
        void onError(String error);
    }
}
