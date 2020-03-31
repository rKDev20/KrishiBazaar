package com.krishibazaar.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.krishibazaar.R;

public class CustomButtonManager
{
    private TextView buttonText;
    private ProgressBar progressBar;
    private ConstraintLayout layout;

    public CustomButtonManager(View view) {
        buttonText = view.findViewById(R.id.custom_text);
        progressBar=view.findViewById(R.id.custom_progressbar);
        layout=view.findViewById(R.id.custom_button);
        layout.setPadding(40,10,40,10);
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void setButtonText(String text)
    {
        buttonText.setText(text);
    }
    public void startProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
        setMargins(buttonText,0,0,20,0);
    }
    public void stopProgressBar()
    {
        progressBar.setVisibility(View.GONE);
        setMargins(buttonText,0,0,0,0);
    }
}
