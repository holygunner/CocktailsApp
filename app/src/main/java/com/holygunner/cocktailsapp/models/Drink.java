package com.holygunner.cocktailsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    @SerializedName("idDrink")
    private int id; // idDrink
    @SerializedName("strDrink")
    private String name; // strDrink
    @SerializedName("strCategory")
    private String category; // strCategory
    @SerializedName("strGlass")
    private String glass; // strGlass
    @SerializedName("strInstructions")
    private String instruction; // strInstructions
    @SerializedName("strDrinkThumb")
    private String urlImage; //strDrinkThumb
    private String strAlcoholic;

    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;

    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;

    private Ingredient[] mIngredients;
    private List<Ingredient> chosenIngredients;

    public Drink(){
        chosenIngredients = new ArrayList<>();
    }

    // demo only
    public Drink(String name){
        this.name = name;
        chosenIngredients = new ArrayList<>();
    }

    private void initIngredients(){
        mIngredients = new Ingredient[10];
        mIngredients[0] = new Ingredient(strIngredient1, strMeasure1);
        mIngredients[1] = new Ingredient(strIngredient2, strMeasure2);
        mIngredients[2] = new Ingredient(strIngredient3, strMeasure3);
        mIngredients[3] = new Ingredient(strIngredient4, strMeasure4);
        mIngredients[4] = new Ingredient(strIngredient5, strMeasure5);
        mIngredients[5] = new Ingredient(strIngredient6, strMeasure6);
        mIngredients[6] = new Ingredient(strIngredient7, strMeasure7);
        mIngredients[7] = new Ingredient(strIngredient8, strMeasure8);
        mIngredients[8] = new Ingredient(strIngredient9, strMeasure9);
        mIngredients[9] = new Ingredient(strIngredient10, strMeasure10);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAlcoholic() {
        return strAlcoholic.equals("Alcoholic");
    }

    public String getGlass() {
        return glass;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Ingredient[] getIngredients() {
        if (mIngredients == null){
            initIngredients();
        }
        return mIngredients;
    }

    public int getMatchedIngredientsCount(){
        int count = 0;

        for (Ingredient ingredient: mIngredients){
            if (ingredient == null){
                return count;
            }

            if (ingredient.isMatched()){
               count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Drink && this.id == ((Drink) object).id;
    }

    public List<Ingredient> getChosenIngredients() {
        return chosenIngredients;
    }

    public void addChosenIngredient(Ingredient ingredient){
        chosenIngredients.add(ingredient);
    }
}
