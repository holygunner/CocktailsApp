package com.holygunner.cocktailsapp;

import android.os.AsyncTask;

import com.holygunner.cocktailsapp.logic.Drink;
import java.util.List;

class CocktailsProviderTask extends AsyncTask<String, Void, List<Drink>> {
    public CocktailsProviderTask(){
    }

    @Override
    protected List<Drink> doInBackground(String... ingredients) {
        return new CocktailsProvider().selectDrinks(ingredients);
    }

    @Override
    protected void onPostExecute(List<Drink> selectedDrinks){
        // set drinks with adapter
    }
}
