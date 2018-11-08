package com.holygunner.cocktailsapp.logic;

public class Ingredient {
    private String name;
    private String measure;
    private boolean isMatched;

    public Ingredient(String ingredientName, String ingredientMeasure){
        this.name = ingredientName;
        this.measure = ingredientMeasure;
    }

    public Ingredient(String ingredientName){
        this.name = ingredientName;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
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
                && this.name.equals(((Ingredient) ingredient).name);
    }
}
