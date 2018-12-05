package com.holygunner.cocktailsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ChosenIngredientsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ChosenIngredientsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}