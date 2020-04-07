package com.krishibazaar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Popups.SpinnerChooser;
import com.krishibazaar.R;
import com.krishibazaar.Utils.AssetsHandler;

import java.util.ArrayList;

public class SpinnerChooserAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<String> itemList, temp;
    private SpinnerChooser.ItemSelectedListener listener;
    private AssetsHandler assetsHandler;

    public SpinnerChooserAdapter(ArrayList<String> itemList, SpinnerChooser.ItemSelectedListener listener, Context context) {
        this.itemList = itemList;
        this.temp = itemList;
        this.listener = listener;
        assetsHandler = new AssetsHandler(context);
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
        holder.itemName.setText(itemList.get(position));
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasSubCategory = assetsHandler.hasSubCategory(itemList.get(position));
                listener.onItemSelected(temp.indexOf(itemList.get(position)), itemList.get(position), hasSubCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }


    public void filterList(ArrayList<String> filteredNames, ArrayList<String> tempList) {
        itemList = filteredNames;
        temp = tempList;
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
