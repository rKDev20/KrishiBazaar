package com.krishibazaar.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Models.Search;
import com.krishibazaar.Models.Transaction;
import com.krishibazaar.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.GEOCODE_PHP;
import static com.krishibazaar.Utils.Constants.GET_PROFILE_PHP;
import static com.krishibazaar.Utils.Constants.GET_TRANSACTION_PHP;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.SEARCH;
import static com.krishibazaar.Utils.Constants.SEARCH_PHP;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS;
import static com.krishibazaar.Utils.Constants.SUCCESS;
import static com.krishibazaar.Utils.Constants.TOKEN;
import static com.krishibazaar.Utils.Constants.UPDATE_PROFILE_PHP;

public class VolleyRequestMaker {
    private static RequestQueue queue;

    public static void getLocationByAddress(Context context, String search, final TaskFinishListener<LocationDetails> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(SEARCH, search);
            getLocation(context, params, listener);
        } catch (JSONException e) {
            listener.onError("Some error called");
        }
    }

    public static void getLocationByCoordinates(Context context, double latitude, double longitude, final TaskFinishListener<LocationDetails> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(LATITUDE, latitude);
            params.put(LONGITUDE, longitude);
            getLocation(context, params, listener);
        } catch (JSONException e) {
            listener.onError("Some error called");
        }
    }

    private static void getLocation(Context context, JSONObject params, final TaskFinishListener<LocationDetails> listener) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GEOCODE_PHP, params,
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

    public static void loadProducts(Context context, Search.Query query, final TaskFinishListener<List<Search.Response>> listener) {
        try {
            JSONObject params = query.getJSON();
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SEARCH_PHP, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("abcd", response.toString());
                                if (response.has(SUCCESS)) {
                                    final JSONArray details = response.getJSONArray(SUCCESS);
                                    List<Search.Response> resArr = new ArrayList<>();
                                    for (int i = 0; i < details.length(); ++i) {
                                        Search.Response res = new Search.Response(details.getJSONObject(i));
                                        resArr.add(res);
                                    }
                                    listener.onSuccess(resArr);
                                } else listener.onError("Nothing can be found");
                            } catch (JSONException e) {
                                listener.onError("Some error occurred");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.onError("Network error" + error.getMessage());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            listener.onError("Some error occurred");
        }
    }

    public static void getUserDetails(Context context, String token, final TaskFinishListener<User> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_PROFILE_PHP, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("abcd", response.toString());
                                if (response.has(SUCCESS)) {
                                    final User user = new User(response.getJSONObject(SUCCESS));
                                    listener.onSuccess(user);
                                } else listener.onError("User not found");
                            } catch (JSONException e) {
                                listener.onError(e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.onError("Network error" + error.getMessage());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }
    }

    public static void updateUserDetails(Context context, String token, final User user, final TaskFinishListener<User> listener) {
        try {
            final JSONObject params = user.getJSON();
            params.put(TOKEN, token);
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UPDATE_PROFILE_PHP, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("abcd", response.toString());
                                if (response.getInt(STATUS) == STATUS_SUCCESS) {
                                    listener.onSuccess(user);
                                } else listener.onError("User not found");
                            } catch (JSONException e) {
                                listener.onError(e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("abcd", params.toString());
                            listener.onError("Network error" + error.networkResponse + error.getMessage());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }
    }

    public static void getTransactions(Context context, String token, final TaskFinishListener<List<Transaction>> listener) {
        try {
            final JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_TRANSACTION_PHP, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("abcd", response.toString());
                                if (response.has(SUCCESS)) {
                                    List<Transaction> list = new ArrayList<>();
                                    JSONArray array = response.getJSONArray(SUCCESS);
                                    for (int i = 0; i < array.length(); i++) {
                                        Transaction transaction = new Transaction(array.getJSONObject(i));
                                        list.add(transaction);
                                    }
                                    listener.onSuccess(list);
                                } else listener.onError("User not found");
                            } catch (JSONException e) {
                                listener.onError(e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("abcd", params.toString());
                            listener.onError("Network error" + error.networkResponse + error.getMessage());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }
    }

    public interface TaskFinishListener<T> {

        void onSuccess(T response);

        void onError(String error);
    }
}
