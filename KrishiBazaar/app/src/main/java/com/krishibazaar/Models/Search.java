package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.CATEGORY;
import static com.krishibazaar.Utils.Constants.DESCRIPTION;
import static com.krishibazaar.Utils.Constants.DISTANCE;
import static com.krishibazaar.Utils.Constants.LATITUDE;
import static com.krishibazaar.Utils.Constants.LONGITUDE;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PAGE_OFFSET;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.QUANTITY;
import static com.krishibazaar.Utils.Constants.SEARCH;
import static com.krishibazaar.Utils.Constants.SEARCH_PHP;
import static com.krishibazaar.Utils.Constants.SIZE;

public class Search {
    public static class Query {
        public String search;
        public String category;
        public int size;
        public int pageOffset;
        public Double latitude;
        public Double longitude;

        public JSONObject getJSON() throws JSONException {
            JSONObject params = new JSONObject();
            params.put(SEARCH, search);
            if (category != null)
                params.put(CATEGORY, category);
            params.put(SIZE, size);
            params.put(PAGE_OFFSET, pageOffset);
            if (latitude != null && longitude != null) {
                params.put(LATITUDE, latitude);
                params.put(LONGITUDE, longitude);
            }
            return params;
        }

        public Query(String search, String category, int size, int pageOffset, Double latitude, Double longitude) {
            this.search = search;
            this.category = category;
            this.size = size;
            this.pageOffset = pageOffset;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public static class Response {
        public long product_id;
        public String name;
        public float quantity;
        public float price;
        public String description;
        public float distance;

        public Response(JSONObject object) throws JSONException {
            product_id = object.getLong(PRODUCT_ID);
            name = object.getString(NAME);
            quantity = (float) object.getDouble(QUANTITY);
            price = (float) object.getDouble(PRICE);
            description = object.getString(DESCRIPTION);
            distance = (float) object.getDouble(DISTANCE);
        }

        public long getProduct_id() {
            return product_id;
        }

        public String getName() {
            return name.trim();
        }

        public String getQuantity() {
            return quantity + " quintal";
        }

        public String getPrice() {
            return "Rs. " + price + "/q";
        }

        public String getDescription() {
            return description.trim();
        }

        public String getDistance() {
            return distance + "km away";
        }
    }
}
