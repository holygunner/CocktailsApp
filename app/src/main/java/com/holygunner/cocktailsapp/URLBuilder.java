package com.holygunner.cocktailsapp;

public abstract class URLBuilder {
    private static final String GET_COCKTAILS_LIST_BY_INGREDIENT = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=";
    private static final String COCKTAIL_DETAILS_BY_ID = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";

    public static String getCocktailsListUrl(String ingredientName){
        return GET_COCKTAILS_LIST_BY_INGREDIENT + underscoresToSpacesIfRequired(ingredientName);
    }

    public static String getCocktailDetailsUrl(String id){
        return COCKTAIL_DETAILS_BY_ID + id;
    }

    private static String underscoresToSpacesIfRequired(String name){
        return name.replace(" ", "_");
    }
}
