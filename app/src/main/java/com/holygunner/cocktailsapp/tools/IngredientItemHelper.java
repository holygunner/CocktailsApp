package com.holygunner.cocktailsapp.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.R;

public abstract class IngredientItemHelper {

    public static void setFillToNameTextView(Context context,
                                             TextView ingredientNameTextView, boolean isFill){
        if (isFill){
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.light_color));
            ingredientNameTextView
                    .setBackground(ContextCompat
                            .getDrawable(context, R.drawable.ingredient_name_background));
        }   else {
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.textView_color));
            ingredientNameTextView.setBackgroundResource(0);
        }
    }

    public static void setColorFilterToImageView(Context context,
                                                 ImageView ingredientImageView, boolean isFill){
        if (isFill){
            ingredientImageView.setColorFilter(ContextCompat.getColor(context,
                    R.color.ingredientColorFill));
        }   else {
            ingredientImageView.setColorFilter(null);
        }
    }

    public static int calculateNumbOfColumns(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / 128);
    }

    public static int calculateNumbOfColumns(@NonNull Context context, int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = width / displayMetrics.density;
        return  (int) (dpWidth / 128);
    }
}
