package com.holygunner.cocktailsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.save.Saver;
import com.holygunner.cocktailsapp.tools.ImageHelper;
import com.holygunner.cocktailsapp.tools.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.holygunner.cocktailsapp.values.BundleKeys.DRINK_ID_KEY;
import static com.holygunner.cocktailsapp.values.BundleKeys.DRINK_JSON_KEY;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksHolder> {
    private Context mContext;
    private List<Drink> mDrinks;
    private JsonParser mJsonParser;

    DrinksAdapter(Context context, List<Drink> drinks){
        mContext = context;
        mDrinks = drinks;
        mJsonParser = new JsonParser();
    }

    @NonNull
    @Override
    public DrinksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.drink_card_item, parent, false);
        return new DrinksHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinksHolder holder, int position) {
        String drinkPosition = position + 1 + "";
        holder.drinkPositionTextView.setText(drinkPosition);
        holder.bindDrink(mDrinks.get(position));
    }

    @Override
    public int getItemCount() {
        return mDrinks.size();
    }

    protected class DrinksHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Drink mDrink;
        private TextView drinkNameTextView;
        private TextView drinkChosenIngredientsTextView;
        private TextView drinkPositionTextView;
        private ImageView drinkImageView;
        private View mHeartImageViewContainer;

        DrinksHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name_TextView);
            drinkChosenIngredientsTextView
                    = itemView.findViewById(R.id.drink_chosen_ingredients_textView);
            drinkPositionTextView = itemView.findViewById(R.id.drink_position);
            drinkImageView = itemView.findViewById(R.id.drink_imageView);
            mHeartImageViewContainer = itemView.findViewById(R.id.is_drink_liked_container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DrinkRecipeActivity.class);
            intent.putExtra(DRINK_ID_KEY, mDrink.getId());
            if (mDrink.getIngredientsList().size() > 0){
                intent.putExtra(DRINK_JSON_KEY,
                        mJsonParser.serializeDrinkToJsonBar(mDrink));
            }
            mContext.startActivity(intent);
        }

        void bindDrink(Drink drink){
            mDrink = drink;
            setIsFavourite(Saver.isDrinkFavourite(mContext, mDrink));
            drinkImageView.setTag(ImageHelper.downloadImage(drink.getUrlImage(), drinkImageView));
            drinkNameTextView.setText(drink.getName());
            setDrinkChosenIngredientsTextView(drink);
        }

        private void setIsFavourite(boolean isFav){
            if (isFav){
                mHeartImageViewContainer.setVisibility(View.VISIBLE);
            }   else {
                mHeartImageViewContainer.setVisibility(View.GONE);
            }
        }

        private void setDrinkChosenIngredientsTextView(@NotNull Drink drink){
            StringBuilder text = new StringBuilder();

            List<Ingredient> ingredients;

            if (drink.getChosenIngredients().size() > 0){
                ingredients = drink.getChosenIngredients();
            }   else {
                ingredients = drink.getIngredientsList();
            }

            for (Ingredient ingr: ingredients){
                text.append(ingr.getName()).append(", ");
            }

            text.delete(text.length()-2, text.length()-1);
            drinkChosenIngredientsTextView.setText(text);
        }
    }
}
