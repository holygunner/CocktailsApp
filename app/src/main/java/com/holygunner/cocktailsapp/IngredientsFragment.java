package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.save.Saver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IngredientsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.RecycledViewPool mViewPool;
    private List<Drink> mSelectedDrinks = new ArrayList<>();
    private List<Ingredient> mIngredients = new ArrayList<>();
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;
//    private DrinksProviderTask mProviderTask;
    private Button mixButton;

    public static IngredientsFragment newInstance(){
        return new IngredientsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
        mIngredientManager = new IngredientManager(getContext());
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        mixButton = v.findViewById(R.id.mix_button);
        mixButton.setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.ingredients_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mViewPool = new RecyclerView.RecycledViewPool();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        if (isAdded()){
            RecyclerViewCategoriesAdapter adapter = new RecyclerViewCategoriesAdapter(this,
                    mIngredientManager, mIngredientsCategories);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), DrinksActivity.class);
        startActivity(intent);
    }
}
