package com.holygunner.cocktailsapp.logic;

import java.util.Comparator;

public class DrinkComparator implements Comparator<Drink> {
    @Override
    public int compare(Drink drink1, Drink drink2) {
        return Integer.compare(drink1.getMatchedIngredientsCount(),
                drink2.getMatchedIngredientsCount());
    }
}
