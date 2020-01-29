package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.ADDRESS;
import static com.krishibazaar.Utils.Constants.MOBILE;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PINCODE;

public class User {

    private String name;
    private long mobile;
    private String address;
    private int pincode;

    public User(JSONObject user) throws JSONException {
        name = user.getString(NAME);
        mobile = user.getLong(MOBILE);
        address = user.getString(ADDRESS);
        pincode = user.getInt(PINCODE);
    }

    public JSONObject getJSON() throws JSONException{
        JSONObject object = new JSONObject();
        object.put(NAME,name);
        object.put(MOBILE,mobile);
        object.put(ADDRESS,address);
        object.put(PINCODE,pincode);
        return object;
    }
    public User(String name, long mobile, String address, int pincode) throws IllegalArgumentException{
        if (name==null||mobile==-1||address==null||pincode==-1)
            throw new IllegalArgumentException();
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public long getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public int getPincode() {
        return pincode;
    }
}
