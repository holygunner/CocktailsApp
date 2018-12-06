package com.holygunner.cocktailsapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class IngredientItemHelper {

    public static void setFillToNameTextView(Context context,
                                             TextView ingredientNameTextView, boolean isFill){
        if (isFill){
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context, R.color.light_color));
            ingredientNameTextView
                    .setBackground(ContextCompat
                            .getDrawable(context, R.drawable.ingredient_name_background));
        }   else {
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context, R.color.textView_color));
            ingredientNameTextView.setBackgroundResource(0);
        }
    }

    public static void setColorFilterToImageView(Context context,
                                                 ImageView ingredientImageView, boolean isFill){
        if (isFill){
            ingredientImageView.setColorFilter(ContextCompat.getColor(context, R.color.ingredientColorFill));
        }   else {
            ingredientImageView.setColorFilter(null);
        }
    }
}
