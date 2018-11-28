package com.holygunner.cocktailsapp;

abstract class URLBuilder {
    private static final String GET_COCKTAILS_LIST_BY_INGREDIENT = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=";
    private static final String COCKTAIL_BY_ID = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private static final String COCKTAIL_BY_NAME = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";

    static String getCocktailsListUrl(String ingredientName){
        return GET_COCKTAILS_LIST_BY_INGREDIENT + underscoresToSpacesIfRequired(ingredientName);
    }

    static String getCocktailDetailsUrl(int id){
        return COCKTAIL_BY_ID + id;
    }

    private static String underscoresToSpacesIfRequired(String name){
        return name.replace(" ", "_");
    }

    static String getMissedIngredientUrl(String name){
        return "https://www.thecocktaildb.com/images/ingredients/" + name + "-Medium.png";
    }
}
