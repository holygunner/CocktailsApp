package com.holygunner.cocktailsapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

public class IngredientsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return IngredientsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
