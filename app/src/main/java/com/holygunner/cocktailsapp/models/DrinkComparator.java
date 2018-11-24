package com.holygunner.cocktailsapp.models;

import java.util.Comparator;

public class DrinkComparator implements Comparator<Drink> {
    @Override
    public int compare(Drink drink1, Drink drink2) {
        int sizeCompare = Integer.compare(drink2.getChosenIngredients().size(),
                drink1.getChosenIngredients().size());

        if (sizeCompare == 0){
            return (drink1.getName().compareTo(drink2.getName()));
        }   else
            return sizeCompare;
    }
}
