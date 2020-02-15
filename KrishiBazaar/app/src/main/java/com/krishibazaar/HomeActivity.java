package com.krishibazaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.krishibazaar.Adapters.SearchAdapter;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Models.Search;
import com.krishibazaar.Popups.LocationChooser;
import com.krishibazaar.Popups.PopupListener;
import com.krishibazaar.Utils.LocationManagerActivity;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.util.List;

import static com.krishibazaar.Utils.Constants.PRODUCT_ID;

public class HomeActivity extends Fragment {

    private final int ITEMS_TO_LOAD = 3;
    private int pageOffset = 0;
    private boolean isLoading = false;
    private LocationDetails locationDetails;
    private TextView locationText;
    private EditText searchBox;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private RecyclerView.OnScrollListener scrollListener;
    private SearchAdapter adapter;
    private LocationManagerActivity activity;
    private Context context;
    private String searchText;
    private LocationManagerActivity.LocationListener listener;
    private ProgressBar progressBar;
    private TextView errorText;
    private ConstraintLayout errorBox;
    private LinearLayout loadingBox;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Button retry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        initViews(view);
        initLoading(inflater);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("abcd", "here mf");

        activity = (LocationManagerActivity) getActivity();
        context = activity.getApplicationContext();
        locationDetails = SharedPreferenceManager.getLocation(context);
        if (locationDetails != null)
            locationText.setText(locationDetails.getName());
        listener = new LocationManagerActivity.LocationListener() {
            @Override
            public void onSuccess(LocationDetails details) {
                SharedPreferenceManager.setLocation(context, details);
                locationText.setText(details.getName());
                locationDetails = details;
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        };
        activity.getLocation(listener);
        initSearch(true);
    }

    private void initViews(View view) {
        locationText = view.findViewById(R.id.location);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationChooser chooser = new LocationChooser(activity, new PopupListener() {
                    @Override
                    public void onLocationSelected(LocationDetails details) {
                        locationText.setText(details.getName());
                        locationDetails = details;
                    }
                });
                chooser.popup();
            }
        });
        searchBox = view.findViewById(R.id.search_box);
        progressBar = view.findViewById(R.id.progressBar);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearch(false);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        errorText = view.findViewById(R.id.errorText);
        errorBox = view.findViewById(R.id.errorContainer);
        loadingBox = view.findViewById(R.id.loadingContainer);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        retry = view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText == null || searchText.compareTo("") == 0)
                    initSearch(true);
                else initSearch(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        initScrollListener();
    }

    private void initLoading(LayoutInflater inflater) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int n = metrics.heightPixels / (int) getActivity().getResources().getDimension(R.dimen.shimmer_container);
        for (int i = 0; i < n - 2; i++) {
            Log.d("abcd", n + "sss");
            loadingBox.addView(inflater.inflate(R.layout.shimmer_home_activity, loadingBox, false));
        }
    }

    private void initSearch(boolean blank) {
        String temp = searchBox.getText().toString();
        if ((!temp.isEmpty() && temp.length() > 3) || blank) {
            searchText = temp;
            pageOffset = 0;
            isLoading = false;
            recyclerView.addOnScrollListener(scrollListener);
            if (blank) {
                searchText = null;
                setLoading();
            } else setSearchLoading();
            loadProducts();
        } else
            Toast.makeText(context, "Search text must be greater than 3 characters", Toast.LENGTH_SHORT).show();
    }

    private void setSearchLoading() {
        isLoading = true;
        if (pageOffset == 0) {
            searchButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setLoading() {
        errorBox.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void unsetLoading() {
        isLoading = false;
        searchButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
    }

    private void showError(String error) {
        if (pageOffset == 0) {
            errorBox.setVisibility(View.VISIBLE);
            errorText.setText(error);
        }
    }

    private void removeError() {
        errorBox.setVisibility(View.GONE);
    }

    private void loadProducts() {
        isLoading = true;
        Double latitude = null;
        Double longitude = null;
        if (locationDetails != null) {
            longitude = locationDetails.getLongitude();
            latitude = locationDetails.getLatitude();
        }
        Search.Query query = new Search.Query(searchText, ITEMS_TO_LOAD, pageOffset + 1, latitude, longitude);
        VolleyRequestMaker.loadProducts(context, query, new VolleyRequestMaker.TaskFinishListener<List<Search.Response>>() {
            @Override
            public void onSuccess(List<Search.Response> response) {
                refreshList(response);
                removeError();
                unsetLoading();
                ++pageOffset;
                adapter.setReloadFailed(false);
                if (response.size() < ITEMS_TO_LOAD) {
                    adapter.setMaxLimitReached(true);
                    recyclerView.removeOnScrollListener(scrollListener);
                }
            }

            @Override
            public void onError(String error) {
                showError(error);
                unsetLoading();
                if (adapter != null)
                    adapter.setReloadFailed(true);
            }
        });
    }

    private void refreshList(List<Search.Response> response) {
        if (adapter == null) {
            adapter = new SearchAdapter(context, new SearchAdapter.ReloadListener() {
                @Override
                public void onReload() {
                    loadProducts();
                }
            }, new SearchAdapter.OnClickListener() {
                @Override
                public void onClick(long productId) {
                    openProductView(productId);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        if (pageOffset == 0)
            adapter.reset();
        adapter.addData(response);
    }

    private void openProductView(long productId) {
        Intent intent = new Intent(context, ProductViewActivity.class);
        intent.putExtra(PRODUCT_ID, (int)productId);
        startActivity(intent);
    }

    private void initScrollListener() {
        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                        loadProducts();
                        isLoading = true;
                    }
                }
            }
        };
    }
}
