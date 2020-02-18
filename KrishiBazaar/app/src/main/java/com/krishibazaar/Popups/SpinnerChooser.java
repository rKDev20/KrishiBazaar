package com.krishibazaar.Popups;

import android.content.Context;

public class SpinnerChooser {
    public static void popup(Context context, boolean isCategory, int category, ItemSelectedListener listener) {

    }

    public interface ItemSelectedListener {
        void onItemSelected(int i, String text, boolean hasSubcategory);
    }


//    public String readJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = context.getAssets().open("products.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, StandardCharsets.UTF_8);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
//
//    public ArrayList<String> getCategoryArray(String selectedCat) {
//        ArrayList<String> subCategory = new ArrayList<>();
//        JSONArray arr = null;
//        try {
//            JSONArray jsonArray = new JSONArray(readJSONFromAsset());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                if (jsonObject.getString("category").equals(selectedCat)) {
//                    arr = jsonObject.getJSONArray("subCategory");
//                    for (int j = 0; j < arr.length(); j++) {
//                        String js = arr.getString(j);
//                        subCategory.add(js);
//                    }
//                    break;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return subCategory;
//    }
//
//    public void getCategoryToSpinner() {
//        ArrayList<String> category = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(readJSONFromAsset());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                category.add(jsonObject.getString("category"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(context,
//                android.R.layout.simple_list_item_1, category);
//        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        catSpinner.setAdapter(catAdapter);
//    }
//
//    public void getSubCategoryToSpinner() {
//        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedCat = catSpinner.getSelectedItem().toString();
//                ArrayList<String> subCategory = getCategoryArray(selectedCat);
//                ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(context,
//                        android.R.layout.simple_list_item_1, subCategory);
//                scatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                scatSpinner.setAdapter(scatAdapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        String selectedCat = catSpinner.getSelectedItem().toString();
//        ArrayList<String> subCategory = getCategoryArray(selectedCat);
//        ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(context,
//                android.R.layout.simple_list_item_1, subCategory);
//        scatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        scatSpinner.setAdapter(scatAdapter);
//    }
}
