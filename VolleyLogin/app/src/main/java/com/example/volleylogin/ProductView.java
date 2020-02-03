package com.example.volleylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductView extends AppCompatActivity {
    TextView cat,scat,qty,prc,desc,pin,dis,proStatus;
    EditText negPrice;
    Button button;
    ImageButton call;
    String productId="6969";
    String url1="http://192.168.43.151/productDetails.php";
    String url2="http://192.168.43.151/makeTransaction.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        proStatus=findViewById(R.id.status);
        //proStatus.setVisibility(View.INVISIBLE);
        call=findViewById(R.id.call);
        call.setVisibility(View.INVISIBLE);
        negPrice=findViewById(R.id.neg_price);
        negPrice.setVisibility(View.INVISIBLE);
        button=findViewById(R.id.actions);
        button.setVisibility(View.INVISIBLE);
        productDetails("6969");

    }
    public void productDetails(final String productId)
    {
        JSONObject params= new JSONObject();
        try
        {
            params.put("productId",productId);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try
                        {
                            String category=response.getJSONObject("result").getString("category");
                            String subCategory=response.getJSONObject("result").getString("subCategory");
                            String quantity=response.getJSONObject("result").getString("quantity");
                            String price=response.getJSONObject("result").getString("price");
                            String description=response.getJSONObject("result").getString("description");
                            String pincode=response.getJSONObject("result").getString("pincode");
                            String distance=response.getJSONObject("result").getString("distance");
                            String status=response.getJSONObject("result").getString("status");
                            cat=findViewById(R.id.cat);
                            scat=findViewById(R.id.scat);
                            qty=findViewById(R.id.qty);
                            prc=findViewById(R.id.prc);
                            desc=findViewById(R.id.desc);
                            pin=findViewById(R.id.pin);
                            dis=findViewById(R.id.dis);
                            cat.setText(cat.getText().toString()+category);
                            scat.setText(scat.getText().toString()+subCategory);
                            qty.setText(qty.getText().toString()+quantity+" Kgs");
                            prc.setText(prc.getText().toString()+"₹"+price+" per Kgs");
                            desc.setText(desc.getText().toString()+description);
                            pin.setText(pin.getText().toString()+pincode);
                            dis.setText(dis.getText().toString()+distance+" KM");
                            if(status.equals("owned"))
                            {
                                proStatus.setVisibility(View.VISIBLE);
                                proStatus.setText("Product is already owned !");
                            }
                            else if(status.equals("available"))
                            {
                                negPrice.setVisibility(View.VISIBLE);
                                button.setText("I am Interested !");
                                button.setVisibility(View.VISIBLE);
                                //make transaction
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(negPrice.getText().toString().length()!=0)
                                        {
                                            int usersPrice=Integer.parseInt(negPrice.getText().toString());
                                            if(usersPrice!=0)
                                                makeTransaction(usersPrice);
                                            else
                                                Toast.makeText(ProductView.this,"You can't buy it for FREE !",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                            Toast.makeText(ProductView.this,"Enter your negotiable price !",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else if(status.equals("pending"))
                            {
                                proStatus.setText("Awaiting Farmer's Approval ! ");
                                proStatus.setVisibility(View.VISIBLE);
                            }
                            else if(status.equals("accepted"))
                            {
                                proStatus.setText("Proposal Accepted ! ");
                                proStatus.setVisibility(View.VISIBLE);
                                call.setVisibility(View.VISIBLE);
                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String mobileNumber= null;
                                        try
                                        {
                                            mobileNumber = response.getJSONObject("result").getJSONObject("farmer_detail").getString("mobile");
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Intent makeCall=new Intent(Intent.ACTION_DIAL);
                                        makeCall.setData(Uri.parse("tel:"+mobileNumber));
                                        startActivity(makeCall);
                                    }
                                });

                            }
                            else if(status.equals("rejected"))
                            {
                                proStatus.setText("Proposal Rejected ! ");
                                proStatus.setVisibility(View.VISIBLE);
                            }
                            else if(status.equals("sold"))
                            {
                                proStatus.setText("Item already sold ! ");
                                proStatus.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(ProductView.this,"Error !",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ProductView.this,"Error !",Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void makeTransaction(int usersPrice)
    {
        JSONObject params= new JSONObject();
        try
        {
            params.put("productId",productId);
            params.put("negPrice",usersPrice);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            String status=response.getString("status");
                            if(status.equals("success"))
                            {
                                negPrice.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                proStatus.setVisibility(View.VISIBLE);
                                proStatus.setText(proStatus.getText()+"Proposal Sent !");
                            }
                            else if(status.equals("product_sold"))
                            {
                                negPrice.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                proStatus.setVisibility(View.VISIBLE);
                                proStatus.setText("Item already sold ! ");
                                Toast.makeText(ProductView.this,"Hard Luck !",Toast.LENGTH_LONG).show();
                            }
                            else if(status.equals("invalid_user"))
                            {
                                negPrice.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.INVISIBLE);
                                Toast.makeText(ProductView.this,"Error !",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(ProductView.this,"Login Error !",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductView.this,"Login Error 7!",Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
