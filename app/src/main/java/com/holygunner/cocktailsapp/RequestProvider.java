package com.holygunner.cocktailsapp;

import android.util.Log;
import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;

class RequestProvider {
    private Gson mGson;
    private OkHttpClient mHttpClient;

    RequestProvider(){
        mGson = new Gson();
        mHttpClient = new OkHttpClient();
    }

    Bar[] downloadBars(String... ingredients){
        Bar[] downloadBars = new Bar[ingredients.length];

        for (int i = 0; i < ingredients.length; i++){
            String url = URLBuilder.getCocktailsListUrl(ingredients[i]);
            String json = getJsonByRequest(url);
            Bar bar = parseJsonToDrinksBar(json);

            if (bar.drinks.length > 0) {
                downloadBars[i] = bar;

                for (int j = 0; j < downloadBars[i].drinks.length; j++){
                    downloadBars[i].drinks[j].addChosenIngredient(new Ingredient(ingredients[i]));
                }
            }
        }
        return downloadBars;
    }

    Drink getDrinkById(Integer drinkId){
        if (drinkId != null){
            String url = URLBuilder.getCocktailDetailsUrl(drinkId);
            Bar bar = parseJsonToDrinksBar(getJsonByRequest(url));
            assert bar != null;
            return bar.drinks[0];
        }   else
            return null;
    }

    @Nullable
    private String getJsonByRequest(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            return mHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bar parseJsonToDrinksBar(String json){
            Bar bar = mGson.fromJson(json, Bar.class);
            Log.i("TAG", "output: " + json);
            return bar;
    }
}
