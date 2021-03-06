package com.krishibazaar.Utils;

import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.krishibazaar.Models.Authentication;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Models.NewUser;
import com.krishibazaar.Models.Product;
import com.krishibazaar.Models.Search;
import com.krishibazaar.Models.SellProduct;
import com.krishibazaar.Models.Transaction;
import com.krishibazaar.Models.TransactionDetails;
import com.krishibazaar.Models.User;
import com.krishibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.CHANGE_TRANSACTION_STATUS_PHP;
import static com.krishibazaar.Utils.Constants.DELETE_PRODUCT_PHP;
import static com.krishibazaar.Utils.Constants.FCM;
import static com.krishibazaar.Utils.Constants.GENERATE_OTP_PHP;
import static com.krishibazaar.Utils.Constants.GEOCODE_PHP;
import static com.krishibazaar.Utils.Constants.GET_PRODUCT_DETAILS_PHP;
import static com.krishibazaar.Utils.Constants.GET_PROFILE_PHP;
import static com.krishibazaar.Utils.Constants.GET_TRANSACTION_PHP;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LOGOUT_PHP;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.MAKE_TRANSACTION_PHP;
import static com.krishibazaar.Utils.Constants.MOBILE;
import static com.krishibazaar.Utils.Constants.OTP;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.REGISTER_PHP;
import static com.krishibazaar.Utils.Constants.SEARCH;
import static com.krishibazaar.Utils.Constants.SEARCH_PHP;
import static com.krishibazaar.Utils.Constants.SELL_PRODUCT_PHP;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.STATUS_ERROR;
import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS;
import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS_EXIST;
import static com.krishibazaar.Utils.Constants.STATUS_SUCCESS_NEW;
import static com.krishibazaar.Utils.Constants.SUCCESS;
import static com.krishibazaar.Utils.Constants.TOKEN;
import static com.krishibazaar.Utils.Constants.TRANSACTION_ID;
import static com.krishibazaar.Utils.Constants.VERIFY_OTP_PHP;

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

    private static void getLocation(final Context context, JSONObject params, final TaskFinishListener<LocationDetails> listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GEOCODE_PHP, params,
                response -> {
                    try {
                        if (response.has(SUCCESS)) {
                            final JSONObject details = response.getJSONObject(SUCCESS);
                            LocationDetails ld = new LocationDetails();
                            ld.setName(details.getString(ADDRESS));
                            ld.setLatitude(details.getDouble(LATITUDE));
                            ld.setLongitude(details.getDouble(LONGITUDE));
                            listener.onSuccess(ld);
                        } else listener.onError(context.getString(R.string.no_location));
                    } catch (JSONException e) {
                        listener.onError(context.getString(R.string.error_unknown));
                    }
                },
                error -> listener.onError(context.getString(R.string.error_network)));
        addQueue(context, request);
    }

    public static void loadProducts(final Context context, Search.Query query, final TaskFinishListener<List<Search.Response>> listener) {
        try {
            JSONObject params = query.getJSON();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SEARCH_PHP, params,
                    response -> {
                        try {
                            if (response.has(SUCCESS)) {
                                final JSONArray details = response.getJSONArray(SUCCESS);
                                List<Search.Response> resArr = new ArrayList<>();
                                for (int i = 0; i < details.length(); ++i) {
                                    Search.Response res = new Search.Response(details.getJSONObject(i));
                                    resArr.add(res);
                                }
                                listener.onSuccess(resArr);
                            } else
                                listener.onError(context.getString(R.string.error_nothing_found));
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, request);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void getUserDetails(Context context, String token, final TaskFinishListener<User> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_PROFILE_PHP, params,
                    response -> {
                        try {
                            if (response.has(SUCCESS)) {
                                final User user = new User(response.getJSONObject(SUCCESS));
                                listener.onSuccess(user);
                            } else listener.onError("User not found");
                        } catch (JSONException e) {
                            listener.onError(e.getMessage());
                        }
                    },
                    error -> listener.onError("Network error"));
            addQueue(context, request);
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }
    }

    public static void getTransactions(Context context, Transaction.Query query, final TaskFinishListener<List<Transaction.Response>> listener) {
        try {
            final JSONObject params = query.getJSON();
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_TRANSACTION_PHP, params,
                    response -> {
                        try {
                            if (response.has(SUCCESS)) {
                                List<Transaction.Response> list = new ArrayList<>();
                                JSONArray array = response.getJSONArray(SUCCESS);
                                for (int i = 0; i < array.length(); i++) {
                                    Transaction.Response transaction = new Transaction.Response(array.getJSONObject(i));
                                    list.add(transaction);
                                }
                                listener.onSuccess(list);
                            } else listener.onError("User not found");
                        } catch (JSONException e) {
                            listener.onError("Server error");
                        }
                    },
                    error -> listener.onError("Network error"));
            addQueue(context, request);
        } catch (JSONException e) {
            listener.onError("Unknown error");
        }
    }

    public static void sendOtp(final Context context, long mobile, final TaskFinishListener<Boolean> listener) {
        JSONObject params = new JSONObject();
        try {
            params.put(MOBILE, mobile);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GENERATE_OTP_PHP, params,
                    response -> {
                        try {
                            int status = response.getInt(STATUS);
                            if (status == STATUS_SUCCESS) {
                                listener.onSuccess(true);
                            } else
                                listener.onError("Failed to send otp");
                        } catch (JSONException e) {
                            listener.onError("Failed to send otp");
                        }
                    }, error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void verifyOtp(final Activity context, final long mobile, final int otp, final TaskFinishListener<Authentication> listener) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(context, task -> {
            if (task.isSuccessful()) {
                try {
                    JSONObject params = new JSONObject();
                    params.put(MOBILE, mobile);
                    params.put(OTP, otp);
                    params.put(FCM, task.getResult().getToken());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VERIFY_OTP_PHP, params,
                            response -> {
                                try {
                                    response = response.getJSONObject(SUCCESS);
                                    int status = response.getInt(STATUS);
                                    String token = response.getString(TOKEN);
                                    if (status == STATUS_SUCCESS_NEW || status == STATUS_SUCCESS_EXIST) {
                                        listener.onSuccess(new Authentication(status, token));
                                    } else
                                        listener.onError(context.getString(R.string.error_unknown));
                                } catch (JSONException e) {
                                    listener.onError("Otp mismatch");
                                }
                            },
                            error -> listener.onError(context.getString(R.string.error_network)));
                    addQueue(context, jsonObjectRequest);
                } catch (JSONException e) {
                    listener.onError(context.getString(R.string.error_unknown));
                }
            } else {
                listener.onError(context.getString(R.string.error_unknown));
            }
        });


    }

    public static void register(final Context context, NewUser user, final TaskFinishListener<Integer> listener) {
        try {
            JSONObject params = user.getJSON();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_PHP, params,
                    response -> {
                        try {
                            int status = response.getInt(STATUS);
                            if (status == STATUS_SUCCESS)
                                listener.onSuccess(status);
                            else if (status == STATUS_ERROR)
                                listener.onError(context.getString(R.string.error_network));
                            else listener.onError(context.getString(R.string.error_unknown));
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void getProductDetails(final Context context, Product.Query query, final TaskFinishListener<Product.Response> listener) {
        try {
            final JSONObject params = query.getJSON();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GET_PRODUCT_DETAILS_PHP, params,
                    response -> {
                        try {
                            Product.Response data = new Product.Response(response.getJSONObject(SUCCESS));
                            listener.onSuccess(data);
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void makeTransaction(final Context context, TransactionDetails.Query query, final TaskFinishListener<Integer> listener) {
        try {
            JSONObject params = query.getJSON();
            final int WRONG_PINCODE = -2;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MAKE_TRANSACTION_PHP, params,
                    response -> {
                        try {
                            if (response.getInt(STATUS) == STATUS_SUCCESS)
                                listener.onSuccess(0);
                            else if (response.getInt(STATUS) == WRONG_PINCODE)
                                listener.onError(context.getString(R.string.invalid_pincode));
                            else listener.onError(context.getString(R.string.error_unknown));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void sellProduct(final Context context, SellProduct query, final TaskFinishListener<Integer> listener) {
        try {
            JSONObject params = query.getJSON();
            final int WRONG_PINCODE = -2;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SELL_PRODUCT_PHP, params,
                    response -> {
                        try {
                            int status = response.getInt(STATUS);
                            if (status > 0)
                                listener.onSuccess(status);
                            else if (status == WRONG_PINCODE)
                                listener.onError(context.getString(R.string.invalid_pincode));
                            else
                                listener.onError(context.getString(R.string.error_unknown));
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void deleteProduct(final Context context, String token, int productId, final TaskFinishListener<Integer> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            params.put(PRODUCT_ID, productId);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DELETE_PRODUCT_PHP, params,
                    response -> {
                        try {
                            if (response.getInt(STATUS) == STATUS_SUCCESS)
                                listener.onSuccess(0);
                            else listener.onError(context.getString(R.string.error_unknown));
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void changeTransaction(final Context context, String token, int tranId, int status, final TaskFinishListener<Integer> listener) {
        try {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            params.put(TRANSACTION_ID, tranId);
            params.put(STATUS, status);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CHANGE_TRANSACTION_STATUS_PHP, params,
                    response -> {
                        try {
                            if (response.getInt(STATUS) == STATUS_SUCCESS)
                                listener.onSuccess(0);
                            else listener.onError(context.getString(R.string.error_unknown));
                        } catch (JSONException e) {
                            listener.onError(context.getString(R.string.error_unknown));
                        }
                    },
                    error -> listener.onError(context.getString(R.string.error_network)));
            addQueue(context, jsonObjectRequest);
        } catch (JSONException e) {
            listener.onError(context.getString(R.string.error_unknown));
        }
    }

    public static void logout(final Context context, String token) {
        try {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGOUT_PHP, params, null, null);
            addQueue(context, jsonObjectRequest);
        } catch (JSONException ignore) {
        }
    }

    private static void addQueue(Context context, JsonObjectRequest request) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void destroy() {
        if (queue != null)
            queue.cancelAll(request -> true);
    }

    public interface TaskFinishListener<T> {

        void onSuccess(T response);

        void onError(String error);
    }
}
