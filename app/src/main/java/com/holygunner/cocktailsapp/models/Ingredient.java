package com.holygunner.cocktailsapp.models;

import android.graphics.drawable.Drawable;

public class Ingredient {
    private String mName;
    private String mCategory;
    private String mMeasure;
    private Drawable mDrawable;
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

    @Override
    public boolean equals(Object ingredient) {
        return ingredient instanceof Ingredient
                && this.mName.toLowerCase().equals(((Ingredient) ingredient).mName.toLowerCase());
    }

    public String getCategory() {
        return mCategory;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
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
