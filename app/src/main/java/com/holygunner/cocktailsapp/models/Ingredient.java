package com.holygunner.cocktailsapp.models;

import org.jetbrains.annotations.Contract;

public class Ingredient {
    private String mName;
    private String mCategory;
    private String mMeasure;
    private boolean mIsFill;

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

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object ingredient) {
        return ingredient instanceof Ingredient
                && this.mName.toLowerCase().equals(((Ingredient) ingredient).mName.toLowerCase());
    }

    public String getCategory() {
        return mCategory;
    }

    public boolean isFill() {
        return mIsFill;
    }

    public void setFill(boolean fill) {
        mIsFill = fill;
    }

    void setCategory(String category) {
        this.mCategory = category;
    }
}
