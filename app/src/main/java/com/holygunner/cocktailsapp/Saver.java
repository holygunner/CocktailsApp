package com.holygunner.cocktailsapp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class Saver {
    private static final String INGREDIENTS_KEY = "ingredients_key";

    public static Set<String> readChosenIngredientsNames(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(INGREDIENTS_KEY, new HashSet<String>());
    }

    public static void writeChosenIngredientName(Context context, String ingredientName){
        Set<String> savedNames = readChosenIngredientsNames(context);
        boolean isAdded = savedNames.add(ingredientName);
        if (isAdded) {
            changeChosenIngredientsNames(context, savedNames);
            Log.i("TAG", "saved Names size: " + savedNames.size());
        }
    }

    public static void removeChosenIngredientName(Context context, String ingredientName){
        Set<String> savedNames = readChosenIngredientsNames(context);
        boolean isRemove = savedNames.remove(ingredientName);
        if (isRemove) {
            changeChosenIngredientsNames(context, savedNames);
        }
    }

    private static void changeChosenIngredientsNames(Context context, Set<String> savedNames){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(INGREDIENTS_KEY, savedNames)
                .apply();
    }
}
