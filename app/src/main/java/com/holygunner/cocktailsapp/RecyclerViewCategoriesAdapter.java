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

import java.util.List;

public class RecyclerViewCategoriesAdapter extends RecyclerView.Adapter<RecyclerViewCategoriesAdapter.CategoryHolder> {
    private List<IngredientsCategory> mIngredientsCategories;
    private IngredientManager mIngredientManager;
    private IngredientsFragment.DrinksProviderTask mProviderTask;
    private Context mContext;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private SnapHelper mSnapHelper;
    private IngredientsFragment mFragment;

    public RecyclerViewCategoriesAdapter(IngredientsFragment fragment, IngredientManager ingredientManager,
                                         List<IngredientsCategory> ingredientsCategories){
        mFragment = fragment;
        mIngredientManager = ingredientManager;
        mIngredientsCategories = ingredientsCategories;
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.ingredient_section_card, null);
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
                mIngredientManager, mProviderTask);
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,
                false));
        holder.mRecyclerView.setAdapter(ingredientsAdapter);
        holder.mRecyclerView.setOnFlingListener(null);
        mSnapHelper.attachToRecyclerView(holder.mRecyclerView);
    }

    @Override
    public int getItemCount() {
        return mIngredientsCategories.size();
    }

    protected class CategoryHolder extends RecyclerView.ViewHolder{
        private TextView categoryNameTextView;
        private RecyclerView mRecyclerView;

        public CategoryHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_textView);
            mRecyclerView = itemView.findViewById(R.id.ingredients_recyclerView);
        }
    }
}
