package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

public class IngredientsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return IngredientsFragment.newInstance();
    }
}