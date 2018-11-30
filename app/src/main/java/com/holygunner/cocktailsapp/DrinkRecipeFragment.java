package com.holygunner.cocktailsapp;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.save.Saver;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

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
    private Set<String> chosenIngredientNames;
    private Drink mDrink;
    private JsonParser mJsonParser;

    private static final String SAVED_DRINK_KEY = "saved_drink_key";

    @NotNull
    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        chosenIngredientNames = Saver.readChosenIngredientsNamesInLowerCase(getContext(),
                CHOSEN_INGREDIENTS_KEY);
        mJsonParser = new JsonParser();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if (mDrink != null) {
            savedInstanceState.putCharArray(SAVED_DRINK_KEY,
                    mJsonParser.serializeDrinkToJsonBar(mDrink).toCharArray());
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.drink_recipe_layout, container, false);
        drinkImageView = v.findViewById(R.id.drink_imageView);
        likeImageButton = v.findViewById(R.id.like_imageButton);
        likeImageButton.setOnClickListener(this);
//        likeImageButton.setPressed(true);
        recipeCardView = v.findViewById(R.id.recipe_cardView);
        ingredientsListCardView = v.findViewById(R.id.ingredients_list_cardView);
        drinkNameTextView = v.findViewById(R.id.drink_name_textView);
        drinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getSpanCount()));

        if (savedInstanceState != null){
            if (savedInstanceState.getCharArray(SAVED_DRINK_KEY) != null) {
                String json = new String(Objects
                        .requireNonNull(savedInstanceState.getCharArray(SAVED_DRINK_KEY)));
                Drink drink = mJsonParser.parseJsonToDrinksBar(json).drinks[0];
                setupDrinkRecipe(drink);
            }
        }   else {
            loadDrink(v);
        }

        return v;
    }

    private int getSpanCount(){
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = 2;

        if (orientation == ORIENTATION_PORTRAIT){
            spanCount = 3;
        }
        return spanCount;
    }

    private void loadDrink(@NotNull View v){
        int drinkId = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(DrinksFragment.DRINK_ID_KEY, 0);
        final ProgressBar progressBar = v.findViewById(R.id.recipe_load_progressBar);
//        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        BarProviderTask task = new BarProviderTask(this);
        task.setProgressBar(progressBar);
        task.execute(drinkId);
    }

    private void setupAdapter(Drink drink){
        if (isAdded()){
            List<Ingredient> ingredients = drink.getIngredientsList();
            mRecyclerView.setAdapter(new IngredientsAdapter(ingredients));
        }
    }

    private void setupDrinkRecipe(Drink drink){
        mDrink = drink;
        setupAdapter(drink);
        ImageHelper.downloadImage(drink.getUrlImage(), drinkImageView);
        recipeCardView.setVisibility(View.VISIBLE);
        ingredientsListCardView.setVisibility(View.VISIBLE);
        likeImageButton.setVisibility(View.VISIBLE);
        drinkNameTextView.setText(drink.getName());
        String recipe = " " + drink.getInstruction();
        drinkRecipeTextView.setText(recipe);
        String serveGlass = getString(R.string.serve) + " " + drink.getGlass();
        serveGlassTextView.setText(serveGlass);
    }

    @Override
    public void onClick(View v) {
        Toast toast = ToastBuilder.getDrinkAddedToast(getContext());

        likeImageButton.setImageResource(R.drawable.like_button_pressed); // TEST

        toast.show();
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

    protected static class BarProviderTask extends AsyncTask<Integer, Integer, Drink> {
        private WeakReference<DrinkRecipeFragment> mReference;
        private WeakReference<ProgressBar> mProgressBarReference;

        BarProviderTask(DrinkRecipeFragment instance){
            mReference = new WeakReference<>(instance);
        }

        @Override
        protected void onProgressUpdate(Integer... param) {
            super.onProgressUpdate(param);
            ProgressBar progressBar = mProgressBarReference.get();
            if (progressBar != null) {
                progressBar.setProgress(param[0]);
            }
        }

        @Override
        protected Drink doInBackground(Integer... drinksId) {
            return new RequestProvider().getDrinkById(drinksId[0]);
        }

        @Override
        protected void onPostExecute(Drink drink){
            DrinkRecipeFragment fragment = mReference.get();

            if (mProgressBarReference != null) {
                mProgressBarReference.get().setVisibility(View.GONE);
            }

            if (fragment != null) {
                if (drink != null) {
                    if (fragment.isAdded()) {
                        fragment.setupDrinkRecipe(drink);
                    }
                }   else {
                    Toast toast = ToastBuilder.getFailedConnectionToast(fragment.getContext());
                    toast.show();
                    Objects.requireNonNull(fragment.getActivity()).onBackPressed();
                }
            }
        }

        void setProgressBar(ProgressBar progressBar) {
            mProgressBarReference = new WeakReference<>(progressBar);
        }
    }
}
