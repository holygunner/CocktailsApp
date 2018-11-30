package com.holygunner.cocktailsapp;

import android.support.annotation.NonNull;

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
    private JsonParser mJsonParser;
    private OkHttpClient mHttpClient;

    RequestProvider(){
        mJsonParser = new JsonParser();
        mHttpClient = new OkHttpClient();
    }

    List<Bar> downloadBars(String... ingredients){
        List<Bar> downloadBars = new ArrayList<>();

        for (int i = 0; i < ingredients.length; i++){
            String checkedIngr = ingredients[i];
            String url = URLBuilder.getCocktailsListUrl(checkedIngr);
            String json = downloadJsonByRequest(url);
            Bar bar = mJsonParser.parseJsonToDrinksBar(json);

            if (bar != null) {
                if (bar.drinks.length > 0) {
                    downloadBars.add(bar);

//                    Bar currentBar = downloadBars.get(i);

                    for (int j = 0; j < bar.drinks.length; j++) {
                        bar.drinks[j].addChosenIngredient(new Ingredient(checkedIngr));
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
            Bar bar = mJsonParser.parseJsonToDrinksBar(downloadJsonByRequest(url));

            if (bar == null){
                return null;
            }   else {
                return bar.drinks[0];
            }
        }   else
            return null;
    }

    @NonNull
    private String downloadJsonByRequest(String url){
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
}
