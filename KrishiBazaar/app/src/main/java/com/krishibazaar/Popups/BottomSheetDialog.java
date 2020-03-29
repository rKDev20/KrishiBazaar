package com.krishibazaar.Popups;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krishibazaar.R;
import com.krishibazaar.Utils.AssetsHandler;
import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private SpinnerChooser.ItemSelectedListener listener;
    RecyclerView recyclerView;
    boolean isCategory;
    Context context;
    int category;
    SpinnerChooser chooser;
    ArrayList<String> itemList;

    public BottomSheetDialog(boolean isCategory, Context context, int category, SpinnerChooser.ItemSelectedListener listener) {
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
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        ArrayList<String> categoryList = new AssetsHandler(context).getcategoryArray();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (isCategory)
        {
            itemList=categoryList;
            chooser = new SpinnerChooser(itemList, listener, context);
            recyclerView.setAdapter(chooser);
        }
        else
        {
            itemList= new AssetsHandler(context).getSubcategoryArray(categoryList.get(category));
            chooser = new SpinnerChooser(itemList, listener, context);
            recyclerView.setAdapter(chooser);
        }
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        EditText searchItem = view.findViewById(R.id.search_item);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filter(editable.toString());
            }
        });
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
    private void filter(String text)
    {
        ArrayList<String> filteredNames = new ArrayList<>();
        for (String s : itemList)
            if (s.toLowerCase().startsWith(text.toLowerCase()))
                filteredNames.add(s);
        chooser.filterList(filteredNames);
    }
}
