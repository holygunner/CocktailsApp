package com.holygunner.cocktailsapp;

import android.os.AsyncTask;

import com.holygunner.cocktailsapp.logic.Bar;
import com.holygunner.cocktailsapp.logic.Drink;
import com.holygunner.cocktailsapp.logic.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class CocktailsProviderTask extends AsyncTask<String, Void, List<Drink>> {
    private List<Drink> chosenDrinks;
    private Set<Ingredient> chosenIngredients;


    public CocktailsProviderTask(){
//        chosenDrinks = new ArrayList<>();
//        chosenIngredients = new HashSet<>();
    }

    @Override
    protected List<Drink> doInBackground(String... ingredients) {

//        for (String ingredientName: ingredients){
//            String url = URLBuilder.getCocktailsListUrl(ingredientName);
//            Bar bar = CocktailsProvider.parseJsonToBar(url);
//            Ingredient ingredient = new Ingredient(ingredientName);
//            compareBars(bar, ingredient);
//        }
//        return chosenDrinks;
        return new CocktailsProvider().selectDrinks(ingredients);
    }

    @Override
    protected void onPostExecute(List<Drink> selectedDrinks){
        // set drinks with adapter
    }



    private void compareBars(Bar addedBar, Ingredient chosenIngredient){
        if (saveChosenIngredient(chosenIngredient)) {
            List<Drink> addedBarList = new ArrayList<>(Arrays.asList(addedBar.drinks));

            for (Drink drink : addedBarList) {
                drink.addChosenIngredient(chosenIngredient);
            }

            for (Drink existedDrink : chosenDrinks) {
                Iterator<Drink> iterator = addedBarList.listIterator();

                while (iterator.hasNext()) {
                    Drink matchedDrink = iterator.next();
                    if (matchedDrink.equals(existedDrink)) {
                        existedDrink.addChosenIngredient(chosenIngredient);
                        iterator.remove();
                    }
                }
            }
            chosenDrinks.addAll(addedBarList);
        }
    }

    private boolean saveChosenIngredient(Ingredient ingredient){
        if (!isChosenIngredientExists(ingredient)){
            chosenIngredients.add(ingredient);
            return true;
        }
        return false;
    }

    private boolean isChosenIngredientExists(Ingredient ingredient){
        if (chosenIngredients.contains(ingredient)){
            return true;
        }   else
            return false;
    }
}
