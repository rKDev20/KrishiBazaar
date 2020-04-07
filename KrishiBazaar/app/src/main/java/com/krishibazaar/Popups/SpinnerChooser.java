package com.krishibazaar.Popups;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

public class SpinnerChooser {
    private BottomSheetDialog dialog;

    public void popup(final Context context, boolean isCategory, int category, FragmentManager manager, final ItemSelectedListener listener) {
        ItemSelectedListener l = new ItemSelectedListener() {
            @Override
            public void onItemSelected(int i, String text, boolean hasSubcategory) {
                listener.onItemSelected(i, text, hasSubcategory);
                dialog.dismiss();
            }
        };
        dialog = new BottomSheetDialog(isCategory, context, category, l);
        dialog.show(manager, "BottomSheet");
    }

    public interface ItemSelectedListener {
        void onItemSelected(int i, String text, boolean hasSubcategory);
    }
}
