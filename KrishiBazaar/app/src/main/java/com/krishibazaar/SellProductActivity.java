package com.krishibazaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krishibazaar.Models.Category;
import com.krishibazaar.Models.SellProduct;
import com.krishibazaar.Models.SubCategory;
import com.krishibazaar.Popups.SpinnerChooser;
import com.krishibazaar.Utils.Constants;
import com.krishibazaar.Utils.LoadingButton;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import libs.mjn.fieldset.FieldSetView;

import static android.view.View.GONE;

public class SellProductActivity extends Fragment {
    private FieldSetView scatField;
    private TextView catSpinner, scatSpinner;
    private EditText quantity, price, pinCode, description, name;
    private Context context;
    private SpinnerChooser.ItemChooseListener<Category> categoryListener;
    private SpinnerChooser.ItemChooseListener<SubCategory> subCategoryListener;
    private int category = -1;
    private int subCategory = -1;
    private LoadingButton sellButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sell_products, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        quantity = view.findViewById(R.id.qty);
        price = view.findViewById(R.id.price);
        catSpinner = view.findViewById(R.id.cat);
        pinCode = view.findViewById(R.id.pin);
        scatSpinner = view.findViewById(R.id.subcat);
        sellButton = view.findViewById(R.id.otpbutton);
        sellButton.setButtonText("Sell");
        name = view.findViewById(R.id.name);
        scatField = view.findViewById(R.id.fieldSubCat);
        description = view.findViewById(R.id.desc);
        categoryListener = cat -> {
            catSpinner.setText(cat.getName());
            category = cat.getId();
            if (cat.hasSubcategory()) {
                scatField.setVisibility(View.VISIBLE);
                scatSpinner.setText("---SELECT---");
                subCategory = 0;
            } else {
                scatField.setVisibility(GONE);
                subCategory = -1;
            }
        };
        subCategoryListener = sub -> {
            scatSpinner.setText(sub.getName());
            subCategory = sub.getId();
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = requireActivity().getApplicationContext();

        catSpinner.setOnClickListener(v -> new SpinnerChooser().popup(context, true, 0, getFragmentManager(), categoryListener));
        scatSpinner.setOnClickListener(v -> {
            if (category != -1)
                new SpinnerChooser().popup(context, false, category, getFragmentManager(), subCategoryListener);
            else
                Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show();
        });
        sellButton.setOnClickListener(view -> {
            if (quantity.getText().toString().trim().length() != 0 &&
                    price.getText().toString().trim().length() != 0 &&
                    pinCode.getText().toString().length() == 6 &&
                    category != -1 &&
                    description.getText().toString().trim().length() != 0) {
                String token = SharedPreferenceManager.getToken(context);
                SellProduct query = new SellProduct(token,
                        category,
                        subCategory,
                        name.getText().toString(),
                        Float.parseFloat(quantity.getText().toString()),
                        Float.parseFloat(price.getText().toString()),
                        description.getText().toString(),
                        Integer.parseInt(pinCode.getText().toString()));
                sellButton.setButtonText("Listing Your Product");
                sellButton.startProgressBar();
                sellButton.setClickable(false);
                sellButton.setFocusable(false);
                VolleyRequestMaker.sellProduct(context, query, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        sellButton.stopProgressBar();
                        sellButton.setClickable(true);
                        sellButton.setFocusable(true);
                        sellButton.setButtonText("Sell");
                        Toast.makeText(context, "Product On Sale !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, ProductViewActivity.class);
                        intent.putExtra(Constants.PRODUCT_ID, response);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String error) {
                        sellButton.setClickable(true);
                        sellButton.setFocusable(true);
                        sellButton.setButtonText("Try Again");
                        sellButton.stopProgressBar();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(context, "Enter All Values !", Toast.LENGTH_LONG).show();
        });
    }

}
