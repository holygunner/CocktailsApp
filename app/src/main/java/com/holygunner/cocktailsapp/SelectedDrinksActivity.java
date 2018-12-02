package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class SelectedDrinksActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SelectedDrinksFragment.newInstance();
    }
}
