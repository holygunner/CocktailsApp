package com.holygunner.cocktailsapp.models;

public class Ingredient {
    private String mName;
    private String mCategory;
    private String mMeasure;
    private boolean isMatched;

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

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    @Override
    public boolean equals(Object ingredient) {
        return ingredient instanceof Ingredient
                && this.mName.equals(((Ingredient) ingredient).mName);
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }
}
