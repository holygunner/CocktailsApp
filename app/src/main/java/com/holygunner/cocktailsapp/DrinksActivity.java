package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class DrinksActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DrinksFragment.newInstance();
    }
}
