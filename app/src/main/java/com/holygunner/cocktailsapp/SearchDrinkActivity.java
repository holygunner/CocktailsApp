package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class SearchDrinkActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SearchDrinkFragment.newInstance();
    }
}
