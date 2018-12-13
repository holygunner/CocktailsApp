package com.holygunner.cocktailsapp.tools;

import android.support.annotation.NonNull;

import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestProvider {
    private JsonParser mJsonParser;
    private OkHttpClient mHttpClient;

    private final static String WRONG_RESPONSE = "{\"drinks\":null}";

    public RequestProvider(){
        mJsonParser = new JsonParser();
        mHttpClient = new OkHttpClient();
    }

    public List<Bar> downloadBars(String... ingredients){
        List<Bar> downloadBars = new ArrayList<>();

        for (String checkedIngr : ingredients) {
            String url = URLBuilder.getBarByIngredientUrl(checkedIngr);
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

    public String downloadBarByDrinkName(String drinkName){
        if (isDrinkNameCorrect(drinkName)){
            String url = URLBuilder.getBarByDrinkNameUrl(drinkName);
            String response = downloadJsonByRequest(url);
            if (isResponseCorrect(response)){
                return response;
            }
        }   return null;
    }

    @Contract("null -> false")
    private boolean isDrinkNameCorrect(String drinkName){
        if (drinkName != null){
            return !drinkName.startsWith(" ") && !drinkName.equals("");
        }   else {
            return false;
        }
    }

    @Nullable
    public String downloadBarJsonById(Integer drinkId){
        if (drinkId != null){
            String url = URLBuilder.getCocktailDetailsUrl(drinkId);
            String jsonDrink = downloadJsonByRequest(url);

            if (!isResponseCorrect(jsonDrink)){
                return null;
            }   else {
                return jsonDrink;
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

    private boolean isResponseCorrect(@NonNull String response){
        return !response.equals("") && !response.equals(WRONG_RESPONSE);
    }
}