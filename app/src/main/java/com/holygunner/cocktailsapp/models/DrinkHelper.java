package com.holygunner.cocktailsapp.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract class DrinkHelper {

    static List<Ingredient> convertArrToIngredientList(@NotNull Ingredient[] ingredients){
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
