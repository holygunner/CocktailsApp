package com.holygunner.cocktailsapp.logic;

import java.util.HashSet;
import java.util.Set;

public class Bar {
    public Drink[] drinks;
    private Set<Ingredient> savedIngredients;

    public Bar(){}

    public Set<Ingredient> getSavedIngredients() {
        if (savedIngredients == null){
            savedIngredients = new HashSet<>();
        }
        return savedIngredients;
    }
}
