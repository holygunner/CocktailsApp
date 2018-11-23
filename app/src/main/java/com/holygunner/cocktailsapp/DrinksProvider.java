package com.holygunner.cocktailsapp;

import android.util.Log;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.DrinkComparator;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DrinksProvider {
    private List<Drink> chosenDrinks;
    private Set<Ingredient> chosenIngredients;

    DrinksProvider(){
        chosenDrinks = new ArrayList<>();
        chosenIngredients = new HashSet<>();
    }

    List<Drink> selectDrinks(String... ingredients){
        for (String ingredientName: ingredients){
            String url = URLBuilder.getCocktailsListUrl(ingredientName);
            Bar bar = parseJsonToBar(url);
            Ingredient ingredient = new Ingredient(ingredientName);
            selectBars(bar, ingredient);
        }
        Collections.sort(chosenDrinks, new DrinkComparator());
        return chosenDrinks;
    }

    Drink getDrinkById(Integer drinkId){
        if (drinkId != null){
            String url = URLBuilder.getCocktailDetailsUrl(drinkId);
            Bar bar = parseJsonToBar(url);
            assert bar != null;
            return bar.drinks[0];
        }   else
            return null;
    }

    private static Bar parseJsonToBar(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            String json = response.body().string();
            Bar drinks = gson.fromJson(json, Bar.class);
            Log.i("TAG", "output: " + json);
            return drinks;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectBars(Bar addedBar, Ingredient chosenIngredient){
        if (saveChosenIngredient(chosenIngredient) && addedBar != null) {
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
        return chosenIngredients.contains(ingredient);
    }
}
