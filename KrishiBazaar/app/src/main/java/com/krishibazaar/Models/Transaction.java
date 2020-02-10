package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.krishibazaar.Utils.Constants.DISTANCE;
import static com.krishibazaar.Utils.Constants.NAME;
import static com.krishibazaar.Utils.Constants.PAGE_OFFSET;
import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.QUANTITY;
import static com.krishibazaar.Utils.Constants.SIZE;
import static com.krishibazaar.Utils.Constants.STATUS;
import static com.krishibazaar.Utils.Constants.TIMESTAMP;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class Transaction {

    public static class Query {
        String token;
        int pageOffset;
        int size;

        public Query(String token, int pageOffset, int size) {
            this.token = token;
            this.pageOffset = pageOffset;
            this.size = size;
        }

        public JSONObject getJSON() throws JSONException {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            params.put(PAGE_OFFSET, pageOffset);
            params.put(SIZE, size);
            return params;
        }
    }

    public static class Response {
        private long product_id;
        private String name;
        private float quantity;
        private float price;
        private Float distance;
        private int status;
        private long timestamp;

        public Response(JSONObject object) throws JSONException {
            product_id = object.getLong(PRODUCT_ID);
            name = object.getString(NAME);
            quantity = (float) object.getDouble(QUANTITY);
            price = (float) object.getDouble(PRICE);
            if (object.has(DISTANCE))
                distance = (float) object.getDouble(DISTANCE);
            else distance = null;
            timestamp = object.getLong(TIMESTAMP);
            status = object.getInt(STATUS);
        }

        public String getTime() {
            Date now = new Date();
            long diff = (now.getTime()/1000 - timestamp);
            if (diff < 60)
                return diff + " seconds ago";
            else if (diff < 3600)
                return diff / 60 + " minutes ago";
            else if (diff < 86400)
                return diff / 3600 + " hours ago";
            else
                return diff / 86400 + " days ago";
        }

        public long getProductId() {
            return product_id;
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

        public String getDistance() {
            if (distance != null)
                return distance + "km away";
            else return null;
        }

        public int getStatus() {
            return status;
        }
    }
}
