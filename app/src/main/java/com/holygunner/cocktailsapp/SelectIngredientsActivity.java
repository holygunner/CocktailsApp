package com.holygunner.cocktailsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SelectIngredientsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SelectIngredientsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
