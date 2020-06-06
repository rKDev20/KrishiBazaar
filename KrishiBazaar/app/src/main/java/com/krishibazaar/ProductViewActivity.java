package com.krishibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.krishibazaar.Adapters.ProductRequestAdapter;
import com.krishibazaar.Models.BuyerDetails;
import com.krishibazaar.Models.Product;
import com.krishibazaar.Models.TransactionDetails;
import com.krishibazaar.Utils.LoadingButton;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.util.List;

import static com.krishibazaar.Utils.Constants.ACCEPTED;
import static com.krishibazaar.Utils.Constants.AVAILABLE;
import static com.krishibazaar.Utils.Constants.DELETED;
import static com.krishibazaar.Utils.Constants.OWNED;
import static com.krishibazaar.Utils.Constants.PENDING;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;
import static com.krishibazaar.Utils.Constants.REJECTED;
import static com.krishibazaar.Utils.Constants.SOLD;

public class ProductViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView cat, scat, qty, prc, desc, pin, dis, disLabel, name;
    ImageView productImage;
    int productId;
    GoogleMap map;
    Double lat, lon;
    ProductRequestAdapter adapter;
    ShimmerFrameLayout shimmer1, shimmer2, shimmer3;
    ScrollView container;
    ScrollView shimmer;
    ConstraintLayout errorBox;
    Button retry;
    TextView catDot;
    TextView errorText;
    FrameLayout statusLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        productId = getIntent().getIntExtra(PRODUCT_ID, -1);
        if (productId == -1) {
            Toast.makeText(this, "Couldn't find product", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            initViews();
            retry.setOnClickListener(v -> productDetails(productId));
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

    private void initViews() {
        cat = findViewById(R.id.cat);
        scat = findViewById(R.id.scat);
        qty = findViewById(R.id.qty);
        prc = findViewById(R.id.price);
        desc = findViewById(R.id.desc);
        pin = findViewById(R.id.pin);
        productImage = findViewById(R.id.proImg);
        name = findViewById(R.id.name);
        dis = findViewById(R.id.dis);
        disLabel = findViewById(R.id.disLabel);
        container = findViewById(R.id.container);
        shimmer = findViewById(R.id.shimmer);
        errorBox = findViewById(R.id.errorBox);
        shimmer1 = findViewById(R.id.shimmer_view_container);
        shimmer2 = findViewById(R.id.shimmer_view_container2);
        shimmer3 = findViewById(R.id.shimmer_view_container3);
        retry = findViewById(R.id.retry);
        errorText = findViewById(R.id.errorText);
        catDot = findViewById(R.id.textView10);
        statusLayout = findViewById(R.id.status_layout);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateMap();
    }

    public void productDetails(final int productId) {
        showLoading();
        String token = SharedPreferenceManager.getToken(this);
        VolleyRequestMaker.getProductDetails(this, new Product.Query(token, productId), new VolleyRequestMaker.TaskFinishListener<Product.Response>() {
            @Override
            public void onSuccess(final Product.Response response) {
                loadResponse(response);
            }

            @Override
            public void onError(String error) {
                showError(error);
            }
        });
    }

    private void loadResponse(Product.Response response) {
        if (response.getImageUrl() != null)
            Glide.with(ProductViewActivity.this)
                    .load(response.getImageUrl())
                    .error(R.drawable.default_picture)
                    .into(productImage);

        name.setText(response.getName());

        if (response.getCategory() != null)
            cat.setText(response.getCategory());
        else catDot.setVisibility(View.GONE);
        if (response.getSubCategory() != null)
            scat.setText(response.getSubCategory());

        prc.setText(response.getPrice(false));

        desc.setText(response.getDescription());

        int status = response.getStatus();

        qty.setText(response.getQuantity());

        pin.setText(String.valueOf(response.getPincode()));
        if (response.getDistance() == null) {
            dis.setVisibility(View.GONE);
            disLabel.setVisibility(View.GONE);
        } else
            dis.setText(response.getDistance());
        lat = response.getLatitude();
        lon = response.getLongitude();
        updateMap();

        switch (status) {
            case OWNED:
                owned(response.getBuyers());
                break;
            case AVAILABLE:
                available();
                break;
            case ACCEPTED:
                accepted(response.getFarmerMobile());
                break;
            case PENDING:
                pending();
                break;
            case REJECTED:
                rejected();
                break;
            case DELETED:
                deleted();
                break;
            case SOLD:
                sold();
                break;
        }
        showContainer();
    }

    private void sold() {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_sold, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
    }

    private void deleted() {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_deleted, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
    }

    private void rejected() {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_rejected, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
    }

    private void pending() {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_pending, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
    }

    private void accepted(long farmerMobile) {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_accepted, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
        Button call = view.findViewById(R.id.call);
        call.setOnClickListener(v -> {
            Intent makeCall = new Intent(Intent.ACTION_DIAL);
            makeCall.setData(Uri.parse("tel:" + farmerMobile));
            startActivity(makeCall);
        });
    }

    private void available() {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_available, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
        EditText suggestedPrice = view.findViewById(R.id.suggested_price);
        EditText suggestedPincode = view.findViewById(R.id.suggested_pincode);
        LoadingButton request = view.findViewById(R.id.request);
        request.setOnClickListener(req -> {
            request.startProgressBar();
            makeTransaction(productId,
                    Float.parseFloat(suggestedPrice.getText().toString()),
                    Integer.parseInt(suggestedPincode.getText().toString()),
                    request);
        });
    }

    private void owned(List<BuyerDetails> buyers) {
        View view = LayoutInflater.from(this).inflate(R.layout.product_view_owned, statusLayout, false);
        statusLayout.removeAllViews();
        statusLayout.addView(view);
        LoadingButton delete;
        delete=view.findViewById(R.id.delete);
        delete.setOnClickListener(v->{
            delete.startProgressBar();
            VolleyRequestMaker.deleteProduct(this, SharedPreferenceManager.getToken(this), productId, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                @Override
                public void onSuccess(Integer response) {
                    delete.stopProgressBar();
                    deleted();
                }

                @Override
                public void onError(String error) {
                    delete.stopProgressBar();
                    Toast.makeText(ProductViewActivity.this,error,Toast.LENGTH_SHORT).show();
                }
            });
        });
        ListView buyersList = view.findViewById(R.id.buyers);
        if (buyers == null || buyers.size() == 0) {
            view.findViewById(R.id.no_buyers).setVisibility(View.VISIBLE);
            buyersList.setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.no_buyers).setVisibility(View.GONE);
            buyersList.setVisibility(View.VISIBLE);
            adapter = new ProductRequestAdapter(ProductViewActivity.this, buyers);
            buyersList.setAdapter(adapter);
        }
    }

    public void makeTransaction(int productId, Float negotiablePrice, int pinCode, LoadingButton request) {
        VolleyRequestMaker.makeTransaction(this,
                new TransactionDetails.Query(SharedPreferenceManager.getToken(this), productId, negotiablePrice, pinCode),
                new VolleyRequestMaker.TaskFinishListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        request.stopProgressBar();
                        pending();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ProductViewActivity.this, error, Toast.LENGTH_LONG).show();
                        request.stopProgressBar();
                    }
                });
    }

    private void updateMap() {
        if (map != null && lat != null && lon != null) {
            CameraPosition camPos = new CameraPosition.Builder()
                    .zoom(15)
                    .target(new LatLng(lat, lon))
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);
        }
    }
}

