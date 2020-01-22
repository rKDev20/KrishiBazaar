package com.rk.smsserver;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NotifyServer {
    public static void send(){
        StringRequest request = new StringRequest(Request.Method.POST, getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kid", kid);
                params.put("username", username);
                params.put("password", password);
                Log.d("abcd",kid+","+username+","+password);
                return params;
            }
        };
        queue.add(request);
    }
}
