package com.holygunner.cocktailsapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.values.IngredientsCategoriesNames;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class IngredientManager {
    private AssetManager mAssetManager;

    IngredientManager(Context context){
        mAssetManager = context.getAssets();
    }

    List<IngredientsCategory> getAllIngredients(){
        List<IngredientsCategory> allIngredients = new ArrayList<>();
        String[] categoriesNames = IngredientsCategoriesNames.CATEGORIES_NAMES;

        for (String category: categoriesNames){
            allIngredients.add(new IngredientsCategory(category, getIngredientsOfCategory(category)));
        }
        return allIngredients;
    }

    static Set<String> countAddedIngredients(Set<String> userChosenIngrs,
                                               @NotNull Set<String> checkedIngrs) {
        return countChangedIngredients(userChosenIngrs, checkedIngrs, false);
    }

    static Set<String> countRemovedIngredients(Set<String> userChosenIngrs,
                                               @NotNull Set<String> checkedIngrs) {
        return countChangedIngredients(userChosenIngrs, checkedIngrs, true);
    }

    Drawable getIngredientDrawable(String folderName, String fileName){
        try {
            InputStream inputStream = mAssetManager.open(getRightFileName(folderName, fileName));
            return Drawable.createFromStream(inputStream, null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    String findIngredientCategory(String fileName){
        String ingredientCategory = "";
        try {
            for (String category: IngredientsCategoriesNames.CATEGORIES_NAMES){
                if (Arrays.asList(Objects.requireNonNull(mAssetManager.list(category)))
                        .contains(fileName + ".png")){
                    ingredientCategory = category;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ingredientCategory;
    }

    private static Set<String> countChangedIngredients(Set<String> ingrs1, Set<String> ingrs2,
                                                       boolean isInvertCompare){
        Set<String> returnIngrs = new HashSet<>();

        if (isInvertCompare){
            Set<String> temp = ingrs1;
            ingrs1 = ingrs2;
            ingrs2 = temp;
        }

        for (String ingr: ingrs1){
            if (!ingrs2.contains(ingr)) {
                returnIngrs.add(ingr);
            }
        }
        return returnIngrs;
    }

    private List<Ingredient> getIngredientsOfCategory(String category){
        try {
            String[] names = mAssetManager.list(category);
            assert names != null;
            return namesToIngredients(names, category);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRightFileName(String folderName, String fileName){
        return folderName + "/" + fileName + ".png";
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
