package com.krishibazaar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krishibazaar.Adapters.TransactionAdapter;
import com.krishibazaar.Models.NewUser;
import com.krishibazaar.Models.Transaction;
import com.krishibazaar.Models.User;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import java.util.List;

import static android.view.View.GONE;
import static com.krishibazaar.Utils.Constants.PRODUCT_ID;

public class ProfileActivity extends Fragment {
    private EditText mobile;
    private EditText name;
    private EditText pincode;
    private EditText address;
    private ImageButton edit;
    private ProgressBar editProgressBar;

    private User user;
    private ConstraintLayout error;
    private ConstraintLayout container;
    private boolean editMode = false;
    private TextView errorText;
    private Button retry;
    private ProgressBar errorProgress;
    private Context context;


    private RecyclerView recyclerView;
    private final int ITEMS_TO_LOAD = 3;
    private int pageOffset = 0;
    private boolean isLoading = false;
    private RecyclerView.OnScrollListener scrollListener;
    private TransactionAdapter adapter;

    private ConstraintLayout transactionContainer;
    private CardView transactionError;
    private CardView transactionProgress;
    private TextView transactionErrorText;
    private Button transactionRetry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("abcd", "here mf");
        context = getContext();
        initUser();
        initTransactions();
    }

    private void initViews(View view) {
        mobile = view.findViewById(R.id.mobile);
        address = view.findViewById(R.id.address);
        pincode = view.findViewById(R.id.buyerpc);
        name = view.findViewById(R.id.name);
        edit = view.findViewById(R.id.edit);
        error = view.findViewById(R.id.errorBox);
        retry = view.findViewById(R.id.retry);
        errorText = view.findViewById(R.id.loadingText);
        errorProgress = view.findViewById(R.id.progressBar);
        container = view.findViewById(R.id.container);
        recyclerView = view.findViewById(R.id.recyclerView);
        editProgressBar = view.findViewById(R.id.edit_progress_bar);
        transactionContainer = view.findViewById(R.id.transactionContainer);
        transactionError = view.findViewById(R.id.transactionError);
        transactionErrorText = view.findViewById(R.id.transactionErrorText);
        transactionProgress = view.findViewById(R.id.transactionProgress);
        transactionRetry = view.findViewById(R.id.transactionErrorRetry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUser();
            }
        });
        transactionRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTransactions();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    changeProfile();
                } else {
                    setEdit(true);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        initScrollListener();
    }

    boolean onBackPressed() {
        if (editMode) {
            setEdit(false);
            return false;
        } else return true;
    }

    private void initUser() {
        user = SharedPreferenceManager.getUser(context);
        if (user != null) {
            showProfileContainer();
            setUser();
        } else showBigLoading();
        fetchProfile();
        setEdit(false);
    }

    private void setUser() {
        if (user != null) {
            mobile.setText(String.valueOf(user.getMobile()));
            address.setText(user.getAddress());
            pincode.setText(String.valueOf(user.getPincode()));
            name.setText(user.getName());
        }
    }

    private void setEdit(boolean enable) {
        setUser();
        mobile.setFocusable(false);
        mobile.setCursorVisible(false);
        editMode = enable;
        address.setFocusable(enable);
        address.setFocusableInTouchMode(enable);
        address.setCursorVisible(enable);
        pincode.setFocusable(enable);
        pincode.setFocusableInTouchMode(enable);
        pincode.setCursorVisible(enable);
        name.setFocusable(enable);
        name.setFocusableInTouchMode(enable);
        name.setCursorVisible(enable);
        if (!enable) {
            edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
            pincode.setBackground(getResources().getDrawable(android.R.color.transparent));
            name.setBackground(getResources().getDrawable(android.R.color.transparent));
            address.setBackground(getResources().getDrawable(android.R.color.transparent));
        } else {
            name.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            EditText tmp = new EditText(context);
            Drawable drawable = tmp.getBackground();
            edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
            pincode.setBackground(drawable);
            name.setBackground(drawable);
            address.setBackground(drawable);
        }
    }

    private void fetchProfile() {
        String token = SharedPreferenceManager.getToken(context);
        VolleyRequestMaker.getUserDetails(context, token, new VolleyRequestMaker.TaskFinishListener<User>() {
            @Override
            public void onSuccess(User response) {
                user = response;
                SharedPreferenceManager.setUser(context, user);
                showProfileContainer();
                stopLoading();
                setUser();
            }

            @Override
            public void onError(String error) {
                stopLoading();
                if (user == null)
                    showBigError(error);
                else Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBigError(String err) {
        error.setVisibility(View.VISIBLE);
        container.setVisibility(View.INVISIBLE);
        errorProgress.setVisibility(GONE);
        errorText.setText(err);
        errorText.setVisibility(View.VISIBLE);
        retry.setVisibility(View.VISIBLE);
        edit.setVisibility(GONE);
    }

    private void showBigLoading() {
        error.setVisibility(View.VISIBLE);
        container.setVisibility(View.INVISIBLE);
        errorText.setVisibility(GONE);
        retry.setVisibility(GONE);
        errorProgress.setVisibility(View.VISIBLE);
        edit.setVisibility(GONE);
    }

    private void showProfileContainer() {
        error.setVisibility(GONE);
        container.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        editProgressBar.setVisibility(View.VISIBLE);
        edit.setVisibility(GONE);
    }

    private void stopLoading() {
        edit.setVisibility(View.VISIBLE);
        editProgressBar.setVisibility(GONE);
    }

    private void changeProfile() {
        String nameText = name.getText().toString();
        String pincodeText = pincode.getText().toString();
        String mobileText = mobile.getText().toString();
        String addressText = address.getText().toString();
        if (!nameText.isEmpty() && pincodeText.length() == 6 && mobileText.length() == 10 && !addressText.isEmpty()) {
            showLoading();
            String token = SharedPreferenceManager.getToken(context);
            int pin = Integer.valueOf(pincodeText);
            final NewUser tmp = new NewUser(nameText, token, addressText, pin);
            VolleyRequestMaker.register(context, tmp, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                @Override
                public void onSuccess(Integer response) {
                    user = new User(tmp.getName(), user.getMobile(), tmp.getAddress(), tmp.getPincode());
                    setEdit(false);
                    setUser();
                    SharedPreferenceManager.setUser(context, user);
                    stopLoading();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    stopLoading();
                }
            });
        }
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
                        loadTransactions();
                        isLoading = true;
                    }
                }
            }
        };
    }

    private void initTransactions() {
        pageOffset = 0;
        isLoading = false;
        recyclerView.addOnScrollListener(scrollListener);
        loadTransactions();
    }

    private void showTransactionError(String error) {
        transactionErrorText.setText(error);
        transactionError.setVisibility(View.VISIBLE);
        transactionContainer.setVisibility(GONE);
        transactionProgress.setVisibility(GONE);
    }

    private void showTransaction() {
        transactionContainer.setVisibility(View.VISIBLE);
        transactionError.setVisibility(GONE);
        transactionProgress.setVisibility(GONE);
    }

    private void showTransactionProgress() {
        transactionProgress.setVisibility(View.VISIBLE);
        transactionContainer.setVisibility(GONE);
        transactionError.setVisibility(GONE);
    }

    private void loadTransactions() {
        if (pageOffset == 0)
            showTransactionProgress();
        isLoading = true;
        String token = SharedPreferenceManager.getToken(context);
        Transaction.Query query = new Transaction.Query(token, pageOffset + 1, ITEMS_TO_LOAD);
        VolleyRequestMaker.getTransactions(context, query, new VolleyRequestMaker.TaskFinishListener<List<Transaction.Response>>() {
            @Override
            public void onSuccess(List<Transaction.Response> response) {
                if (pageOffset == 0 && response.size() == 0)
                    showTransactionError("No transactions found");
                else {
                    initList(response);
                    isLoading = false;
                    ++pageOffset;
                    showTransaction();
                    adapter.setReloadFailed(false);
                    if (response.size() < ITEMS_TO_LOAD) {
                        adapter.setMaxLimitReached(true);
                        recyclerView.removeOnScrollListener(scrollListener);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (pageOffset == 0) {
                    showTransactionError(error);
                }
                Log.d("abcd", error);
                isLoading = false;
                if (adapter != null)
                    adapter.setReloadFailed(true);
            }
        });
    }

    private void initList(List<Transaction.Response> response) {
        if (adapter == null) {
            adapter = new TransactionAdapter(context, response, new TransactionAdapter.ReloadListener() {
                @Override
                public void onReload() {
                    loadTransactions();
                }
            }, new TransactionAdapter.OnClickListener() {
                @Override
                public void onClick(long productId) {
                    openProductView(productId);
                }
            });
            recyclerView.setAdapter(adapter);
        } else adapter.addData(response);
        adapter.notifyDataSetChanged();
    }

    private void openProductView(long productId) {
        Intent intent = new Intent(context, ProductViewActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        startActivity(intent);
    }
}
