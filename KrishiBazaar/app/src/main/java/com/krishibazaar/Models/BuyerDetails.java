package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PINCODE;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.TIMESTAMP;
import static com.krishibazaar.Utils.Constants.TRANSACTION_ID;

class BuyerDetails {
    private int tranId;
    private String name;
    private float price;
    private int pincode;
    private long timestamp;
    private int status;

    BuyerDetails(JSONObject object) throws JSONException {
        tranId = object.getInt(TRANSACTION_ID);
        name = object.getString(NAME);
        price = (float) object.getDouble(PRICE);
        timestamp = object.getLong(TIMESTAMP);
        pincode = object.getInt(PINCODE);
        status = object.getInt(STATUS);
    }

    public int getTranId() {
        return tranId;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getPincode() {
        return pincode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }
}