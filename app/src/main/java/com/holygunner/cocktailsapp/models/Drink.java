package com.holygunner.cocktailsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    @SerializedName("idDrink")
    private int id;
    @SerializedName("strDrink")
    private String name;
    @SerializedName("strCategory")
    private String category;
    @SerializedName("strGlass")
    private String glass;
    @SerializedName("strInstructions")
    private String instruction;
    @SerializedName("strDrinkThumb")
    private String urlImage;
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

    private List<Ingredient> mIngredientsList;
    private List<Ingredient> chosenIngredients;

    public Drink(){
        chosenIngredients = new ArrayList<>();
    }

    private void initIngredients(){
        Ingredient[] ingredients = new Ingredient[10];
        ingredients[0] = new Ingredient(strIngredient1, strMeasure1);
        ingredients[1] = new Ingredient(strIngredient2, strMeasure2);
        ingredients[2] = new Ingredient(strIngredient3, strMeasure3);
        ingredients[3] = new Ingredient(strIngredient4, strMeasure4);
        ingredients[4] = new Ingredient(strIngredient5, strMeasure5);
        ingredients[5] = new Ingredient(strIngredient6, strMeasure6);
        ingredients[6] = new Ingredient(strIngredient7, strMeasure7);
        ingredients[7] = new Ingredient(strIngredient8, strMeasure8);
        ingredients[8] = new Ingredient(strIngredient9, strMeasure9);
        ingredients[9] = new Ingredient(strIngredient10, strMeasure10);

        mIngredientsList = DrinkHelper.convertArrToIngredientList(ingredients);
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


    public List<Ingredient> getIngredientsList(){
        if (mIngredientsList == null){
            initIngredients();
        }
        return mIngredientsList;
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
