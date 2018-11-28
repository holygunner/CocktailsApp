package com.holygunner.cocktailsapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.DrinkComparator;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.save.Saver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.holygunner.cocktailsapp.save.Saver.CHECKED_INGREDIENTS_KEY;
import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

class BarManager {
    private List<Drink> mSelectedDrinks;
    private Gson mGson;
    private Context mContext;

    BarManager(Context context){
        mContext = context;
        mGson = new Gson();
    }

    Bar getSelectedBar(Bar[] downloadBars){
        mSelectedDrinks = new ArrayList<>();
        Bar deserializedBar = parseJsonToDrinksBar(Saver
                .readSelectedBar(mContext));

        if (deserializedBar != null) {
            deserializedBar.drinks = checkAndRemoveOldDrinks(deserializedBar).toArray(new Drink[0]);
            Log.i("TAG", "CHECKED AND REMOVED PREFS - BAR SIZE = " + deserializedBar.drinks.length);
            mSelectedDrinks.addAll(Arrays.asList(deserializedBar.drinks));
        }

        for (Bar bar: downloadBars){
            selectBars(bar);
        }

        Collections.sort(mSelectedDrinks, new DrinkComparator());

        Bar selectedBar = new Bar();
        Saver.writeIngredients(mContext,
                Saver.readIngredients(mContext, CHOSEN_INGREDIENTS_KEY),
                CHECKED_INGREDIENTS_KEY);

        selectedBar.drinks = mSelectedDrinks.toArray(new Drink[0]);

        Log.i("TAG", "SAVE BAR - BAR SIZE = " + selectedBar.drinks.length);
        Saver.writeSelectedBar(mContext, selectedBar);

        return selectedBar;
    }

    private void selectBars(Bar addedBar){
        if (addedBar != null) {
            Ingredient addedBarIngr = addedBar.drinks[0].getChosenIngredients().get(0);
            List<Drink> addedBarList = new LinkedList<>(Arrays.asList(addedBar.drinks));

            for (Drink existedDrink : mSelectedDrinks){
                Iterator<Drink> iterator = addedBarList.listIterator();

                while (iterator.hasNext()) {
                    Drink matchedDrink = iterator.next();

                    if (existedDrink.equals(matchedDrink)){
                        if (!existedDrink.getChosenIngredients().contains(addedBarIngr)){
                            existedDrink.addChosenIngredient(addedBarIngr);
                        }
                        iterator.remove();
                    }

                }
            }

            mSelectedDrinks.addAll(addedBarList);
        }
    }

    public List<Drink> checkAndRemoveOldDrinks(Bar checkedBar){
        Set<String> removedIngrs = IngredientManager.countRemovedIngredients(
                Saver.readIngredients(mContext, CHOSEN_INGREDIENTS_KEY),
                Saver.readIngredients(mContext, CHECKED_INGREDIENTS_KEY));

        List<Drink> updDrinks = new LinkedList<>();

        if (checkedBar != null) {
            updDrinks = new LinkedList<>(Arrays.asList(checkedBar.drinks));

            for (String removedIngrName : removedIngrs) {
                Iterator<Drink> drinkIterator = updDrinks.listIterator();

                while (drinkIterator.hasNext()){
                    Drink drink = drinkIterator.next();
                    Iterator<Ingredient> ingredientIterator = drink.getChosenIngredients().listIterator();

                    while (ingredientIterator.hasNext()) {
                        Ingredient comparedIngr = ingredientIterator.next();

                        if (comparedIngr.getName().equals(removedIngrName)) {
                            ingredientIterator.remove();
                        }

                        if (drink.getChosenIngredients().size() == 0) {
                            drinkIterator.remove();
                        }
                    }
                }
            }
            checkedBar = new Bar();
            checkedBar.drinks = updDrinks.toArray(new Drink[0]);
        }
        return updDrinks;
    }

    private Bar parseJsonToDrinksBar(String json){
        Bar drinks = mGson.fromJson(json, Bar.class);
        Log.i("TAG", "output: " + json);
        return drinks;
    }
}
