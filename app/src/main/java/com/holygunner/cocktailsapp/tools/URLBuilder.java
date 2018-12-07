package com.holygunner.cocktailsapp.tools;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class URLBuilder {
    private static final String GET_COCKTAILS_LIST_BY_INGREDIENT = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=";
    private static final String COCKTAIL_BY_ID = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private static final String COCKTAIL_BY_NAME = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";

    @NotNull
    static String getCocktailsListUrl(String ingredientName){
        return GET_COCKTAILS_LIST_BY_INGREDIENT + underscoresToSpacesIfRequired(ingredientName);
    }


    @NonNull
    @Contract(pure = true)
    static String getCocktailDetailsUrl(int id){
        return COCKTAIL_BY_ID + id;
    }


    @NonNull
    private static String underscoresToSpacesIfRequired(@NotNull String name){
        return name.replace(" ", "_");
    }

    @NonNull
    @Contract(pure = true)
    public static String getMissedIngredientUrl(String name){
        return "https://www.thecocktaildb.com/images/ingredients/" + name + "-Medium.png";
    }
}
