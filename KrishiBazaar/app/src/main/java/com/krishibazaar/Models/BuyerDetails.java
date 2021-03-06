package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.DISTANCE;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.TIMESTAMP;
import static com.krishibazaar.Utils.Constants.TRANSACTION_ID;

public class BuyerDetails {
    private int tranId;
    private String name;
    private Float price;//optional
    private float distance;
    private long timestamp;
    private int status;

    BuyerDetails(JSONObject object) throws JSONException {
        tranId = object.getInt(TRANSACTION_ID);
        name = object.getString(NAME);
        if (object.has(PRICE))
            price = (float) object.getDouble(PRICE);
        timestamp = object.getLong(TIMESTAMP);
        distance = (float) object.getDouble(DISTANCE);
        status = object.getInt(STATUS);
    }

    public int getTranId() {
        return tranId;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public float getDistance() {
        return distance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }
}
