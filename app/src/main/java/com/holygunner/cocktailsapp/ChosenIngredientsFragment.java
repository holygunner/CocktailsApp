package com.holygunner.cocktailsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holygunner.cocktailsapp.models.IngredientManager;
import com.holygunner.cocktailsapp.models.IngredientsCategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChosenIngredientsFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;
    private ViewGroup parentLayout;
    private Button removeButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private final int CURRENT_ITEM_ID = R.id.chosen_ingredients;

    @NotNull
    public static Fragment newInstance(){
        return new ChosenIngredientsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chosen_ingredients_layout, container, false);

        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        parentLayout = v.findViewById(R.id.parent_layout);
        removeButton = v.findViewById(R.id.remove_button);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        DrawerMenuHelper.setNavigationMenu(getContext(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
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

    private void setRemoveButtonVisibility(){}

    private void setupAdapter() {
        if (isAdded()){
//
//            mRecyclerView.setAdapter(adapter);
        }
    }
}
