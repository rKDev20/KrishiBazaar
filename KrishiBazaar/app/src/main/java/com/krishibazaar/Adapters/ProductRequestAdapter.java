package com.krishibazaar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Models.BuyerDetails;
import com.krishibazaar.R;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.REJECTED;

public class ProductRequestAdapter extends BaseAdapter
{
    Context context;
    List<BuyerDetails> list;
    TextView price,name,timestamp,distance,actionStatus;
    Button accept,reject;


    public ProductRequestAdapter(Context context,List<BuyerDetails> list) {
        this.list = list;
        this.context = context;
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
        view=LayoutInflater.from(context).inflate(R.layout.adapter_product_requests,viewGroup,false);
        price=view.findViewById(R.id.price);
        actionStatus=view.findViewById(R.id.action);
        actionStatus.setVisibility(View.GONE);
        accept=view.findViewById(R.id.accept);
        reject=view.findViewById(R.id.reject);
        name=view.findViewById(R.id.name);
        timestamp=view.findViewById(R.id.ts);
        distance=view.findViewById(R.id.dis);
        price.setText(String.valueOf(list.get(i).getPrice()));
        name.setText(list.get(i).getName());
        distance.setText(String.valueOf(list.get(i).getDistance()));
        timestamp.setText(getTime(list.get(i).getTimestamp()));
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status;
                if (view.getId()==R.id.accept)
                    status=ACCEPTED;
                else status=REJECTED;
                actionMade(view,i,status);
            }
        } ;
        accept.setOnClickListener(listener);
        reject.setOnClickListener(listener);
        return view;
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

    public void actionMade(View view, int i, final int status)
    {
        //TODO
        VolleyRequestMaker.changeTransaction(context, "sss", list.get(i).getTranId(), status, new VolleyRequestMaker.TaskFinishListener<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                if(status==ACCEPTED)
                {
                    actionStatus.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    actionStatus.setText("REQUEST ACCEPTED");
                }
                else
                {
                    actionStatus.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    actionStatus.setText("REQUEST REJECTED");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


}
//    Context context;
//
//    public ProductRequestAdapter(Context context, List<BuyerDetails> list) {
//        super();
//        this.context = context;
//        this.list = list;
//    }
//
//    List<BuyerDetails> list;
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_requests,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ProductRequestViewHolder viewHolder =(ProductRequestViewHolder) holder;
//        ((ProductRequestViewHolder) holder).price.setText(String.valueOf(list.get(position).getPrice()));
//        ((ProductRequestViewHolder) holder).name.setText(list.get(position).getName());
//        ((ProductRequestViewHolder) holder).distance.setText(list.get(position).getPincode());
//        ((ProductRequestViewHolder) holder).timestamp.setText(getTime(list.get(position).getTimestamp()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class ProductRequestViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView price,name,distance,timestamp;
//        Button accept,reject;
//        public ProductRequestViewHolder(@NonNull View itemView) {
//            super(itemView);
//            price=itemView.findViewById(R.id.price);
//            name=itemView.findViewById(R.id.name);
//            distance=itemView.findViewById(R.id.dis);
//            timestamp=itemView.findViewById(R.id.ts);
//            accept=itemView.findViewById(R.id.accept);
//            reject=itemView.findViewById(R.id.reject);
//        }
//    }

