package com.holygunner.cocktailsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.holygunner.cocktailsapp.save.Saver;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DrinkRecipeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ImageView drinkImageView;
    private ImageButton likeImageButton;
    private CardView recipeCardView;
    private CardView ingredientsListCardView;
    private TextView drinkNameTextView;
    private TextView drinkRecipeTextView;
    private TextView serveGlassTextView;
    private IngredientManager mIngredientManager;
    private Drink mDrink;
    private Set<String> chosenIngredientNames;

    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        chosenIngredientNames = Saver.readChosenIngredientsNamesInLowerCase(getContext());
        setDrink();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View v = inflater.inflate(R.layout.drink_recipe_layout, container, false);
        drinkImageView = v.findViewById(R.id.drink_imageView);

        likeImageButton = v.findViewById(R.id.like_imageButton);
        likeImageButton.setOnClickListener(this);
        likeImageButton.setPressed(true);
        recipeCardView = v.findViewById(R.id.recipe_cardView);
        ingredientsListCardView = v.findViewById(R.id.ingredients_list_cardView);
        drinkNameTextView = v.findViewById(R.id.drink_name_textView);
        drinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        setupAdapter();
        return v;
    }

    private void setDrink(){
        int drinkId = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(DrinksFragment.DRINK_ID_KEY, 0);
        new BarProviderTask(this).execute(drinkId);
    }

    private void setupAdapter(Drink drink){
        if (isAdded()){
            List<Ingredient> ingredients = drink.getIngredientsList();
            mRecyclerView.setAdapter(new IngredientsAdapter(ingredients));
        }
    }

    private void setupDrinkRecipe(Drink drink){
        setupAdapter(drink);
        downloadAndSetImageView(drink.getUrlImage());
        recipeCardView.setVisibility(View.VISIBLE);
        ingredientsListCardView.setVisibility(View.VISIBLE);
        drinkNameTextView.setText(drink.getName());
        drinkRecipeTextView.setText(" " + drink.getInstruction());
        serveGlassTextView.setText("Serve: " + drink.getGlass());
    }

    private void downloadAndSetImageView(final String url){
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                drinkImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 32));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        Picasso.get()
                .load(url)
                .into(target);

        drinkImageView.setTag(target);
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
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientTextView);
        }

        void bindIngredient(Ingredient ingredient){
            String category = mIngredientManager
                    .findIngredientCategory(ingredient
                            .getName());

            Drawable drawable = mIngredientManager
                    .getIngredientDrawable(category,
                            ingredient.getName());

            if (drawable != null) {
                ingredientImageView.setImageDrawable(drawable);
            }   else {
                Picasso.get()
                        .load(URLBuilder.getMissedIngredientUrl(ingredient.getName()))
                        .into(ingredientImageView);
            }

            String text;

            if (!ingredient.getMeasure().equals("\n")) {
                text = ingredient.getName() + ": " + ingredient.getMeasure();
            }   else {
                text = ingredient.getName();
            }

            ingredientNameTextView.setText(text);

            if (chosenIngredientNames.contains(ingredient.getName().toLowerCase())){
                Context context = Objects.requireNonNull(getContext());
                ingredientNameTextView.setTextColor(ContextCompat
                        .getColor(context, R.color.light_color));
                ingredientNameTextView
                        .setBackground(ContextCompat
                                .getDrawable(context,
                                        R.drawable.ingredient_name_background));
            }
        }
    }

    protected static class BarProviderTask extends AsyncTask<Integer, Void, Drink> {
        private WeakReference<DrinkRecipeFragment> mReference;

        BarProviderTask(DrinkRecipeFragment instance){
            mReference = new WeakReference<>(instance);
        }

        @Override
        protected Drink doInBackground(Integer... drinksId) {
            return new RequestProvider().getDrinkById(drinksId[0]);
        }

        @Override
        protected void onPostExecute(Drink drink){
            DrinkRecipeFragment fragment = mReference.get();

            if (fragment != null) {
                fragment.mDrink = drink;
                fragment.setupDrinkRecipe(drink);
            }
        }
    }
}
