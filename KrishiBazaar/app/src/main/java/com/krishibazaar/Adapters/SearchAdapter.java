package com.krishibazaar.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Models.Search;
import com.krishibazaar.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private int VIEW_TYPE_DATA = 0;
    final private int VIEW_TYPE_LOADING = 1;
    private List<Search.Response> data;
    private boolean isMaxLimitReached;
    private boolean isReloadFailed;
    private ReloadListener listener;

    public SearchAdapter(List<Search.Response> data, ReloadListener listener) {
        this.data = data;
        isMaxLimitReached = false;
        isReloadFailed=false;
        this.listener = listener;
    }


    public void addData(List<Search.Response> data) {
        this.data.addAll(data);
    }

    public void reset(){
        data.clear();
        isMaxLimitReached = false;
        isReloadFailed=false;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA)
            return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search, parent, false));
        else
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            viewHolder.name.setText(data.get(position).name);
            viewHolder.description.setText(data.get(position).description);
            viewHolder.quantity.setText(data.get(position).getQuantity());
            viewHolder.price.setText(data.get(position).getPrice());
            viewHolder.location.setText(data.get(position).getDistance());
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            Log.d("abcd","here");
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

class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView description;
    TextView quantity;
    TextView price;
    TextView location;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
        location = itemView.findViewById(R.id.location);
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar progressBar;
    private ImageView refresh;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
        refresh = itemView.findViewById(R.id.refresh);
    }

    public void setRefresh(View.OnClickListener listener) {
        Log.d("abcd","setRefresh()");
        refresh.setVisibility(View.VISIBLE);
        refresh.setOnClickListener(listener);
        progressBar.setVisibility(View.GONE);
    }

    public void setProgressBar() {
        refresh.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
}

