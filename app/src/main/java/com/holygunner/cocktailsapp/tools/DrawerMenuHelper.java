package com.holygunner.cocktailsapp.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.holygunner.cocktailsapp.AboutActivity;
import com.holygunner.cocktailsapp.ChosenIngredientsActivity;
import com.holygunner.cocktailsapp.R;
import com.holygunner.cocktailsapp.SelectIngredientsActivity;
import com.holygunner.cocktailsapp.save.Saver;

public abstract class DrawerMenuHelper {

    public static void setNavigationMenu(final Activity activity, final DrawerLayout drawerLayout,
                                         @NonNull final NavigationView navigationView,
                                         final int currentItemId){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        Context context = activity.getBaseContext();

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
                                            AboutActivity.class);
                            }

                            if (intent != null) {
                                setDrawerToggle(intent, activity, drawerLayout);
                            }
                            drawerLayout.closeDrawers();
                        }
                        return true;
                    }
                });
    }

    private static void setDrawerToggle(final Intent intent, final Activity activity,
                                        final DrawerLayout drawerLayout){
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout,
                R.string.drawer_open, R.string.drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.getApplicationContext().startActivity(intent);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
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
