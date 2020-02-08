package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PINCODE;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class NewUser {
    private String name;
    private String token;
    private String address;
    private int pincode;

    public JSONObject getJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(NAME, name);
        object.put(TOKEN, token);
        object.put(ADDRESS, address);
        object.put(PINCODE, pincode);
        return object;
    }

    public NewUser(String name, String token, String address, int pincode) throws IllegalArgumentException {
        if (name == null || token == null || pincode == -1)
            throw new IllegalArgumentException();
        this.name = name;
        this.token = token;
        this.address = address;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getPincode() {
        return pincode;
    }
}
