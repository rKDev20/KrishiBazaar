package com.krishibazaar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Models.BuyerDetails;
import com.krishibazaar.R;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ProductRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;

    public ProductRequestAdapter(Context context, List<BuyerDetails> list) {
        this.context = context;
        this.list = list;
    }

    List<BuyerDetails> list;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_requests,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductRequestViewHolder viewHolder =(ProductRequestViewHolder) holder;
        ((ProductRequestViewHolder) holder).price.setText(String.valueOf(list.get(position).getPrice()));
        ((ProductRequestViewHolder) holder).name.setText(list.get(position).getName());
        ((ProductRequestViewHolder) holder).distance.setText(list.get(position).getPincode());

        ((ProductRequestViewHolder) holder).timestamp.setText(String.valueOf(list.get(position).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    String getTime(long timestamp){
        Date date=new Date(timestamp);
        Date now=new Date();
        long diff=(now.getTime()-date.getTime())/1000;
        if(diff<60)
            return diff+" seconds ago";
        else if (diff<3600)
            return diff/60 +" minutes ago";
        else if(diff<216000)
            return diff/3600+" hours ago";
        else
            return diff/216000+" days ago";
    }
    public class ProductRequestViewHolder extends RecyclerView.ViewHolder
    {
        TextView price,name,distance,timestamp;
        Button accept,reject;
        public ProductRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            price=itemView.findViewById(R.id.price);
            name=itemView.findViewById(R.id.name);
            distance=itemView.findViewById(R.id.dis);
            timestamp=itemView.findViewById(R.id.ts);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
        }
    }
}
