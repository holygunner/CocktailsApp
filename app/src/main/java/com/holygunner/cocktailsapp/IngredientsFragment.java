package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holygunner.cocktailsapp.models.IngredientManager;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.save.Saver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class IngredientsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private Button mMixButton;
    private ViewGroup parentLayout;
    private DrawerLayout mDrawerLayout;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;

    @NonNull
    public static IngredientsFragment newInstance(){
        return new IngredientsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar_from_ingredients_list);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        parentLayout = v.findViewById(R.id.parent_layout);
        mMixButton = v.findViewById(R.id.mix_button);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        NavigationView navigationView = v.findViewById(R.id.nav_view1);
        setNavigationMenu(navigationView);
        mMixButton.setOnClickListener(this);
        mMixButton.setVisibility(!Saver.readIngredients(getContext(),
                CHOSEN_INGREDIENTS_KEY).isEmpty() ? View.VISIBLE : View.INVISIBLE);

        mRecyclerView = v.findViewById(R.id.ingredients_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        setupAdapter();
        return v;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), SelectedDrinksActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationMenu(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.about){
                            startActivity(new Intent(getContext(), SelectedDrinksActivity.class)); // only TEST
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    public void setMixButtonVisibility(){
        boolean isExist = Saver.readIngredients(getContext(),
                CHOSEN_INGREDIENTS_KEY).isEmpty();
        boolean visibility;

        if ((mMixButton.getVisibility() == View.INVISIBLE) && !isExist){
            visibility = true;
        }   else if
                ((mMixButton.getVisibility() == View.VISIBLE) && isExist){
            visibility = false;
            }   else {
            return;
        }

        TransitionSet set = new TransitionSet()
                .addTransition(new Fade())
                .setInterpolator(visibility ? new LinearOutSlowInInterpolator() :
                        new FastOutLinearInInterpolator());

        TransitionManager.beginDelayedTransition(parentLayout, set);
        mMixButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupAdapter() {
        if (isAdded()){
            RecyclerViewCategoriesAdapter adapter = new RecyclerViewCategoriesAdapter(this,
                    mIngredientManager, mIngredientsCategories);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
