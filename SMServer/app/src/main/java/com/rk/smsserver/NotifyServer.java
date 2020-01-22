package com.rk.smsserver;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;
import static com.rk.smsserver.Constants.MOBILE;
import static com.rk.smsserver.Constants.TEXT;

public class NotifyServer {
    public static void send(Context context, final String number, final String text) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://" + getWifiIpAddress(context) + "/KrishiBazaar/handleSms.php";
            Log.d("abcd", url);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //SendSms.send(number, "Hello Madarchod");
                            onResult(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(MOBILE, number);
                    params.put(TEXT, text);
                    return params;
                }
            };
            queue.add(request);
        } catch (UnknownHostException e) {
            onError(e.getMessage());
        }
    }

    private static void onResult(String response) {
        Log.d("abcd-result", response);
    }

    private static void onError(String message) {
        Log.d("abcd-error", message);
    }

    public static String getWifiIpAddress(Context context) throws UnknownHostException {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        int ipAddress = info.getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }
        final byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
        final InetAddress address;
        address = InetAddress.getByAddress(bytes);
        String add = address.getHostAddress();
        add=add.substring(0,add.lastIndexOf('.'))+".1";
        return add;
    }
}
