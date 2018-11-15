package com.holygunner.cocktailsapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.values.IngredientsCategoriesNames;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IngredientManager {
    private Context mContext;
    private AssetManager mAssetManager;

    public IngredientManager(Context context){
        mContext = context;
        mAssetManager = mContext.getAssets();
    }

    public List<Ingredient> getIngredientsOfCategory(String category){
        try {
            String[] names = mAssetManager.list(category);
            return namesToIngredients(names, category);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<IngredientsCategory> getAllIngredients(){
        List<IngredientsCategory> allIngredients = new ArrayList<>();
        String[] categoriesNames = IngredientsCategoriesNames.CATEGORIES_NAMES;

        for (String category: categoriesNames){
            allIngredients.add(new IngredientsCategory(category, getIngredientsOfCategory(category)));
        }
        return allIngredients;
    }

    public Drawable getIngredientDrawable(String folderName, String fileName){
        try {
            InputStream inputStream = mAssetManager.open(folderName + "/" + fileName + ".png");
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Ingredient> setTitleElemFirstIfExists(List<Ingredient> ingredients, String category){
        int indx = 0;
        for (int i = 0; i < ingredients.size(); i++){
            if (ingredients.get(i).getName().equals(category)){
                indx = i;
                break;
            }
        }
        if (indx != 0){
            Ingredient titleIngredient = ingredients.get(indx);
            ingredients.remove(indx);
            ingredients.add(0, titleIngredient);
        }
        return ingredients;
    }

    private List<Ingredient> namesToIngredients(String[] fileNames, String category){
        List<Ingredient> ingredients = new LinkedList<>();
        for (String name: fileNames){
            name = name.replace(".png", "");
            Ingredient ingredient = new Ingredient(name);
            ingredient.setCategory(category);
            ingredients.add(ingredient);
        }
        ingredients = setTitleElemFirstIfExists(ingredients, category);
        return ingredients;
    }
}
