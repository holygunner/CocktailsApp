package com.holygunner.cocktailsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

abstract class ToastBuilder {

    static Toast getFailedConnectionToast(Context context){
        return buildToast(context, R.string.failed_connection);
    }

    static Toast getDrinkAddedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_ok);
    }

    static Toast getDrinkRemovedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_cancel);
    }

    static Toast noChosenIngrsToast(Context context){
        return buildToast(context, R.string.no_chosen_ingredients);
    }

    private static Toast buildToast(Context context, int resId){
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context,
                resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,
                0, 0);
        return toast;
    }
}
