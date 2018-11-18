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

    public static boolean isIngrediendExists(Context context, String ingredientName){
        return readChosenIngredientsNames(context).contains(ingredientName);
    }

    public static boolean changeChosenIngredientName(Context context, String ingredientName){
        Set<String> savedNames = readChosenIngredientsNames(context);

        boolean result;

        if (savedNames.contains(ingredientName)){
            savedNames.remove(ingredientName);
            result = false;
        }   else {
            savedNames.add(ingredientName);
            result = true;
        }
        changeChosenIngredientsNames(context, savedNames);
        return result;

//        boolean isAdded = savedNames.add(ingredientName);
//        if (isAdded) {
//            changeChosenIngredientsNames(context, savedNames);
//            Log.i("TAG", "saved Names size: " + savedNames.size());
//        }
    }

    private static void changeChosenIngredientsNames(Context context, Set<String> savedNames){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(INGREDIENTS_KEY, savedNames)
                .apply();
    }

    public static void removeChosenIngredientName(Context context, String ingredientName){
        Set<String> savedNames = readChosenIngredientsNames(context);
        boolean isRemove = savedNames.remove(ingredientName);
        if (isRemove) {
            changeChosenIngredientsNames(context, savedNames);
        }
    }
}
