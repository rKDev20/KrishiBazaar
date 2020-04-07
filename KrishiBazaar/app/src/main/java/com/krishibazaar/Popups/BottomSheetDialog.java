package com.krishibazaar.Popups;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krishibazaar.Adapters.SpinnerChooserAdapter;
import com.krishibazaar.R;
import com.krishibazaar.Utils.AssetsHandler;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private SpinnerChooser.ItemSelectedListener listener;
    private boolean isCategory;
    private Context context;
    private int category;
    private SpinnerChooserAdapter chooser;
    private ArrayList<String> itemList;

    BottomSheetDialog(boolean isCategory, Context context, int category, SpinnerChooser.ItemSelectedListener listener) {
        this.isCategory = isCategory;
        this.context = context;
        this.category = category;
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheeetDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_category_chooser, container, false);
        initHeader(view);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        EditText searchItem = view.findViewById(R.id.search_item);
        ArrayList<String> categoryList = new AssetsHandler(context).getCategoryArray();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (isCategory) {
            itemList = categoryList;
            chooser = new SpinnerChooserAdapter(itemList, listener, context);
            recyclerView.setAdapter(chooser);
        } else {
            itemList = new AssetsHandler(context).getSubcategoryArray(categoryList.get(category));
            chooser = new SpinnerChooserAdapter(itemList, listener, context);
            recyclerView.setAdapter(chooser);
        }
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        return view;
    }

    private void initHeader(View view) {
        if (isCategory)
            ((TextView) view.findViewById(R.id.textView8)).setText("Select category");
        else
            ((TextView) view.findViewById(R.id.textView8)).setText("Select sub-category");
    }

    private void filter(String text) {
        ArrayList<String> filteredNames = new ArrayList<>();
        for (String s : itemList)
            if (s.toLowerCase().startsWith(text.toLowerCase()))
                filteredNames.add(s);
        chooser.filterList(filteredNames, itemList);
    }
}
