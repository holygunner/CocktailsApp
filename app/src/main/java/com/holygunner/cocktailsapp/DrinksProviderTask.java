package com.holygunner.cocktailsapp;

import android.os.AsyncTask;

import com.holygunner.cocktailsapp.models.Drink;
import java.util.List;

class DrinksProviderTask extends AsyncTask<String, Void, List<Drink>> {
    public DrinksProviderTask(){
    }

    @Override
    protected List<Drink> doInBackground(String... ingredients) {
        return new DrinksProvider().selectDrinks(ingredients);
    }

    @Override
    protected void onPostExecute(List<Drink> selectedDrinks){
        // set drinks with adapter
    }
}
