package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;

    public static IngredientsFragment newInstance(){
        return new IngredientsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        parentLayout = v.findViewById(R.id.parent_layout);
        mMixButton = v.findViewById(R.id.mix_button);
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
        startActivity(new Intent(getContext(), DrinksActivity.class));
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
