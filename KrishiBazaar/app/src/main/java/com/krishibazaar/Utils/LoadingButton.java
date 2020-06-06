package com.krishibazaar.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.krishibazaar.R;

public class LoadingButton extends FrameLayout {
    private TextView buttonText;
    private ProgressBar progressBar;

    public LoadingButton(Context context) {
        super(context);
        init();
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init() {
        inflate(getContext(), R.layout.custom_hybrid_button, this);
        setBackground(getContext().getResources().getDrawable(R.drawable.login_button_background));
        buttonText = findViewById(R.id.custom_text);
        progressBar = findViewById(R.id.custom_progressbar);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        String text = arr.getString(R.styleable.LoadingButton_name);
        if (text != null)
            setButtonText(text);
        boolean load = arr.getBoolean(R.styleable.LoadingButton_loading, false);
        if (load)
            startProgressBar();
        arr.recycle();
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setButtonText(String text) {
        buttonText.setText(text);
    }

    public void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
