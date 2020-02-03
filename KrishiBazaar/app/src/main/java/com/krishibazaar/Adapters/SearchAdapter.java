package com.krishibazaar.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.krishibazaar.Models.Search;
import com.krishibazaar.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private int VIEW_TYPE_DATA = 0;
    final private int VIEW_TYPE_LOADING = 1;
    private List<Search.Response> data;
    private boolean isMaxLimitReached;
    private boolean isReloadFailed;
    private ReloadListener listener;
    private Context context;
    public SearchAdapter(ReloadListener listener, Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        isMaxLimitReached = false;
        isReloadFailed = false;
        this.listener = listener;
    }

    public void addData(List<Search.Response> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void reset() {
        data.clear();
        isMaxLimitReached = false;
        isReloadFailed = false;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SearchViewHolder) {
            final SearchViewHolder viewHolder = (SearchViewHolder) holder;
            Glide.with(context).clear(viewHolder.image);
            viewHolder.image.setImageResource(R.drawable.image);
            Glide.with(context).load(data.get(position)
                    .getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            BitmapDrawable drawable = (BitmapDrawable) resource;
                            Bitmap bitmap = drawable.getBitmap();
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    int color=p.getDominantColor(context.getResources().getColor(R.color.blue));
                                    ViewCompat.setBackgroundTintList(viewHolder.wave,ColorStateList.valueOf(color));
                                    viewHolder.container.setStrokeColor(color);
                                }
                            });
                            return false;
                        }
                    })
                    .into(viewHolder.image);
            viewHolder.name.setText(data.get(position).getName());
            viewHolder.description.setText(data.get(position).getDescription());
            viewHolder.quantity.setText(data.get(position).getQuantity());
            viewHolder.price.setText(data.get(position).getPrice());
            viewHolder.location.setText(data.get(position).getDistance());
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            Log.d("abcd", "here");
            if (isReloadFailed)
                viewHolder.setRefresh(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isReloadFailed = false;
                        listener.onReload();
                        notifyItemChanged(position);
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
    ImageView image;
    ConstraintLayout wave;
    MaterialCardView container;
    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        wave=itemView.findViewById(R.id.wave);
        container =itemView.findViewById(R.id.container);
        image = itemView.findViewById(R.id.image);
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
        Log.d("abcd", "setRefresh()");
        refresh.setVisibility(View.VISIBLE);
        refresh.setOnClickListener(listener);
        progressBar.setVisibility(View.GONE);
    }

    public void setProgressBar() {
        refresh.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
}

