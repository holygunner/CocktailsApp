package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class FavouriteDrinksActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return FavouriteDrinksFragment.newInstance();
    }
}
