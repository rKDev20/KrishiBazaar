package com.krishibazaar.Models;

import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.STATUS;

public class Transaction {

    class Query {
        String token;
        int pageOffset;
        int size;

        public Query(String token, int pageOffset, int size) {
            this.token = token;
            this.pageOffset = pageOffset;
            this.size = size;
        }

        public void getJSON() {

        }
    }

    class Response extends Search.Response {
        private String status;

        public Response(JSONObject object) throws JSONException {
            super(object);
            status = object.getString(STATUS);
        }
    }
}
