package com.holygunner.cocktailsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Ingredient;

import java.util.List;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.IngredientHolder> {
    private Context mContext;
    private List<Ingredient> mIngredients;
    private IngredientManager mIngredientManager;

    public DrinksAdapter(Context context, List<Ingredient> ingredients,
                         IngredientManager ingredientManager){
        mContext = context;
        mIngredients = ingredients;
        mIngredientManager = ingredientManager;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ingredient_item, null);
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

        public IngredientHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
        }

        public void bind(Ingredient ingredient){
            ingredientNameTextView.setText(ingredient.getName());
            ingredientImageView.setImageDrawable(mIngredientManager.getIngredientDrawable(ingredient.getCategory(),
                    ingredient.getName()));
        }

        @Override
        public void onClick(View v) {

        }
    }
}
