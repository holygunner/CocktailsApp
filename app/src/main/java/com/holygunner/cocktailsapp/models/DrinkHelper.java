package com.holygunner.cocktailsapp.models;

import java.util.ArrayList;
import java.util.List;

public abstract class DrinkHelper {

    public static List<Ingredient> convertArrToIngredientList(Ingredient[] ingredients){
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient: ingredients){
            if (ingredient.getName() != null){
                if (!ingredient.getName().equals("")) {
                    ingredientList.add(ingredient);
                }
            }   else {
                break;
            }
        }
        return ingredientList;
    }
}
