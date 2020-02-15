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
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krishibazaar.Models.SellProduct;
import com.krishibazaar.Utils.VolleyRequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SellProductActivity extends Fragment {
    private Spinner catSpinner, scatSpinner;
    private EditText quantity, price, pinCode, description, name;
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
        catSpinner = view.findViewById(R.id.cat);
        pinCode = view.findViewById(R.id.pin);
        scatSpinner = view.findViewById(R.id.subcat);
        button = view.findViewById(R.id.otpbutton);
        name = view.findViewById(R.id.name);
        statusText = view.findViewById(R.id.pro_status);
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
                if (quantity.getText().toString().length() != 0 && price.getText().toString().length() != 0 && pinCode.getText().toString().length() == 6) {
                    SellProduct query = new SellProduct("kiit",
                            Integer.parseInt(catSpinner.getSelectedItem().toString()),
                            Integer.parseInt(scatSpinner.getSelectedItem().toString()),
                            name.getText().toString(),
                            Float.parseFloat(quantity.getText().toString()),
                            Float.parseFloat(price.getText().toString()),
                            description.getText().toString(),
                            Integer.parseInt(pinCode.getText().toString()));
                    VolleyRequestMaker.sellProduct(context, query, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                        @Override
                        public void onSuccess(Integer response) {
                            button.setVisibility(View.INVISIBLE);
                            statusText.setVisibility(View.VISIBLE);
                            statusText.setText("Product On Sale");

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
                else
                    Toast.makeText(context, "Enter All Values !", Toast.LENGTH_LONG).show();
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
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(context,
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
                ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(context,
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
        ArrayAdapter<String> scatAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, subCategory);
        scatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scatSpinner.setAdapter(scatAdapter);
    }
}
