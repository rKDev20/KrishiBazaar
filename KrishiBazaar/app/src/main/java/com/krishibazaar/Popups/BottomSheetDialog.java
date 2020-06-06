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
import com.krishibazaar.Adapters.CategoryAdapter;
import com.krishibazaar.Models.Category;
import com.krishibazaar.Models.CategoryInterface;
import com.krishibazaar.Models.SubCategory;
import com.krishibazaar.R;
import com.krishibazaar.Utils.AssetsHandler;

import java.util.List;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private SpinnerChooser.ItemChooseListener listener;
    private boolean isCategory;
    private Context context;
    private int category;
    private CategoryAdapter chooser;
    private List<CategoryInterface> itemList;

    BottomSheetDialog(boolean isCategory, Context context, int category, SpinnerChooser.ItemChooseListener listener) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (isCategory)
            chooser = new CategoryAdapter<>(AssetsHandler.getInstance(context).getCategoryArray(), listener);
        else
            chooser = new CategoryAdapter<>(AssetsHandler.getInstance(context).getSubcategoryArray(category), listener);
        recyclerView.setAdapter(chooser);
        recyclerView.setOnClickListener(view1 -> dismiss());
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                chooser.filterList(editable.toString());
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
}
