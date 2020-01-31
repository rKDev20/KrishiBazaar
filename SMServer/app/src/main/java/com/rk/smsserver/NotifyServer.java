package com.rk.smsserver;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.rk.smsserver.Constants.HANDLE_SMS_PHP;
import static com.rk.smsserver.Constants.MOBILE;
import static com.rk.smsserver.Constants.TEXT;

class NotifyServer {
    private static RequestQueue queue;

    static void send(Context context, final String number, final String text) {
        try {
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            JSONObject params = new JSONObject();
            params.put(MOBILE, number);
            params.put(TEXT, text);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, HANDLE_SMS_PHP, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                SendSms.send(number,response.getString(TEXT));
                            } catch (JSONException e) {
                                Log.e("abcd", "Error" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("abcd","Error " +error);
                    SendSms.send(number,"Server unreachable. Please try again later");
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            Log.e("abcd", "Error" + e.getMessage());
        }
    }

    public interface TaskFinishListener {
        void onReceive(String response);
    }
}
