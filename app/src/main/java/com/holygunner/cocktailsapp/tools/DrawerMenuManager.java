package com.holygunner.cocktailsapp.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.holygunner.cocktailsapp.AboutActivity;
import com.holygunner.cocktailsapp.ChosenIngredientsActivity;
import com.holygunner.cocktailsapp.FavouriteDrinksActivity;
import com.holygunner.cocktailsapp.R;
import com.holygunner.cocktailsapp.SearchDrinkActivity;
import com.holygunner.cocktailsapp.SelectIngredientsActivity;
import com.holygunner.cocktailsapp.save.Saver;

public class DrawerMenuManager {

    public void setNavigationMenu(final FragmentActivity activity, final DrawerLayout drawerLayout,
                                         @NonNull final NavigationView navigationView,
                                         final int currentItemId){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getItemId() != currentItemId) {
                            Context context = activity.getBaseContext();
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
                                case R.id.search_drink:
                                    intent = new Intent(context,
                                            SearchDrinkActivity.class);
                                    break;
                                case R.id.favourite_drinks:
                                    if (isFavDrinksExist(context)) {
                                        intent = new Intent(context,
                                                FavouriteDrinksActivity.class);
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
//                            setDrawerToggle(intent, activity, drawerLayout);
                            if (intent != null) {
                                final Intent finalIntent = intent;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.startActivity(finalIntent);
                                    }
                                }, 300);
                            }
                            drawerLayout.closeDrawers();
                        }
                        return true;
                    }
                });
    }

//    private void setDrawerToggle(final Intent intent, final Activity activity,
//                                        final DrawerLayout drawerLayout){
//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout,
//                R.string.drawer_open, R.string.drawer_close){
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//
//                activity.getApplicationContext().startActivity(intent);
//
//                Log.i("TAG", "intent is null: " + (intent == null));
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//        };
//        if (intent != null) {
//            drawerLayout.addDrawerListener(drawerToggle);
//        }
//    }

    private boolean isChosenIngrsAvailable(Context context){
        boolean result = Saver.readIngredients(context, Saver.CHOSEN_INGREDIENTS_KEY).size() > 0;

        if (result){
            return true;
        }   else {
            ToastBuilder.noChosenIngrsToast(context).show();
            return false;
        }
    }

    private boolean isFavDrinksExist(Context context){
        boolean result = Saver.readFavDrinkIdSet(context).size() > 0;

        if (result){
            return true;
        }   else {
            ToastBuilder.noFavDrinksToast(context).show();
            return false;
        }
    }
}
