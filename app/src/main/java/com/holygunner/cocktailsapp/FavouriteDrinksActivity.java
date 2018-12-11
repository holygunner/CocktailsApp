package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class FavouriteDrinksActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return FavouriteDrinksFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SelectIngredientsActivity.class));
    }
}
