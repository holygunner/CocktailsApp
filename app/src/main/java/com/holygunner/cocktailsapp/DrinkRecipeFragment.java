package com.holygunner.cocktailsapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class DrinkRecipeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ImageView drinkImageView;
    private ImageButton likeImageButton;
    private TextView drinkRecipeTextView;
    private TextView serveGlassTextView;

    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View v = inflater.inflate(R.layout.drink_recipe_layout, container, false);
        drinkImageView = v.findViewById(R.id.drink_imageView);

        String testUrl = "https://www.thecocktaildb.com/images/media/drink/srpxxp1441209622.jpg"; // Just test
        downloadAndSetImageView(testUrl);
//        drinkImageView.setImageResource(R.drawable.drink_test_image);
        likeImageButton = v.findViewById(R.id.like_imageButton);
        likeImageButton.setOnClickListener(this);
        likeImageButton.setPressed(true);

        drinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        setupAdapter();
        return v;
    }

    private void setupAdapter(){
        if (isAdded()){
            List<Ingredient> testIngredients = getTestData();
            mRecyclerView.setAdapter(new IngredientsAdapter(testIngredients));
        }
    }

    private void downloadAndSetImageView(String url){
//        Picasso.get()
//                .load(url)
//                .into(drinkImageView);

        Picasso.get()
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        /* Save the bitmap or do something with it here */

                        //Set it in the ImageView
                        drinkImageView
                                .setImageBitmap(ImageRoundCornersHelper.getRoundedCornerBitmap(bitmap, 32));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
    }

    private List<Ingredient> getTestData(){
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("Vodka", "3 oz");
        Ingredient ingredient2 = new Ingredient("Vodka", "3 oz");
        Ingredient ingredient3 = new Ingredient("Vodka", "3 oz");
        Ingredient ingredient4 = new Ingredient("Vodka", "3 oz");
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
        ingredients.add(ingredient4);
        return ingredients;
    }

    @Override
    public void onClick(View v) {
        likeImageButton.setImageResource(R.drawable.like_button_pressed); // TEST
    }

    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder>{
        List<Ingredient> mIngredients;

        IngredientsAdapter(List<Ingredient> ingredients){
            mIngredients = ingredients;
        }

        @NonNull
        @Override
        public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.ingredient_item_with_measure, parent, false);
            return new IngredientsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {
            holder.bindIngredient(mIngredients.get(position));
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }
    }

    private class IngredientsHolder extends RecyclerView.ViewHolder{
        private ImageView ingredientImageView;
        private TextView ingredientNameTextView;
        private TextView ingredientMeasureTextView;

        IngredientsHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            ingredientMeasureTextView = itemView.findViewById(R.id.ingredientMeasureTextView);
        }

        void bindIngredient(Ingredient ingredient){
            ingredientNameTextView.setText(ingredient.getName());
            ingredientMeasureTextView.setText(ingredient.getMeasure());
        }
    }
}
