package com.krishibazaar.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Models.Transaction;
import com.krishibazaar.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private int VIEW_TYPE_DATA = 0;
    final private int VIEW_TYPE_LOADING = 1;
    private List<Transaction.Response> data;
    private boolean isMaxLimitReached;
    private boolean isReloadFailed;
    private ReloadListener listener;

    public TransactionAdapter(List<Transaction.Response> data, ReloadListener listener) {
        this.data = data;
        isMaxLimitReached = false;
        isReloadFailed = false;
        this.listener = listener;
    }


    public void addData(List<Transaction.Response> data) {
        this.data.addAll(data);
    }

    public void reset() {
        data.clear();
        isMaxLimitReached = false;
        isReloadFailed = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA)
            return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction, parent, false));
        else
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder) {
            TransactionViewHolder viewHolder = (TransactionViewHolder) holder;
            viewHolder.name.setText(data.get(position).name);
            viewHolder.description.setText(data.get(position).description);
            viewHolder.quantity.setText(data.get(position).getQuantity());
            viewHolder.price.setText(data.get(position).getPrice());
            viewHolder.status.setText(data.get(position).getStatus());
            viewHolder.location.setText(data.get(position).getDistance());
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            Log.d("abcd", "here");
            if (isReloadFailed)
                viewHolder.setRefresh(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onReload();
                    }
                });
            else viewHolder.setProgressBar();
        }
    }

    @Override
    public int getItemCount() {
        if (isMaxLimitReached)
            return data.size();
        else return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isMaxLimitReached)
            return VIEW_TYPE_DATA;
        else {
            if (position == data.size())
                return VIEW_TYPE_LOADING;
            else return VIEW_TYPE_DATA;
        }
    }

    public void setMaxLimitReached(boolean b) {
        isMaxLimitReached = b;
    }

    public void setReloadFailed(boolean isReloadFailed) {
        this.isReloadFailed = isReloadFailed;
        notifyDataSetChanged();
    }

    public interface ReloadListener {
        void onReload();
    }
}

class TransactionViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView description;
    TextView quantity;
    TextView price;
    TextView location;
    TextView status;

    TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
        location = itemView.findViewById(R.id.location);
        status = itemView.findViewById(R.id.status);
    }
}


