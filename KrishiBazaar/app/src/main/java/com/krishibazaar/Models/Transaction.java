package com.krishibazaar.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.krishibazaar.Utils.Constants.PAGE_OFFSET;
import static com.krishibazaar.Utils.Constants.SIZE;
import static com.krishibazaar.Utils.Constants.STATUS;
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
            params.put(PAGE_OFFSET,pageOffset);
            params.put(SIZE,size);
            return params;
        }
    }

    public static class Response extends Search.Response {
        private String status;

        public Response(JSONObject object) throws JSONException {
            super(object);
            status = object.getString(STATUS);
        }

        public String getStatus() {
            return status;
        }
    }
}
