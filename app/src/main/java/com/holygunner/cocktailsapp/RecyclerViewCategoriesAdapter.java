package com.holygunner.cocktailsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.values.IngredientsCategoriesNames;

import java.util.List;

public class RecyclerViewCategoriesAdapter extends RecyclerView.Adapter<RecyclerViewCategoriesAdapter
        .CategoryHolder> {
    private List<IngredientsCategory> mIngredientsCategories;
    private IngredientManager mIngredientManager;
    private Context mContext;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private SnapHelper mSnapHelper;
    private IngredientsFragment mFragment;

    RecyclerViewCategoriesAdapter(IngredientsFragment fragment, IngredientManager ingredientManager,
                                  List<IngredientsCategory> ingredientsCategories){
        mFragment = fragment;
        mContext = fragment.getContext();
        mIngredientManager = ingredientManager;
        mIngredientsCategories = ingredientsCategories;
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
        mRecycledViewPool.setMaxRecycledViews(R.id.ingredients_recyclerView,
                IngredientsCategoriesNames.CATEGORIES_NAMES.length);
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.ingredient_section_card,
                parent, false);
        mSnapHelper = new LinearSnapHelper();
        CategoryHolder holder = new CategoryHolder(view);
        holder.mRecyclerView.setRecycledViewPool(mRecycledViewPool);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        final String categoryName = mIngredientsCategories.get(position).getCategoryName();
        List<Ingredient> singleCategoryIngredients = mIngredientsCategories.get(position).getIngredients();
        holder.categoryNameTextView.setText(categoryName);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(mFragment, singleCategoryIngredients,
                mIngredientManager);
        holder.mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,
                false);
        manager.setInitialPrefetchItemCount(3);
        holder.mRecyclerView.setLayoutManager(manager);
        holder.mRecyclerView.setAdapter(ingredientsAdapter);
        if (holder.mRecyclerView.getOnFlingListener() == null)
            mSnapHelper.attachToRecyclerView(holder.mRecyclerView);
//        holder.mRecyclerView.setOnFlingListener(null);
//        mSnapHelper.attachToRecyclerView(holder.mRecyclerView);
    }

    @Override
    public int getItemCount() {
        return mIngredientsCategories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        private TextView categoryNameTextView;
        private RecyclerView mRecyclerView;

        CategoryHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_textView);
            mRecyclerView = itemView.findViewById(R.id.ingredients_recyclerView);
        }
    }
}
