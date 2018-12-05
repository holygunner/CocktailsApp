package com.holygunner.cocktailsapp;

import android.support.v4.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class FavouriteDrinksFragment extends Fragment {

    @NotNull
    public static Fragment newInstance(){
        return new ChosenIngredientsFragment();
    }
}
