package com.krishibazaar.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Models.CategoryInterface;
import com.krishibazaar.Popups.SpinnerChooser;
import com.krishibazaar.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter<T extends CategoryInterface> extends RecyclerView.Adapter<ItemViewHolder> {
    private List<T> itemList;
    private List<T> origList;
    private SpinnerChooser.ItemChooseListener<T> listener;

    public CategoryAdapter(List<T> itemList, SpinnerChooser.ItemChooseListener listener) {
        this.itemList = itemList;
        this.listener = listener;
        origList=new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final T category = itemList.get(position);
        holder.itemName.setText(category.getName());
        holder.itemName.setOnClickListener(view -> listener.onItemSelected(category));
    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }


    public void filterList(String search) {
        itemList = new ArrayList<>();
        for (T e : origList)
            if (e.getName().toLowerCase().startsWith(search.toLowerCase()))
                itemList.add(e);
        notifyDataSetChanged();
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemName;

    ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = (TextView) itemView;
    }
}
