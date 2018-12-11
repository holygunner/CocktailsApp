package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class SearchDrinkActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SearchDrinkFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SelectIngredientsActivity.class));
    }
}
