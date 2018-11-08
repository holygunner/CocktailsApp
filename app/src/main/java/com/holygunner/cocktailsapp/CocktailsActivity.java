package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class CocktailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return CocktailsFragment.newInstance();
    }
}
