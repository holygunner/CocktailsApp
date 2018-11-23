package com.holygunner.cocktailsapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrinkRecipeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ImageView drinkImageView;
    private ImageButton likeImageButton;
    private CardView recipeCardView;
    private TextView drinkNameTextView;
    private TextView drinkRecipeTextView;
    private TextView serveGlassTextView;
    private int drinkId;
    private Drink mDrink;

    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setDrink();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View v = inflater.inflate(R.layout.drink_recipe_layout, container, false);
        drinkImageView = v.findViewById(R.id.drink_imageView);

//        downloadAndSetImageView("https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/nl89tf1518947401.jpg");
//        drinkImageView.setImageResource(R.drawable.drink_test_image);
        likeImageButton = v.findViewById(R.id.like_imageButton);
        likeImageButton.setOnClickListener(this);
        likeImageButton.setPressed(true);
        recipeCardView = v.findViewById(R.id.recipe_cardView);
        drinkNameTextView = v.findViewById(R.id.drink_name_textView);
        drinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        setupAdapter();
        return v;
    }

    private void setDrink(){
        drinkId = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(DrinksFragment.DRINK_ID_KEY, 0);
        new DrinkProviderTask(this).execute(drinkId);
    }

    private void setupAdapter(){
        if (isAdded()){
            List<Ingredient> testIngredients = getTestData();
            mRecyclerView.setAdapter(new IngredientsAdapter(testIngredients));
        }
    }

    private void setupDrinkRecipe(Drink drink){
        downloadAndSetImageView(drink.getUrlImage());
        recipeCardView.setVisibility(View.VISIBLE);
        drinkNameTextView.setText(drink.getName());
        drinkRecipeTextView.setText(" " + drink.getInstruction());
        serveGlassTextView.setText("Serve: " + drink.getGlass());
    }

    private void downloadAndSetImageView(final String url){
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                drinkImageView.setImageBitmap(ImageRoundCornersHelper.getRoundedCornerBitmap(bitmap, 32));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.get()
                .load(url)
                .into(target);

        drinkImageView.setTag(target);
    }

    private List<Ingredient> getTestData(){
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient("Vodka", "4 oz");
        Ingredient ingredient2 = new Ingredient("Milk", "5 gr");
        Ingredient ingredient3 = new Ingredient("Orange Juice", "0.5 L");
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
            View view = inflater.inflate(R.layout.ingredient_item, parent, false);
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

        IngredientsHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientWithMeasureTextView);
        }

        void bindIngredient(Ingredient ingredient){
            ingredientNameTextView.setText(ingredient.getName()
                    + ": " + ingredient.getMeasure());
        }
    }

    protected static class DrinkProviderTask extends AsyncTask<Integer, Void, Drink> {
        private WeakReference<DrinkRecipeFragment> mReference;

        DrinkProviderTask(DrinkRecipeFragment instance){
            mReference = new WeakReference<>(instance);
        }

        @Override
        protected Drink doInBackground(Integer... drinksId) {
            return new DrinksProvider().getDrinkById(drinksId[0]);
        }

        @Override
        protected void onPostExecute(Drink drink){
            DrinkRecipeFragment fragment = mReference.get();
//            fragment.mDrink = drink;
            fragment.setupDrinkRecipe(drink);
        }
    }
}
