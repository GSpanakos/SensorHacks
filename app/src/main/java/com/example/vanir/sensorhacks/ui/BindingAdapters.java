package com.example.vanir.sensorhacks.ui;

import android.view.View;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
