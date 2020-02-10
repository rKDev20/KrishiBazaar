package com.krishibazaar.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.BASE_ADDRESS;
import static com.krishibazaar.Utils.Constants.BUYER_DETAILS;
import static com.krishibazaar.Utils.Constants.CATEGORY;
import static com.krishibazaar.Utils.Constants.DESCRIPTION;
import static com.krishibazaar.Utils.Constants.DISTANCE;
import static com.krishibazaar.Utils.Constants.FARMER_MOBILE;
import static com.krishibazaar.Utils.Constants.IMAGE_URL;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.OWNED;
import static com.krishibazaar.Utils.Constants.PINCODE;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.QUANTITY;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.SUB_CATEGORY;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class Product {
    public static class Query {
        private String token;
        private int proId;
        private Double latitude;
        private Double longitude;

        public JSONObject getJSON() throws JSONException {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            params.put(PRODUCT_ID, proId);
            params.put(LATITUDE, latitude);
            params.put(LONGITUDE, longitude);
            return params;
        }

        public Query(String token, int proId) {
            this.token = token;
            this.proId = proId;
        }

        public Query(String token, int proID, Double latitude, Double longitude) {
            this.token = token;
            this.proId = proID;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public static class Response {
        private String image_url;
        private String name;
        private float quantity;
        private float price;
        private String description;
        private Float distance;
        private int pincode;
        private double latitude;
        private double longitude;

        //TODO
        private String category;
        private String subCategory;
        int status;
        long farmerMobile;
        List<BuyerDetails> buyers;

        public Response(JSONObject object) throws JSONException {
            image_url = object.getString(IMAGE_URL);
            name = object.getString(NAME);
            if (object.has(CATEGORY))
                category = object.getString(CATEGORY);
            if (object.has(SUB_CATEGORY))
                subCategory= object.getString(SUB_CATEGORY);
            pincode=object.getInt(PINCODE);
            latitude=object.getDouble(LATITUDE);
            longitude=object.getDouble(LONGITUDE);
            quantity = (float) object.getDouble(QUANTITY);
            price = (float) object.getDouble(PRICE);
            description = object.getString(DESCRIPTION);
            if (object.has(DISTANCE))
                distance = (float) object.getDouble(DISTANCE);
            else distance = null;
            status = object.getInt(STATUS);
            if (status == ACCEPTED)
                farmerMobile = object.getLong(FARMER_MOBILE);
            if (status == OWNED) {
                buyers = new ArrayList<>();
                JSONArray array = object.getJSONArray(BUYER_DETAILS);
                for (int i = 0; i < array.length(); i++) {
                    buyers.add(new BuyerDetails(array.getJSONObject(i)));
                }
            } else buyers = null;
        }

        public String getImageUrl() {
            return BASE_ADDRESS + image_url;
        }

        public String getName() {
            return name.trim();
        }

        public String getQuantity() {
            return quantity + " quintal";
        }

        public String getPrice() {
            return "₹" + price;
        }

        public String getDescription() {
            return description.trim();
        }

        public String getDistance() {
            return distance + "km away";
        }

        public int getPincode() {
            return pincode;
        }

        public int getStatus() {
            return status;
        }

        public String getCategory() {
            return category;
        }

        public long getFarmerMobile() {
            return farmerMobile;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
