package com.holygunner.cocktailsapp.save;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class Saver {
    public static final String CHOSEN_INGREDIENTS_KEY = "chosen_ingredients_key";
    public static final String CHECKED_INGREDIENTS_KEY = "checked_ingredients_key";
    private static final String SELECTED_BAR_KEY = "selected_bar_key";

    public static Set<String> readIngredients(Context context, String key){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(key, new HashSet<String>());
    }

    public static void writeIngredients(Context context, Set<String> ingredients, String key){
        updIngredients(context, ingredients, key);
    }

    @Contract("_ -> !null")
    public static String readSelectedBar(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SELECTED_BAR_KEY, "");
    }

    public static void writeSelectedBar(Context context, Bar selectedBar){
        Gson gson = new Gson();
        String jsonBar = gson.toJson(selectedBar);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SELECTED_BAR_KEY, jsonBar)
                .apply();
    }

    public static Set<String> readChosenIngredientsNamesInLowerCase(Context context, String key){
        Set<String> originalNames = readIngredients(context, key);
        Set<String> lowerCaseNames = new HashSet<>();

        for (String name: originalNames){
            lowerCaseNames.add(name.toLowerCase());
        }

        return lowerCaseNames;
    }

    public static boolean isIngredientExists(Context context, String ingredientName){
        return readIngredients(context, CHOSEN_INGREDIENTS_KEY).contains(ingredientName);
    }

    public static boolean changeChosenIngredient(Context context, String ingredientName){
        Set<String> savedNames = readIngredients(context, Saver.CHOSEN_INGREDIENTS_KEY);

        boolean result;

        if (savedNames.contains(ingredientName)){
            savedNames.remove(ingredientName);
            result = false;
        }   else {
            savedNames.add(ingredientName);
            result = true;
        }
        updIngredients(context, savedNames, CHOSEN_INGREDIENTS_KEY);
        return result;
    }

    private static void updIngredients(Context context, Set<String> savedNames, String key){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(key, savedNames)
                .apply();
    }
}
