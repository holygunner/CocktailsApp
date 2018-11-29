package com.holygunner.cocktailsapp;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class RequestProvider {
    private Gson mGson;
    private OkHttpClient mHttpClient;

    RequestProvider(){
        mGson = new Gson();
        mHttpClient = new OkHttpClient();
    }

    List<Bar> downloadBars(String... ingredients){
        List<Bar> downloadBars = new ArrayList<>();

        for (int i = 0; i < ingredients.length; i++){
            String url = URLBuilder.getCocktailsListUrl(ingredients[i]);
            String json = getJsonByRequest(url);
            Bar bar = parseJsonToDrinksBar(json);

            if (bar != null) {
                if (bar.drinks.length > 0) {
                    downloadBars.add(bar);

                    for (int j = 0; j < downloadBars.get(i).drinks.length; j++) {
                        downloadBars.get(i).drinks[j].addChosenIngredient(new Ingredient(ingredients[i]));
                    }
                }
            }
        }
        return downloadBars;
    }

    @Nullable
    Drink getDrinkById(Integer drinkId){
        if (drinkId != null){
            String url = URLBuilder.getCocktailDetailsUrl(drinkId);
            Bar bar = parseJsonToDrinksBar(getJsonByRequest(url));

            if (bar == null){
                return null;
            }   else {
                return bar.drinks[0];
            }
        }   else
            return null;
    }

    @NonNull
    private String getJsonByRequest(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            return mHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Bar parseJsonToDrinksBar(String json){
            Bar bar = mGson.fromJson(json, Bar.class);
            Log.i("TAG", "output: " + json);
            return bar;
    }
}
