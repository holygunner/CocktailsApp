package com.holygunner.cocktailsapp;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


abstract class ToolbarHelper {
    public static final String UP_BUTTON = "up_button";
    public static final String MENU_BUTTON = "menu_button";

    static void setToolbar(@NotNull android.support.v7.widget.Toolbar toolbar,
                           @NotNull final SingleFragmentActivity activity, String key){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;

        if (key.equals(UP_BUTTON)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }   else
            if (key.equals(MENU_BUTTON)){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }   else {
                throw new IllegalArgumentException("Wrong key");
            }
    }

    static void setToolBarHome(@NotNull android.support.v7.widget.Toolbar toolbar,
                               @NotNull final SingleFragmentActivity activity){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }
}
