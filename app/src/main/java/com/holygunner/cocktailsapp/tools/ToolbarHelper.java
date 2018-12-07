package com.holygunner.cocktailsapp.tools;

import android.support.v7.app.ActionBar;

import com.holygunner.cocktailsapp.R;
import com.holygunner.cocktailsapp.SingleFragmentActivity;

import org.jetbrains.annotations.NotNull;

public abstract class ToolbarHelper {
    public static final String UP_BUTTON = "up_button";
    public static final String MENU_BUTTON = "menu_button";

    public static void setToolbar(@NotNull android.support.v7.widget.Toolbar toolbar,
                           @NotNull final SingleFragmentActivity activity, String key){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;

        switch (key) {
            case UP_BUTTON:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                break;
            case MENU_BUTTON:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
                break;
            default:
                throw new IllegalArgumentException("Wrong key");
        }
    }

    public static void setToolbarHome(@NotNull android.support.v7.widget.Toolbar toolbar,
                               @NotNull final SingleFragmentActivity activity){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }
}
