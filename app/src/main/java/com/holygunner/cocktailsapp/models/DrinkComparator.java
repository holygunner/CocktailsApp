package com.holygunner.cocktailsapp.models;

import java.util.Comparator;

public class DrinkComparator implements Comparator<Drink> {
    @Override
    public int compare(Drink drink1, Drink drink2) {
        return Integer.compare(drink2.getChosenIngredients().size(),
                drink1.getChosenIngredients().size());
    }
}
