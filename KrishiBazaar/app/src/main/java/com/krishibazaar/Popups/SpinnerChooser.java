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
import com.krishibazaar.Utils.AssetsHandler;

import java.util.ArrayList;

public class SpinnerChooser extends RecyclerView.Adapter<SpinnerChooser.ItemViewHolder> {
    ArrayList<String> itemList,temp;
    ItemSelectedListener listener;
    Context context;
    BottomSheetDialog dialog;

    public SpinnerChooser() {
    }

    public SpinnerChooser(ArrayList<String> itemList, ItemSelectedListener listener, Context context) {
        this.itemList = itemList;
        this.temp=itemList;
        this.listener = listener;
        this.context = context;
    }

    public void popup(final Context context, boolean isCategory, int category, FragmentManager manager, final ItemSelectedListener listener) {
        ItemSelectedListener l = new ItemSelectedListener() {
            @Override
            public void onItemSelected(int i, String text, boolean hasSubcategory) {
                listener.onItemSelected(i, text, hasSubcategory);
                dialog.dismiss();
            }
        };
        dialog = new BottomSheetDialog(isCategory, context, category, l);
        dialog.show(manager, "BottomSheet");
    }

    public interface ItemSelectedListener {
        void onItemSelected(int i, String text, boolean hasSubcategory);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.itemName.setText(itemList.get(position));
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
        }

        public void bind(final int position, final ItemSelectedListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean hasSubCategory = new AssetsHandler(context).hasSubCategory(itemList.get(position));
                    listener.onItemSelected(temp.indexOf(itemList.get(position)), itemList.get(position), hasSubCategory);
                }
            });
        }
    }
    public void filterList(ArrayList<String> filteredNames,ArrayList<String> tempList) {
        itemList = filteredNames;
        temp=tempList;
        notifyDataSetChanged();
    }
}
