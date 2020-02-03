package com.krishibazaar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SellProductActivity extends Fragment {
    private Spinner catSpinner, scatSpinner;
    private EditText quantity, price, pinCode, description;
    private Button button;
    private TextView statusText;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sell_products, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        quantity = view.findViewById(R.id.qty);
        price = view.findViewById(R.id.prc);
        pinCode = view.findViewById(R.id.pin);
        button = view.findViewById(R.id.otpbutton);
        statusText = view.findViewById(R.id.status);
        description = view.findViewById(R.id.desc);
        statusText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();

        getCategoryToSpinner();
        getSubCategoryToSpinner();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity.getText().toString().length() != 0 &&
                        price.getText().toString().length() != 0 &&
                        pinCode.getText().toString().length() == 6) {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("category", catSpinner.getSelectedItem().toString());
                        params.put("subCategory", scatSpinner.getSelectedItem().toString());
                        params.put("quantity", quantity.getText().toString());
                        params.put("price", price.getText().toString());
                        params.put("pinCode", pinCode.getText().toString());
                        params.put("description", description.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        if (status.equals("invalid_user"))
                                            Toast.makeText(context, "Error !", Toast.LENGTH_LONG).show();
                                        else {
                                            button.setVisibility(View.INVISIBLE);
                                            statusText.setVisibility(View.VISIBLE);
                                            statusText.setText("Product On Sale(PId:" + status + ")");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Error !", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Error !", Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(SellProducts.this);
                    requestQueue.add(jsonObjectRequest);
                } else
                    Toast.makeText(SellProducts.this, "Enter All Values !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String readJSONFromAsset() {
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

    public ArrayList<String> getCategoryArray(String selectedCat) {
        ArrayList<String> subCategory = new ArrayList<>();
        JSONArray arr = null;
        try {
            JSONArray jsonArray = new JSONArray(readJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("category").equals(selectedCat)) {
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

    public void getCategoryToSpinner() {
        ArrayList<String> category = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(readJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                category.add(jsonObject.getString("category"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catSpinner = view.findViewById(R.id.cat);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(SellProductsActivity.this,
                android.R.layout.simple_list_item_1, category);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(catAdapter);
    }

    public void getSubCategoryToSpinner() {
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCat = catSpinner.getSelectedItem().toString();
                ArrayList<String> subCategory = getCategoryArray(selectedCat);
                scatSpinner = view.findViewById(R.id.subcat);
                ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(SellProductsActivity.this,
                        android.R.layout.simple_list_item_1, subCategory);
                scatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                scatSpinner.setAdapter(scatAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String selectedCat = catSpinner.getSelectedItem().toString();
        ArrayList<String> subCategory = getCategoryArray(selectedCat);
        scatSpinner = view.findViewById(R.id.subcat);
        ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(SellProductsActivity.this,
                android.R.layout.simple_list_item_1, subCategory);
        scatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scatSpinner.setAdapter(scatAdapter);
    }
}
