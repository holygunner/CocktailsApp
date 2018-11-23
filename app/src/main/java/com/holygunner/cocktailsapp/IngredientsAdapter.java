package com.holygunner.cocktailsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.save.Saver;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder> {
    private Context mContext;
    private List<Ingredient> mIngredients;
    private IngredientManager mIngredientManager;
//    private IngredientsFragment.DrinksProviderTask mProviderTask;
    private IngredientsFragment mFragment;

    public IngredientsAdapter(IngredientsFragment fragment, List<Ingredient> ingredients,
                              IngredientManager ingredientManager){
        mFragment = fragment;
        mContext = mFragment.getContext();
        mIngredients = ingredients;
        mIngredientManager = ingredientManager;
//        mProviderTask = providerTask;

        mIngredientManager.findIngredientDrawable("Whiskey.png");
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, null);
        return new IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient item = mIngredients.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    protected class IngredientHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView ingredientNameTextView;
        private ImageView ingredientImageView;
        private Ingredient mIngredient;

        public IngredientHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientWithMeasureTextView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientImageView.setOnClickListener(this);
        }

        public void bind(Ingredient ingredient){
            mIngredient = ingredient;
            ingredientNameTextView.setText(ingredient.getName());
            ingredientImageView.setImageDrawable(mIngredientManager.getIngredientDrawable(ingredient.getCategory(),
                    ingredient.getName()));
            setColorFilter(Saver.isIngrediendExists(mContext, ingredient.getName()));
        }

        @Override
        public void onClick(View v) {
//            new DrinkProviderTask(mFragment).execute(mIngredient.getName());

            setColorFilter(Saver.changeChosenIngredientName(mContext, mIngredient.getName()));
        }

        private void setColorFilter(boolean isFill){
            if (isFill){
                ingredientImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorFill));
            }   else {
                ingredientImageView.setColorFilter(null);
            }
        }
    }
}
