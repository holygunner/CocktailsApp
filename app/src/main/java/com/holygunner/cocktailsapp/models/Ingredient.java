package com.holygunner.cocktailsapp.models;

public class Ingredient {
    private String mName;
    private String mCategory;
    private String mMeasure;

    public Ingredient(String ingredientName, String ingredientMeasure){
        this.mName = ingredientName;
        this.mMeasure = ingredientMeasure;
    }

    public Ingredient(String ingredientName){
        this.mName = ingredientName;
    }

    public String getName() {
        return mName;
    }

    public String getMeasure() {
        return mMeasure;
    }

    @Override
    public boolean equals(Object ingredient) {
        return ingredient instanceof Ingredient
                && this.mName.toLowerCase().equals(((Ingredient) ingredient).mName.toLowerCase());
    }

    public String getCategory() {
        return mCategory;
    }

    void setCategory(String category) {
        this.mCategory = category;
    }
}
