package com.krishibazaar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private ReloadListener reloadListener;
    private OnClickListener clickListener;

    private String[] statusText;
    private int[] statusColor;

    public TransactionAdapter(Context context, List<Transaction.Response> data, ReloadListener reloadListener, OnClickListener clickListener) {
        this.data = data;
        isMaxLimitReached = false;
        isReloadFailed = false;
        this.reloadListener = reloadListener;
        this.clickListener = clickListener;
        statusColor = context.getResources().getIntArray(R.array.status_color);
        statusText = context.getResources().getStringArray(R.array.status_array);
    }

    public void addData(List<Transaction.Response> data) {
        this.data.addAll(data);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TransactionViewHolder) {
            TransactionViewHolder viewHolder = (TransactionViewHolder) holder;
            final Transaction.Response response = data.get(position);
            viewHolder.name.setText(response.getName());
            viewHolder.quantity.setText(response.getQuantity());
            viewHolder.price.setText(response.getPrice());
            viewHolder.status.setText(statusText[response.getStatus()]);
            viewHolder.status.setBackgroundColor(statusColor[response.getStatus()]);
            viewHolder.timestamp.setText(response.getTime());
            viewHolder.itemView.setOnClickListener(v -> clickListener.onClick(response.getProductId()));
            if (response.getDistance() != null) {
                viewHolder.location.setVisibility(View.VISIBLE);
                viewHolder.location.setText(response.getDistance());
            } else
                viewHolder.location.setVisibility(View.GONE);
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            if (isReloadFailed)
                viewHolder.setRefresh(view -> {
                    isReloadFailed = false;
                    notifyItemChanged(position);
                    reloadListener.onReload();
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

    public interface OnClickListener {
        void onClick(long productId);
    }
}

class TransactionViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout itemView;
    TextView name;
    TextView quantity;
    TextView price;
    TextView location;
    TextView status;
    TextView timestamp;

    TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = (ConstraintLayout) itemView;
        name = itemView.findViewById(R.id.name);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
        location = itemView.findViewById(R.id.location);
        timestamp = itemView.findViewById(R.id.timestamp);
        status = itemView.findViewById(R.id.pro_status);
    }
}


