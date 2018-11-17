package com.holygunner.cocktailsapp;

import android.os.AsyncTask;
import android.util.Log;

import com.holygunner.cocktailsapp.models.Drink;

import java.lang.ref.WeakReference;
import java.util.List;

public class DrinksProviderTask extends AsyncTask<String, Void, List<Drink>> {
    private WeakReference<IngredientsFragment> mReference;

    DrinksProviderTask(IngredientsFragment instance){
        mReference = new WeakReference<>(instance);
    }

    @Override
    protected List<Drink> doInBackground(String... ingredients) {
        List<Drink> drinks = new DrinksProvider().selectDrinks(ingredients);
        return drinks;
    }

    @Override
    protected void onPostExecute(List<Drink> selectedDrinks){
        IngredientsFragment fragment = mReference.get();
        fragment.setSelectedDrinks(selectedDrinks);
        // get a data
        Log.i("", "");
    }
}
