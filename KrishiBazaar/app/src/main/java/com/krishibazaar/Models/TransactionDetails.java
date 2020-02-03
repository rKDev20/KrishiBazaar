package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.PRICE;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class TransactionDetails {
    public static class Query {
        private String token;
        private int proId;
        private float negPrice;

        public JSONObject getJSON() throws JSONException {
            JSONObject params = new JSONObject();
            params.put(TOKEN, token);
            params.put(PRODUCT_ID,proId);
            params.put(PRICE,negPrice);
            return params;
        }

        public Query(String token, int proId, float negPrice) {
            this.token = token;
            this.proId = proId;
            this.negPrice = negPrice;
        }
    }
}
