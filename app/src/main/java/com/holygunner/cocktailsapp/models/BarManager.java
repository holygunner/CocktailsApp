package com.holygunner.cocktailsapp.models;

import com.holygunner.cocktailsapp.tools.JsonParser;
import com.holygunner.cocktailsapp.save.Saver;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.holygunner.cocktailsapp.save.Saver.CHECKED_INGREDIENTS_KEY;
import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class BarManager {
    private List<Drink> mSelectedDrinks;
    private JsonParser mJsonParser;
    private Context mContext;

    public BarManager(Context context){
        mContext = context;
        mJsonParser = new JsonParser();
    }

    public Bar getSelectedBar(List<Bar> downloadBars){
        mSelectedDrinks = new ArrayList<>();
        Bar deserializedBar = mJsonParser.parseJsonToDrinksBar(Saver
                .readSelectedBar(mContext));

        if (deserializedBar != null) {
            deserializedBar.drinks = checkAndRemoveOldDrinks(deserializedBar).toArray(new Drink[0]);
            mSelectedDrinks.addAll(Arrays.asList(deserializedBar.drinks));
        }

        for (Bar bar: downloadBars){
            selectBars(bar);
        }

        Collections.sort(mSelectedDrinks, new DrinksComparator());
        Bar selectedBar = new Bar();
        Saver.writeIngredients(mContext,
                Saver.readIngredients(mContext, CHOSEN_INGREDIENTS_KEY),
                CHECKED_INGREDIENTS_KEY);
        selectedBar.drinks = mSelectedDrinks.toArray(new Drink[0]);
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

    private List<Drink> checkAndRemoveOldDrinks(Bar checkedBar){
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
}
