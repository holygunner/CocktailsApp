package com.holygunner.cocktailsapp.tools;

import android.support.annotation.NonNull;

import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.tools.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestProvider {
    private JsonParser mJsonParser;
    private OkHttpClient mHttpClient;

    public RequestProvider(){
        mJsonParser = new JsonParser();
        mHttpClient = new OkHttpClient();
    }

    public List<Bar> downloadBars(String... ingredients){
        List<Bar> downloadBars = new ArrayList<>();

        for (String checkedIngr : ingredients) {
            String url = URLBuilder.getCocktailsListUrl(checkedIngr);
            String json = downloadJsonByRequest(url);
            Bar bar = mJsonParser.parseJsonToDrinksBar(json);

            if (bar != null) {
                if (bar.drinks.length > 0) {
                    downloadBars.add(bar);

                    for (int j = 0; j < bar.drinks.length; j++) {
                        bar.drinks[j].addChosenIngredient(new Ingredient(checkedIngr));
                    }
                }
            }
        }
        return downloadBars;
    }

    @Nullable
    public Drink downloadDrinkById(Integer drinkId){
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
