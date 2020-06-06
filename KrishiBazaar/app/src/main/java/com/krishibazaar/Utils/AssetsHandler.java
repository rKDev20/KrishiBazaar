package com.krishibazaar.Utils;

import android.content.Context;

import com.krishibazaar.Models.Category;
import com.krishibazaar.Models.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AssetsHandler {
    private static AssetsHandler instance;
    private HashMap<Integer, Category> categoryHashMap;

    public static AssetsHandler getInstance(Context context) {
        if (instance == null) instance = new AssetsHandler(context);
        return instance;
    }

    public AssetsHandler(Context context) {
        categoryHashMap = readJSONFromAsset(context);
    }

    public List<SubCategory> getSubcategoryArray(int categoryId) {
        if (categoryHashMap.containsKey(categoryId))
            return Objects.requireNonNull(categoryHashMap.get(categoryId)).getSubcategoryList();
        else return null;
    }

    public List<Category> getCategoryArray() {
        return new ArrayList<>(categoryHashMap.values());
    }

    private HashMap<Integer, Category> readJSONFromAsset(Context context) {
        HashMap<Integer, Category> hashMap = new HashMap<>();
        String json;
        try {
            //Read categories
            InputStream is = context.getResources().getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt("category_id");
                hashMap.put(id, new Category(object.getString("name"), id));
            }
            //Read subCategories
            is = context.getResources().getAssets().open("sub_categories.json");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                int categoryId = object.getInt("category_id");
                Category category = hashMap.get(categoryId);
                assert category != null;
                category.addSubcategory(object.getInt("sub_id"), object.getString("name"));
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return hashMap;
    }
}
