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

import static com.rk.smsserver.Constants.AUTH;
import static com.rk.smsserver.Constants.AUTH_KEY;
import static com.rk.smsserver.Constants.HANDLE_SMS_BOT_PHP;
import static com.rk.smsserver.Constants.HANDLE_SMS_TRANSACTION_PHP;
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
            params.put(AUTH, AUTH_KEY);
            String url;
            if (SharedPreferenceManager.getChannel(context) == SharedPreferenceManager.CHANNEL_BOT)
                url = HANDLE_SMS_BOT_PHP;
            else
                url = HANDLE_SMS_TRANSACTION_PHP;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                SendSms.send(number, response.getString(TEXT));
                            } catch (JSONException e) {
                                SendSms.send(number, "Server error");
                                Log.e("abcd", "Error" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("abcd", "Error " + error);
                    SendSms.send(number, "Server unreachable. Please try again later");
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            Log.e("abcd", "Error" + e.getMessage());
        }
    }
}
