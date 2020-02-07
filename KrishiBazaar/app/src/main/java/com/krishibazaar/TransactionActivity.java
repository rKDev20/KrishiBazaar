//package com.krishibazaar;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.krishibazaar.Adapters.TransactionAdapter;
//import com.krishibazaar.Models.Transaction;
//import com.krishibazaar.Utils.VolleyRequestMaker;
//
//import java.util.List;
//
//public class TransactionActivity extends AppCompatActivity {
//    private final int ITEMS_TO_LOAD = 3;
//    int pageOffset = 0;
//    boolean isLoading = false;
//    RecyclerView recyclerView;
//    RecyclerView.OnScrollListener scrollListener;
//    TransactionAdapter adapter;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction);
//    }
//
//    void initViews() {
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        initScrollListener();
//    }
//
//    private void initTransactions() {
//        pageOffset = 0;
//        isLoading = false;
//        recyclerView.addOnScrollListener(scrollListener);
//        if (adapter != null)
//            adapter.reset();
//        loadTransactions();
//    }
//
//    void loadTransactions() {
//        isLoading = true;
//        //TODO
//        Transaction.Query query = new Transaction.Query("rishabh", pageOffset + 1, ITEMS_TO_LOAD);
//        VolleyRequestMaker.getTransactions(this, query, new VolleyRequestMaker.TaskFinishListener<List<Transaction.Response>>() {
//            @Override
//            public void onSuccess(List<Transaction.Response> response) {
//                refreshList(response);
//                isLoading = false;
//                ++pageOffset;
//                adapter.setReloadFailed(false);
//                if (response.size() < ITEMS_TO_LOAD) {
//                    adapter.setMaxLimitReached(true);
//                    recyclerView.removeOnScrollListener(scrollListener);
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.d("abcd", error);
//                isLoading = false;
//                if (adapter != null)
//                    adapter.setReloadFailed(true);
//            }
//        });
//    }
//
//    void refreshList(List<Transaction.Response> response) {
//        if (adapter == null) {
//            adapter = new TransactionAdapter(response, new TransactionAdapter.ReloadListener() {
//                @Override
//                public void onReload() {
//                    loadTransactions();
//                }
//            });
//            recyclerView.setAdapter(adapter);
//        } else adapter.addData(response);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void initScrollListener() {
//        scrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!isLoading) {
//                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
//                        loadTransactions();
//                        isLoading = true;
//                    }
//                }
//            }
//        };
//    }
//}
