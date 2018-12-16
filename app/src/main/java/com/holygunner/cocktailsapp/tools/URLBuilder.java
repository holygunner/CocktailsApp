package com.holygunner.cocktailsapp.tools;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class URLBuilder {
    private static final String PERSONAL_API_KEY = "1834";
    private static final String GET_BY_INGR
            = "/filter.php?i=";
    private static final String GET_BY_DRINK_NAME
            = "/search.php?s=";
    private static final String GET_BY_DRINK_ID
            = "/lookup.php?i=";

    @NonNull
    @Contract(pure = true)
    public static String getMissedIngredientUrl(String name){
        return "https://www.thecocktaildb.com/images/ingredients/" + name + "-Medium.png";
    }

    @NotNull
    static String getBarByIngredientUrl(String ingredientName){
        return buildUrl(GET_BY_INGR, underscoresToSpacesIfRequired(ingredientName));
    }

    @Contract(pure = true)
    @NonNull
    static String getBarByDrinkNameUrl(String drinkName){
        return buildUrl(GET_BY_DRINK_NAME, drinkName);
    }

    @NonNull
    @Contract(pure = true)
    static String getCocktailDetailsUrl(int id){
        return buildUrl(GET_BY_DRINK_ID, String.valueOf(id));
    }

    @NonNull
    @Contract(pure = true)
    private static String buildUrl(String requestType, String request){
        return "https://www.thecocktaildb.com/api/json/v1/" + PERSONAL_API_KEY + requestType
                + request;
    }

    @NonNull
    private static String underscoresToSpacesIfRequired(@NotNull String name){
        return name.replace(" ", "_");
    }
}
