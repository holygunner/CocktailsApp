package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holygunner.cocktailsapp.models.IngredientsCategory;

import java.util.ArrayList;
import java.util.List;

public class IngredientsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;

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
        Button mixButton = v.findViewById(R.id.mix_button);
        mixButton.setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.ingredients_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
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
