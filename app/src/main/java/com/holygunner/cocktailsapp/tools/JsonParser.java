package com.holygunner.cocktailsapp.tools;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;

public class JsonParser {
    private Gson mGson;

    public JsonParser(){
        mGson = new Gson();
    }

    public Bar parseJsonToDrinksBar(String json){
        return mGson.fromJson(json, Bar.class);
    }

    public String serializeDrinkToJsonBar(Drink drink){
        Bar bar = new Bar();
        bar.drinks = new Drink[1];
        bar.drinks[0] = drink;
        return mGson.toJson(bar);
    }
}
