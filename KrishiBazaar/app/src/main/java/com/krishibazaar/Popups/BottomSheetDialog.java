package com.krishibazaar.Popups;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krishibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment{
    private SpinnerChooser.ItemSelectedListener listener;
    RecyclerView recyclerView;
    boolean isCategory;
    Context context;
    int category;

    public BottomSheetDialog(boolean isCategory, Context context, int category,SpinnerChooser.ItemSelectedListener listener) {
        this.isCategory = isCategory;
        this.context = context;
        this.category = category;
        this.listener=listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        String JSONFromAssets=readJSONFromAsset(context);
        ArrayList<String> categoryList=getcategoryArray(JSONFromAssets);
        ArrayList<String> subCategoryList=null;
        if(isCategory)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            subCategoryList=getSubcategoryArray(JSONFromAssets,categoryList.get(category));
            if(subCategoryList==null)
                recyclerView.setAdapter(new SpinnerChooser(categoryList,listener,false));
            else
                recyclerView.setAdapter(new SpinnerChooser(categoryList,listener,true));
        }
        else
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            subCategoryList=getSubcategoryArray(JSONFromAssets,categoryList.get(category));
            recyclerView.setAdapter(new SpinnerChooser(subCategoryList,listener,false));
        }
        return view;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            listener=(SpinnerChooser.ItemSelectedListener) context;
//        }
//        catch(ClassCastException e)
//        {
//            throw new ClassCastException(context.toString()+"must implement BottomSheetListener");
//        }
//    }

    public static String readJSONFromAsset(Context context) {
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

    public static ArrayList<String> getSubcategoryArray(String JSONFromAsset,String selectedCat) {
        ArrayList<String> subCategory = new ArrayList<>();
        JSONArray arr = null;
        try {
            JSONArray jsonArray = new JSONArray(JSONFromAsset);
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

    public static ArrayList<String> getcategoryArray(String JSONFromAsset) {
        ArrayList<String> category = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(JSONFromAsset);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                category.add(jsonObject.getString("category"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return category;
    }
}
