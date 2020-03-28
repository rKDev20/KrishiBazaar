package com.krishibazaar.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AssetsHandler
{
    static String JSONFromAssets;

    public AssetsHandler(Context context) {
        this.JSONFromAssets = readJSONFromAsset(context);
    }

    public static boolean hasSubCategory(String category)
    {
        try {
            JSONArray jsonArray = new JSONArray(JSONFromAssets);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("category").equals(category)) {
                    if(jsonObject.has("subCategory"))
                        return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static ArrayList<String> getSubcategoryArray(String selectedCat) {
        ArrayList<String> subCategory = new ArrayList<>();
        JSONArray arr = null;
        try {
            JSONArray jsonArray = new JSONArray(JSONFromAssets);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("category").equals(selectedCat)) {
                    if(!jsonObject.has("subCategory"))
                        return null;
                    arr = jsonObject.getJSONArray("subCategory");
                    for (int j = 0; j < arr.length(); j++) {
                        String js = arr.getString(j);
                        subCategory.add(js);
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subCategory;
    }
    public static ArrayList<String> getcategoryArray() {
        ArrayList<String> category = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(JSONFromAssets);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                category.add(jsonObject.getString("category"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return category;
    }

    private String readJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
