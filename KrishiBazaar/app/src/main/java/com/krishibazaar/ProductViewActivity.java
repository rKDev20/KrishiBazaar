package com.krishibazaar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krishibazaar.Adapters.ProductRequestAdapter;
import com.krishibazaar.Models.BuyerDetails;
import com.krishibazaar.Models.Product;
import com.krishibazaar.Models.TransactionDetails;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import org.json.JSONException;

import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.AVAILABLE;
import static com.krishibazaar.Utils.Constants.OWNED;
import static com.krishibazaar.Utils.Constants.PENDING;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.REJECTED;
import static com.krishibazaar.Utils.Constants.TOKEN;

public class ProductViewActivity extends AppCompatActivity {
    TextView cat, scat, qty, prc, desc, pin, dis, proStatus,name;
    EditText negPrice,buyerpin;
    Button actions;
    ImageButton call;
    ImageView productImage;
    int productId;
    GoogleMap map;
    ListView requests;
    double lat,lon;
    List<BuyerDetails> buyers;
    ProductRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shimmer_product_view);
        Log.d("abcd","here");
//        proStatus = findViewById(R.id.pro_status);
//        call = findViewById(R.id.call);
//        call.setVisibility(View.GONE);
//        buyerpin=findViewById(R.id.buyerpc);
//        buyerpin.setVisibility(View.GONE);
//        negPrice = findViewById(R.id.neg_price);
//        negPrice.setVisibility(View.GONE);
//        actions = findViewById(R.id.actions);
//        actions.setVisibility(View.GONE);
//        requests=findViewById(R.id.lv);
//        proStatus.setVisibility(View.GONE);
//        requests.setVisibility(View.GONE);
        ((ShimmerFrameLayout)findViewById(R.id.shimmer_view_container)).startShimmer();
        //SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
//        supportMapFragment.getMapAsync((OnMapReadyCallback) this);
        //TODO
//        productId = getIntent().getIntExtra(PRODUCT_ID, -1);
//        if (productId == -1)
//            finish();
//        else
//            productDetails(productId);
        Log.d("abcd","here2");

    }

    public void onMapReady(GoogleMap googleMap)
    {
        map=googleMap;
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(new LatLng(lat,lon));
        markerOptions.title("Product Location");
    }

    public void productDetails(final int productId)
    {
        VolleyRequestMaker.getProductDetails(this,new Product.Query(TOKEN,productId), new VolleyRequestMaker.TaskFinishListener<Product.Response>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final Product.Response response) {

                int status = response.getStatus();
                cat = findViewById(R.id.cat);
                scat = findViewById(R.id.scat);
                qty = findViewById(R.id.qty);
                prc = findViewById(R.id.prc);
                desc = findViewById(R.id.desc);
                pin = findViewById(R.id.pin);
                productImage=findViewById(R.id.proImg);
                name=findViewById(R.id.name);
                dis = findViewById(R.id.dis);
                cat.setText(cat.getText().toString() + response.getCategory());
                scat.setText(scat.getText().toString() + response.getName());
                qty.setText(qty.getText().toString() + response.getQuantity());
                prc.setText(prc.getText().toString() + response.getPrice());
                desc.setText(desc.getText().toString() + response.getDescription());
                pin.setText(pin.getText().toString() + response.getPincode());
                dis.setText(dis.getText().toString() + response.getDistance());
                name.setText(name.getText().toString()+response.getName());
                lat=response.getLatitude();
                lon=response.getLongitude();
                Glide.with(ProductViewActivity.this).load(response.getImageUrl()).into(productImage);
                if(status==OWNED)
                {
                    requests.setVisibility(View.VISIBLE);
                    adapter=new ProductRequestAdapter(ProductViewActivity.this,buyers);
                    requests.setAdapter(adapter);
                }
                else if(status==AVAILABLE)
                {
                    actions.setVisibility(View.VISIBLE);
                    negPrice.setVisibility(View.VISIBLE);
                    actions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int buyerpincode= SharedPreferenceManager.getUser(ProductViewActivity.this).getPincode();
                            buyerpin.setVisibility(View.VISIBLE);
                            buyerpin.setText(buyerpincode);
                            makeTransaction(productId,Float.parseFloat(negPrice.getText().toString()),buyerpincode);
                        }
                    });
                }
                else if(status==PENDING)
                {
                    proStatus.setVisibility(View.VISIBLE);
                    proStatus.setText(ProductViewActivity.this.getResources().getStringArray(R.array.status_array)[status]);
                }
                else if(status==ACCEPTED)
                {
                    proStatus.setVisibility(View.VISIBLE);
                    proStatus.setText(ProductViewActivity.this.getResources().getStringArray(R.array.status_array)[status]);
                    call.setVisibility(View.VISIBLE);
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent makeCall = new Intent(Intent.ACTION_DIAL);
                            makeCall.setData(Uri.parse("tel:" + response.getFarmerMobile()));
                            startActivity(makeCall);
                        }
                    });
                }
                else
                {
                    proStatus.setText(ProductViewActivity.this.getResources().getStringArray(R.array.status_array)[status]);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductViewActivity.this,error,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void makeTransaction(int productId,float negotiablePrice,int pinCode)
    {
        VolleyRequestMaker.makeTransaction(this,new TransactionDetails.Query("kiit",productId,negotiablePrice,pinCode), new VolleyRequestMaker.TaskFinishListener<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                proStatus.setVisibility(View.VISIBLE);
                proStatus.setText("PENDING");
                negPrice.setVisibility(View.GONE);
                buyerpin.setVisibility(View.GONE);
                actions.setClickable(false);
                actions.setFocusable(false);
                actions.setText("REQUESTED");
                actions.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductViewActivity.this,error,Toast.LENGTH_LONG).show();
            }
        });
    }

}

