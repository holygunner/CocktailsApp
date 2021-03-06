package com.holygunner.cocktailsapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientManager;
import com.holygunner.cocktailsapp.save.Saver;
import com.holygunner.cocktailsapp.tools.RequestProviderAsyncTask;
import com.holygunner.cocktailsapp.tools.ImageHelper;
import com.holygunner.cocktailsapp.tools.IngredientItemHelper;
import com.holygunner.cocktailsapp.tools.JsonParser;
import com.holygunner.cocktailsapp.tools.RequestProvider;
import com.holygunner.cocktailsapp.tools.ToastBuilder;
import com.holygunner.cocktailsapp.tools.ToolbarHelper;
import com.holygunner.cocktailsapp.tools.URLBuilder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;
import static com.holygunner.cocktailsapp.values.BundleKeys.DRINK_ID_KEY;
import static com.holygunner.cocktailsapp.values.BundleKeys.DRINK_JSON_KEY;

public class DrinkRecipeFragment extends Fragment implements View.OnClickListener {
    private static final String SAVED_DRINK_KEY = "saved_drink_key";
    private static final String IS_FAV_KEY = "is_fav_key";
    private RecyclerView mRecyclerView;
    private ImageView mDrinkImageView;
    private ImageButton mLikeImageButton;
    private CardView mRecipeCardView;
    private CardView mIngredientsListCardView;
    private TextView mDrinkNameTextView;
    private TextView mDrinkRecipeTextView;
    private TextView mServeGlassTextView;
    private IngredientManager mIngredientManager;
    private Set<String> chosenIngredientNames;
    private Drink mDrink;
    private boolean mIsFav;
    private JsonParser mJsonParser;

    @NotNull
    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Context context = Objects.requireNonNull(getContext());
        mIngredientManager = new IngredientManager(context);
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
            savedInstanceState.putBoolean(IS_FAV_KEY, mIsFav);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Objects.requireNonNull(getActivity()).onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.drink_recipe_layout, container, false);
        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar_drink_recipe);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.UP_BUTTON);

        mDrinkImageView = v.findViewById(R.id.drink_imageView);
        mLikeImageButton = v.findViewById(R.id.like_imageButton);
        mLikeImageButton.setOnClickListener(this);
        ViewGroup likeImageButtonContainer = v.findViewById(R.id.like_button_container);
        likeImageButtonContainer.setOnClickListener(this);
        mRecipeCardView = v.findViewById(R.id.recipe_cardView);
        mIngredientsListCardView = v.findViewById(R.id.ingredients_list_cardView);
        mDrinkNameTextView = v.findViewById(R.id.drink_name_textView);
        mDrinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        mDrinkRecipeTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mServeGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mServeGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateSpanCount()));

        final ProgressBar progressBar = v.findViewById(R.id.app_progress_bar);

        if (savedInstanceState != null){
            if (savedInstanceState.getCharArray(SAVED_DRINK_KEY) != null) {
                mIsFav = savedInstanceState.getBoolean(IS_FAV_KEY);
                String drinkJson = new String(Objects
                        .requireNonNull(savedInstanceState.getCharArray(SAVED_DRINK_KEY)));
                setupDrinkRecipe(drinkJson, mIsFav);
            }
        }   else {
            loadDrink(progressBar);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save isFavourite
        if (mDrink != null) {
            Saver.updFavouriteDrinkId(getContext(), mDrink.getId(), mIsFav,
                    mJsonParser.serializeDrinkToJsonBar(mDrink));
        }
    }

    private void setupDrinkRecipe(String drinkJson, Boolean isFav){
        mDrink = mJsonParser.parseJsonToDrinksBar(drinkJson).drinks[0];
        if (isFav != null){
            mIsFav = isFav;
        }   else {
            mIsFav = Saver.isDrinkFavourite(getContext(), mDrink);
        }
        setLikeImageButton();
        setupAdapter(mDrink);
        ImageHelper.downloadImage(mDrink.getUrlImage(), mDrinkImageView);
        mRecipeCardView.setVisibility(View.VISIBLE);
        mIngredientsListCardView.setVisibility(View.VISIBLE);
        mLikeImageButton.setVisibility(View.VISIBLE);
        mDrinkNameTextView.setText(mDrink.getName());
        String recipe = " " + mDrink.getInstruction();
        mDrinkRecipeTextView.setText(recipe);
        String serveGlass = getString(R.string.serve) + " " + mDrink.getGlass();
        mServeGlassTextView.setText(serveGlass);
    }

    @Override
    public void onClick(View v) {
        boolean result = changeLikeImageButtonState();
        if (result) {
            ToastBuilder.getDrinkAddedToast(getContext()).show();
        }   else {
            ToastBuilder.getDrinkRemovedToast(getContext()).show();
        }
    }

    protected static class RecipeProviderTask
            extends RequestProviderAsyncTask<Integer, Integer, String> {

        RecipeProviderTask(Fragment instance) {
            super(instance);
        }

        @Override
        protected String doInBackground(Integer... drinksId) {
            return new RequestProvider().downloadBarJsonById(drinksId[0]);
        }

        @Override
        protected void onPostExecute(String drinkJson){
            super.onPostExecute(drinkJson);
            DrinkRecipeFragment fragment = (DrinkRecipeFragment) super.getFragmentReference().get();

            if (fragment != null) {
                if (drinkJson != null) {
                    if (fragment.isAdded()) {
                        fragment.setupDrinkRecipe(drinkJson, null);
                    }
                }   else {
                    Toast toast = ToastBuilder.getFailedConnectionToast(fragment.getContext());
                    toast.show();
                    Objects.requireNonNull(fragment.getActivity()).onBackPressed();
                }
            }
        }
    }

    private int calculateSpanCount(){
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = 2;
        if (orientation == ORIENTATION_PORTRAIT){
            spanCount = IngredientItemHelper
                    .calculateNumbOfColumns(Objects.requireNonNull(getContext()));
        }
        return spanCount;
    }

    private void loadDrink(@NotNull ProgressBar progressBar){
        int drinkId = Objects.requireNonNull(getActivity())
                .getIntent().getIntExtra(DRINK_ID_KEY, 0);
        String drinkJson = tryToLoadExtraJson();
        if (drinkJson != null){
            setupDrinkRecipe(drinkJson, null);
        }   else {
            RecipeProviderTask task = new RecipeProviderTask(this);
            task.setProgressBar(progressBar);
            task.execute(drinkId);
        }
    }

    private String tryToLoadExtraJson(){
        return Objects.requireNonNull(getActivity())
                .getIntent().getStringExtra(DRINK_JSON_KEY);
    }

    private void setLikeImageButton(){
        if (Saver.isDrinkFavourite(getContext(), mDrink)){
            mLikeImageButton.setImageResource(R.drawable.like_button_pressed);
        }   else {
            mLikeImageButton.setImageResource(R.drawable.like_button);
        }

    }

    private boolean changeLikeImageButtonState(){
        if (!mIsFav){
            mLikeImageButton.setImageResource(R.drawable.like_button_pressed);
            mIsFav = true;
        }   else {
            mLikeImageButton.setImageResource(R.drawable.like_button);
            mIsFav = false;
        }
        return mIsFav;
    }

    private void setupAdapter(Drink drink){
        if (isAdded()){
            List<Ingredient> ingredients = drink.getIngredientsList();
            mRecyclerView.setAdapter(new IngredientsAdapter(ingredients));
        }
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

            if (IngredientManager.ingredientMeasureVerification(ingredient.getMeasure())) {
                text = ingredient.getName() + ": " + ingredient.getMeasure();
            }   else {
                text = ingredient.getName();
            }

            ingredientNameTextView.setText(text);

            if (chosenIngredientNames.contains(ingredient.getName().toLowerCase())){
                IngredientItemHelper.setFillToNameTextView(getContext(),
                        ingredientNameTextView, true);
            }
        }
    }
}
