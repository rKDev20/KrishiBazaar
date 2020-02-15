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
                if (view.getId()==R.id.accept) {
                    status = ACCEPTED;
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    actionStatus.setText(view.getResources().getStringArray(R.array.status_array)[3]);
                    actionStatus.setBackgroundColor(view.getResources().getColor(R.color.accepted));
                }
                else{
                    status=REJECTED;
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    actionStatus.setText(view.getResources().getStringArray(R.array.status_array)[4]);
                    actionStatus.setBackgroundColor(view.getResources().getColor(R.color.rejected));
                }
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

    public void actionMade(final View view, int i, final int status)
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
                    actionStatus.setText(view.getResources().getStringArray(R.array.status_array)[3]);
                    actionStatus.setBackgroundColor(view.getResources().getColor(R.color.accepted));

                }
                else
                {
                    actionStatus.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    actionStatus.setText(view.getResources().getStringArray(R.array.status_array)[4]);
                    actionStatus.setBackgroundColor(view.getResources().getColor(R.color.rejected));
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
