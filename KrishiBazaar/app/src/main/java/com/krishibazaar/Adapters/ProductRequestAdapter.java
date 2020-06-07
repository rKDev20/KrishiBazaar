package com.krishibazaar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.krishibazaar.Models.BuyerDetails;
import com.krishibazaar.R;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.util.Date;
import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.PENDING;
import static com.krishibazaar.Utils.Constants.REJECTED;

public class ProductRequestAdapter extends BaseAdapter {
    Context context;
    List<BuyerDetails> list;
    String[] statusList;
    int[] colorList;

    public ProductRequestAdapter(Context context, List<BuyerDetails> list) {
        this.list = list;
        this.context = context;
        statusList = context.getResources().getStringArray(R.array.status_array);
        colorList = context.getResources().getIntArray(R.array.status_color);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ProductRequestViewHolder holder;
        BuyerDetails details = list.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_product_requests, viewGroup, false);
            view.setTag(holder = new ProductRequestViewHolder(view));
        } else {
            holder = (ProductRequestViewHolder) view.getTag();
        }

        holder.name.setText(details.getName());
        if (details.getStatus() == PENDING) {
            holder.showButtons();
        } else {
            holder.showStatus(details.getStatus());
        }
        holder.distance.setText(details.getDistance() + " kms away");
        holder.timestamp.setText(getTime(details.getTimestamp()));
        if (details.getPrice() == null)
            holder.price.setVisibility(View.GONE);
        else
            holder.price.setText(String.valueOf(details.getPrice()));
        View.OnClickListener listener = v -> {
            holder.showLoading();
            int status;
            if (v.getId() == R.id.accept)
                status = ACCEPTED;
            else
                status = REJECTED;
            VolleyRequestMaker.changeTransaction(context, SharedPreferenceManager.getToken(context), list.get(i).getTranId(), status, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                @Override
                public void onSuccess(Integer response) {
                    holder.showStatus(status);
                    holder.stopLoading();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    holder.stopLoading();
                    holder.showButtons();
                }
            });
        };
        holder.accept.setOnClickListener(listener);
        holder.reject.setOnClickListener(listener);
        return view;
    }

    private String getTime(long timestamp) {
        Date now = new Date();
        long diff = (now.getTime() / 1000 - timestamp);
        if (diff < 60)
            return diff + " second(s) ago";
        else if (diff < 3600)
            return diff / 60 + " minute(s) ago";
        else if (diff < 86400)
            return diff / 3600 + " hour(s) ago";
        else
            return diff / 86400 + " day(s) ago";
    }

    class ProductRequestViewHolder {
        TextView price, name, timestamp, distance, status;
        Button accept, reject;
        ConstraintLayout loading;

        public ProductRequestViewHolder(View view) {
            price = view.findViewById(R.id.price);
            name = view.findViewById(R.id.name);
            timestamp = view.findViewById(R.id.ts);
            distance = view.findViewById(R.id.dis);
            status = view.findViewById(R.id.status);
            accept = view.findViewById(R.id.accept);
            reject = view.findViewById(R.id.reject);
            loading = view.findViewById(R.id.loading);
        }

        void showLoading() {
            loading.setVisibility(View.VISIBLE);
        }

        void stopLoading() {
            loading.setVisibility(View.GONE);
        }

        void showStatus(int stat) {
            loading.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
            status.setBackgroundColor(colorList[stat]);
            status.setText(statusList[stat]);
        }

        void showButtons() {
            loading.setVisibility(View.GONE);
            reject.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
            status.setVisibility(View.GONE);
        }

    }
}
