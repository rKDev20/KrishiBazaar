package com.krishibazaar.Popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.krishibazaar.R;
import java.util.ArrayList;

public class SpinnerChooser extends RecyclerView.Adapter<SpinnerChooser.ItemViewHolder> {
    ArrayList<String> itemList;
    ItemSelectedListener listener;
    boolean hasSubCategory;

    public SpinnerChooser(ArrayList<String> itemList,ItemSelectedListener listener,boolean hasSubCategory) {
        this.itemList = itemList;
        this.listener=listener;
        this.hasSubCategory=hasSubCategory;
    }

    public static void popup(Context context, boolean isCategory, int category, FragmentManager manager, ItemSelectedListener listener)
    {
        BottomSheetDialog dialog=new BottomSheetDialog(isCategory,context,category,listener);
        dialog.show(manager,"BottomSheet");
    }

    public interface ItemSelectedListener {
        void onItemSelected(int i, String text, boolean hasSubcategory);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_item_list,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position)
    {
        holder.itemName.setText(itemList.get(position));
        holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        if(itemList==null)
            return 0;
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
        }
        public void bind(final int position, final ItemSelectedListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemSelected(position,itemList.get(position),hasSubCategory);
                }
            });
        }
    }

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
