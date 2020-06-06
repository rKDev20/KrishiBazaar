package com.krishibazaar.Popups;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.krishibazaar.Models.CategoryInterface;

public class SpinnerChooser {
    private BottomSheetDialog dialog;

    public void popup(final Context context, boolean isCategory, int catId, FragmentManager manager, final ItemChooseListener listener) {
        ItemChooseListener l = category -> {
            listener.onItemSelected(category);
            dialog.dismiss();
        };
        dialog = new BottomSheetDialog(isCategory, context, catId, l);
        dialog.show(manager, "BottomSheet");
    }

    public interface ItemChooseListener<T> {
        void onItemSelected(T category);
    }
}
