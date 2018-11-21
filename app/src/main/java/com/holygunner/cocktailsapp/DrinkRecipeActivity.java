package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class DrinkRecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DrinkRecipeFragment.newInstance();
    }
}
