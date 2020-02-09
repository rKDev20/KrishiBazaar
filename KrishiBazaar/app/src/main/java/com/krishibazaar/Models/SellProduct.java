package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.CATEGORY;
import static com.krishibazaar.Utils.Constants.DESCRIPTION;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PINCODE;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.QUANTITY;
import static com.krishibazaar.Utils.Constants.SUB_CATEGORY;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class SellProduct {
    private String token;
    private long category;
    private long subCategory;
    private String name;
    private float quantity;
    private float price;
    private String description;
    private int pincode;

    public SellProduct(String token, long category,long subCategory, String name, float quantity, float price, String description, int pincode) {
        this.token = token;
        this.category = category;
        this.subCategory=subCategory;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.pincode = pincode;
    }

    public JSONObject getJSON()throws JSONException{
        JSONObject params=new JSONObject();
        params.put(TOKEN,token);
        params.put(CATEGORY,category);
        params.put(SUB_CATEGORY,subCategory);
        params.put(NAME,name);
        params.put(QUANTITY,quantity);
        params.put(PRICE,price);
        params.put(DESCRIPTION,description);
        params.put(PINCODE,pincode);
        return params;
    }
}
