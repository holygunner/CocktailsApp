package com.holygunner.cocktailsapp.save;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class Saver {
    public static final String CHOSEN_INGREDIENTS_KEY = "chosen_ingredients_key";
    public static final String CHECKED_INGREDIENTS_KEY = "checked_ingredients_key";
    private static final String SELECTED_BAR_KEY = "selected_bar_key";
    private static final String FAV_DRINKS_ID_SET_KEY = "fav_drinks_id_set_key";
    private static final String USER_AGE_VERIFICATION_KEY = "user_age_verification_key";

    public static boolean readIsVerificationComplete(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(USER_AGE_VERIFICATION_KEY, false);
    }

    public static void writeVerificationComplete(Context context, boolean isUserAdult){
        if (isUserAdult){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(USER_AGE_VERIFICATION_KEY, true)
                    .apply();
        }
    }

    public static Set<String> readFavDrinkIdSet(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(FAV_DRINKS_ID_SET_KEY, new HashSet<String>());
    }

    public static boolean isDrinkFav(Context context, @NonNull Drink drink){
        String drinkId = String.valueOf(drink.getId());

        return isDrinkFav(context, drinkId);
    }

    public static Set<String> readFavDrinksJsons(Context context){
        Set<String> favDrinksJsons = new HashSet<>();

        Set<String> favDrinkIdSet = readFavDrinkIdSet(context);

        for (String drinkId: favDrinkIdSet){
            favDrinksJsons.add(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(drinkId, ""));
        }

        return favDrinksJsons;
    }

    public static void updFavDrinkId(Context context, int id, boolean isFav, String drinkJson){
        String drinkId = String.valueOf(id);
        Set<String> favDrinksIdSet = readFavDrinkIdSet(context);

        if (isFav){
            saveFavDrink(context, drinkId, drinkJson);
            favDrinksIdSet.add(drinkId);
        }   else {
            removeFavDrink(context, drinkId);
            favDrinksIdSet.remove(drinkId);
        }

        updFavDrinksIdSet(context, favDrinksIdSet);
    }

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

    public static boolean updChosenIngredient(Context context, String ingredientName){
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

    private static void updFavDrinksIdSet(Context context, Set<String> updFavDrinksIdSet){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(FAV_DRINKS_ID_SET_KEY, updFavDrinksIdSet)
                .apply();
    }

    private static boolean isDrinkFav(Context context, @NonNull String drinkId){
        return readFavDrinkIdSet(context).contains(drinkId);
    }

    private static void saveFavDrink(Context context, String drinkId, String drinkJson){
        if (!isDrinkFav(context, drinkId)){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(drinkId, drinkJson)
                    .apply();
        }
    }

    private static void removeFavDrink(Context context, String drinkId){
        if (isDrinkFav(context, drinkId)){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit().remove(drinkId)
                    .apply();
        }
    }

    private static void updIngredients(Context context, Set<String> savedNames, String key){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(key, savedNames)
                .apply();
    }
}
