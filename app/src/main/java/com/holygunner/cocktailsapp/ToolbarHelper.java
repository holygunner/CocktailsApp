package com.holygunner.cocktailsapp;

import android.content.res.Resources;
import android.graphics.PorterDuff;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


abstract class ToolbarHelper {

    static void setToolbarUpButton(
            android.support.v7.widget.Toolbar toolbar, final SingleFragmentActivity activity,
            @NotNull Resources resources){
        Objects.requireNonNull(activity).setSupportActionBar(toolbar);
        Objects.requireNonNull(activity
                .getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(activity
                .getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon())
                .setColorFilter(resources
                        .getColor(R.color.light_color), PorterDuff.Mode.SRC_ATOP);
    }
}
