package com.holygunner.cocktailsapp.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.holygunner.cocktailsapp.R;

public abstract class ToastBuilder {

    public static Toast getAgeDisclaimerToast(Context context){
        return buildToast(context, R.string.age_disclaimer);
    }

    public static Toast getFailedConnectionToast(Context context){
        return buildToast(context, R.string.failed_connection);
    }

    public static Toast getDrinkAddedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_ok);
    }

    public static Toast getDrinkRemovedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_cancel);
    }

    public static Toast chosenIngrsListEmptyToast(Context context){
        return buildToast(context, R.string.chosen_ingredients_are_cleared);
    }

    public static Toast favDrinksListEmptyToast(Context context){
        return buildToast(context, R.string.fav_drinks_are_cleared);
    }

    static Toast noChosenIngrsToast(Context context){
        return buildToast(context, R.string.no_chosen_ingredients);
    }

    static Toast noFavDrinksToast(Context context){
        return buildToast(context, R.string.no_fav_drinks);
    }

    private static Toast buildToast(Context context, int resId){
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context,
                resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,
                0, 0);
        return toast;
    }
}
