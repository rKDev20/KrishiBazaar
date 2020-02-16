package com.krishibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.florent37.materialtextfield.MaterialTextField;
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

import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.AVAILABLE;
import static com.krishibazaar.Utils.Constants.OWNED;
import static com.krishibazaar.Utils.Constants.PENDING;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;

public class ProductViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView cat, scat, qty, prc, desc, pin, dis, proStatus, name;
    MaterialTextField negPrice, buyerpin;
    Button actions;
    Button call;
    CardView reqcard;
    ImageView productImage;
    int productId;
    GoogleMap map;
    ListView requests;
    double lat, lon;
    List<BuyerDetails> buyers;
    ProductRequestAdapter adapter;
    ShimmerFrameLayout shimmer1, shimmer2, shimmer3;
    ScrollView container;
    ScrollView shimmer;
    ConstraintLayout errorBox;
    Button retry;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        productId = getIntent().getIntExtra(PRODUCT_ID, -1);
        if (productId == -1) {
            finish();
            Log.d("part2", "error");
        } else {
            proStatus = findViewById(R.id.pro_status);
            call = findViewById(R.id.call);
            call.setVisibility(View.GONE);
            buyerpin = findViewById(R.id.buyerpc);
            buyerpin.setVisibility(View.GONE);
            negPrice = findViewById(R.id.neg_price);
            negPrice.setVisibility(View.GONE);
            actions = findViewById(R.id.actions);
            actions.setVisibility(View.GONE);
            requests = findViewById(R.id.lv);
            proStatus.setVisibility(View.GONE);
            container = findViewById(R.id.container);
            reqcard = findViewById(R.id.req_card);
            reqcard.setVisibility(View.GONE);
            shimmer = findViewById(R.id.shimmer);
            errorBox = findViewById(R.id.errorBox);
            shimmer1 = findViewById(R.id.shimmer_view_container);
            shimmer2 = findViewById(R.id.shimmer_view_container2);
            shimmer3 = findViewById(R.id.shimmer_view_container3);
            retry = findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDetails(productId);
                }
            });
            errorText = findViewById(R.id.errorText);
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(this);
            productDetails(productId);
        }
    }

    private void showLoading() {
        shimmer.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        errorBox.setVisibility(View.GONE);
        shimmer1.startShimmer();
        shimmer2.startShimmer();
        shimmer3.startShimmer();
    }

    private void stopLoading() {
        shimmer1.stopShimmer();
        shimmer2.stopShimmer();
        shimmer3.stopShimmer();
    }

    private void showContainer() {
        container.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        errorBox.setVisibility(View.GONE);
        stopLoading();
    }

    private void showError(String error) {
        container.setVisibility(View.GONE);
        shimmer.setVisibility(View.GONE);
        errorBox.setVisibility(View.VISIBLE);
        errorText.setText(error);
        stopLoading();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lon));
        markerOptions.title("Product Location");
    }

    public void productDetails(final int productId) {
        showLoading();
        String token = SharedPreferenceManager.getToken(this);
        VolleyRequestMaker.getProductDetails(this, new Product.Query(token, productId), new VolleyRequestMaker.TaskFinishListener<Product.Response>() {
            @Override
            public void onSuccess(final Product.Response response) {
                buyers=response.getBuyers();
                int status = response.getStatus();
                cat = findViewById(R.id.cat);
                scat = findViewById(R.id.scat);
                qty = findViewById(R.id.qty);
                prc = findViewById(R.id.price);
                desc = findViewById(R.id.desc);
                pin = findViewById(R.id.pin);
                productImage = findViewById(R.id.proImg);
                name = findViewById(R.id.name);
                dis = findViewById(R.id.dis);
                if (response.getCategory() != null)
                    cat.setText(response.getCategory());
                if (response.getSubCategory() != null)
                    scat.setText(response.getSubCategory());
                qty.setText(response.getQuantity());
                prc.setText(response.getPrice(false));
                desc.setText(response.getDescription());
                pin.setText(String.valueOf(response.getPincode()));
                dis.setText(response.getDistance());
                name.setText(response.getName());
                lat = response.getLatitude();
                lon = response.getLongitude();
               // Glide.with(ProductViewActivity.this).load(response.getImageUrl()).into(productImage);
                Log.d("part2", status + "ymkbmc");

                if (status == OWNED) {
                    if (buyers != null) {
                        reqcard.setVisibility(View.VISIBLE);
                        adapter = new ProductRequestAdapter(ProductViewActivity.this, buyers);
                        requests.setAdapter(adapter);
                    }
                } else if (status == AVAILABLE) {
                    actions.setVisibility(View.VISIBLE);
                    negPrice.setVisibility(View.VISIBLE);
                    final int buyerpincode = SharedPreferenceManager.getUser(ProductViewActivity.this).getPincode();
                    buyerpin.setVisibility(View.VISIBLE);
                    buyerpin.getEditText().setText(String.valueOf(buyerpincode));
                    actions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeTransaction(productId, Float.parseFloat(negPrice.getEditText().getText().toString()), buyerpincode);
                        }
                    });
                } else if (status == PENDING) {
                    proStatus.setVisibility(View.VISIBLE);
                    proStatus.setText(ProductViewActivity.this.getResources().getStringArray(R.array.status_array)[status]);
                } else if (status == ACCEPTED) {
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
                } else {
                    proStatus.setText(ProductViewActivity.this.getResources().getStringArray(R.array.status_array)[status]);
                }
                showContainer();
            }

            @Override
            public void onError(String error) {
                showError(error);
            }
        });
    }

    public void makeTransaction(int productId, float negotiablePrice, int pinCode) {
        VolleyRequestMaker.makeTransaction(this, new TransactionDetails.Query("kiit", productId, negotiablePrice, pinCode), new VolleyRequestMaker.TaskFinishListener<Integer>() {
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
                Toast.makeText(ProductViewActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

}

