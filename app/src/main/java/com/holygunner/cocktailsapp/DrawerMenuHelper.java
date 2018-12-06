package com.holygunner.cocktailsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.os.Handler;

import com.holygunner.cocktailsapp.save.Saver;

abstract class DrawerMenuHelper {

    static void setNavigationMenu(final Context context, final DrawerLayout drawerLayout,
                                  @NonNull final NavigationView navigationView, final int currentItemId){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getItemId() != currentItemId) {
                            Intent intent = null;
                            switch (menuItem.getItemId()) {
                                case R.id.select_ingredients:
                                    intent = new Intent(context,
                                            SelectIngredientsActivity.class);
                                    break;
                                case R.id.chosen_ingredients:
                                    if (isChosenIngrsAvailable(context)) {
                                        intent = new Intent(context,
                                                ChosenIngredientsActivity.class);
                                    }   else {
                                        menuItem.setChecked(false);
                                        drawerLayout.closeDrawers();
                                        return false;
                                    }
                                    break;
                                case R.id.about:
                                    intent = new Intent(context,
                                            SelectedDrinksActivity.class); // only TEST
                            }
                            final Intent finalIntent = intent;

                            if (intent != null) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        context.startActivity(finalIntent);
                                    }
                                }, 200);
                            }

                            drawerLayout.closeDrawers();
                        }
                        return true;
                    }
                });
    }

    private static boolean isChosenIngrsAvailable(Context context){
        boolean result = Saver.readIngredients(context, Saver.CHOSEN_INGREDIENTS_KEY).size() > 0;

        if (result){
            return true;
        }   else {
            ToastBuilder.noChosenIngrsToast(context).show();
            return false;
        }
    }
}
