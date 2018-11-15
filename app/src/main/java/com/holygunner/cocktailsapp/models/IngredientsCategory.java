package com.holygunner.cocktailsapp.models;

import java.util.List;

public class IngredientsCategory {
    private String mCategoryName;
    private List<Ingredient> mIngredients;

    public IngredientsCategory(String categoryName, List<Ingredient> ingredients){
        mCategoryName = categoryName;
        mIngredients = ingredients;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }
}
