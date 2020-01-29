package com.krishibazaar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Adapters.SearchAdapter;
import com.krishibazaar.Models.LocationDetails;
import com.krishibazaar.Models.Search;
import com.krishibazaar.Popups.LocationChooser;
import com.krishibazaar.Popups.PopupListener;
import com.krishibazaar.Utils.LocationManagerActivity;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.util.List;

public class HomeActivity extends LocationManagerActivity {

    private final int ITEMS_TO_LOAD = 3;
    int pageOffset = 0;
    boolean isLoading = false;
    LocationDetails locationDetails;
    TextView locationText;
    EditText searchBox;
    Button searchButton;
    RecyclerView recyclerView;
    RecyclerView.OnScrollListener scrollListener;
    SearchAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        getLocation(new LocationListener() {
            @Override
            public void onSuccess(LocationDetails details) {
                locationText.setText(details.getName());
                locationDetails = details;
            }

            @Override
            public void onError(String error) {
                locationText.setText(error);
            }
        });
    }

    void initViews() {
        locationText = findViewById(R.id.location);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationChooser.popup(HomeActivity.this, new PopupListener() {
                    @Override
                    public void onLocationSelected(LocationDetails details) {
                        locationText.setText(details.getName());
                        locationDetails = details;
                    }
                });
            }
        });
        searchBox = findViewById(R.id.search_box);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearch();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initScrollListener();
    }

    private void initSearch() {
        pageOffset = 0;
        isLoading = false;
        recyclerView.addOnScrollListener(scrollListener);
        if (adapter != null)
            adapter.reset();
        loadProducts();
    }

    void loadProducts() {
        isLoading = true;
        String searchText = searchBox.getText().toString();
        if (!searchText.isEmpty()) {
            Search.Query query = new Search.Query(searchText, null, ITEMS_TO_LOAD, pageOffset + 1, null, null);
            VolleyRequestMaker.loadProducts(this, query, new VolleyRequestMaker.TaskFinishListener<List<Search.Response>>() {
                @Override
                public void onSuccess(List<Search.Response> response) {
                    refreshList(response);
                    isLoading = false;
                    ++pageOffset;
                    adapter.setReloadFailed(false);
                    if (response.size() < ITEMS_TO_LOAD) {
                        adapter.setMaxLimitReached(true);
                        recyclerView.removeOnScrollListener(scrollListener);
                    }
                }

                @Override
                public void onError(String error) {
                    Log.d("abcd", error);
                    isLoading = false;
                    if (adapter != null)
                        adapter.setReloadFailed(true);
                }
            });
        }
    }

    void refreshList(List<Search.Response> response) {
        if (adapter == null) {
            adapter = new SearchAdapter(response, new SearchAdapter.ReloadListener() {
                @Override
                public void onReload() {
                    loadProducts();
                }
            });
            recyclerView.setAdapter(adapter);
        } else adapter.addData(response);
        adapter.notifyDataSetChanged();
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
