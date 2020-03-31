package com.krishibazaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.krishibazaar.Models.SellProduct;
import com.krishibazaar.Popups.SpinnerChooser;
import com.krishibazaar.Utils.CustomButtonManager;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import libs.mjn.fieldset.FieldSetView;

import static android.view.View.GONE;

public class SellProductActivity extends Fragment {
    private Button catSpinner, scatSpinner;
    private EditText quantity, price, pinCode, description,name;
    View sellButton;
    private Context context;
    private SpinnerChooser.ItemSelectedListener categoryListener;
    private SpinnerChooser.ItemSelectedListener subCategoryListener;
    private int category = -1;
    private int subCategory = -1;
    private CustomButtonManager customSellButton;

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
        customSellButton=new CustomButtonManager(sellButton);
        customSellButton.setButtonText("Sell");
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.desc);
        categoryListener = new SpinnerChooser.ItemSelectedListener() {
            @Override
            public void onItemSelected(int i, String text, boolean hasSubcategory) {
                catSpinner.setText("Category : "+text);
                category = i;
                if (!hasSubcategory) {
                    scatSpinner.setVisibility(GONE);
                    subCategory = 0;
                } else {
                    scatSpinner.setVisibility(View.VISIBLE);
                    scatSpinner.setText("Select subcategory");
                    subCategory = -1;
                }
            }
        };
        subCategoryListener = new SpinnerChooser.ItemSelectedListener() {
            @Override
            public void onItemSelected(int i, String text, boolean ignore) {
                scatSpinner.setText("SubCategory : "+text);
                subCategory = i;
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();

        catSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SpinnerChooser().popup(context, true, 0,getFragmentManager(), categoryListener);
            }
        });
        scatSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category != -1)
                    new SpinnerChooser().popup(context, false, category,getFragmentManager(), subCategoryListener);
            }
        });
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (quantity.getText().toString().trim().length() != 0 &&
                        price.getText().toString().trim().length() != 0 &&
                        pinCode.getText().toString().length() == 6 &&
                        category != -1 &&
                        subCategory != -1 &&
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
                   customSellButton.setButtonText("Listing Your Product");
                   customSellButton.startProgressBar();
                   sellButton.setClickable(false);
                   sellButton.setFocusable(false);
                    VolleyRequestMaker.sellProduct(context, query, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                        @Override
                        public void onSuccess(Integer response) {
                            customSellButton.stopProgressBar();
                            customSellButton.setButtonText("Listed");
                            Toast.makeText(context, "Product On Sale !", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, ProductViewActivity.class);
                            intent.putExtra("productID", response);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String error) {
                            sellButton.setClickable(true);
                            sellButton.setFocusable(true);
                            customSellButton.setButtonText("Try Again");
                            customSellButton.stopProgressBar();
                        }
                    });
                }
                else
                    Toast.makeText(context, "Enter All Values !", Toast.LENGTH_LONG).show();
            }
        });
    }

}
