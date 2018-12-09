package com.holygunner.cocktailsapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.holygunner.cocktailsapp.tools.ImageHelper;
import com.holygunner.cocktailsapp.tools.IngredientItemHelper;
import com.holygunner.cocktailsapp.tools.JsonParser;
import com.holygunner.cocktailsapp.tools.RequestProvider;
import com.holygunner.cocktailsapp.tools.ToastBuilder;
import com.holygunner.cocktailsapp.tools.ToolbarHelper;
import com.holygunner.cocktailsapp.tools.URLBuilder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private boolean mIsFav;
    private JsonParser mJsonParser;

    private static final String SAVED_DRINK_KEY = "saved_drink_key";
    private static final String IS_FAV_KEY = "is_fav_key";

    @NotNull
    public static Fragment newInstance(){
        return new DrinkRecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Context context = Objects.requireNonNull(getContext());
//        mFavDrinksManager = new FavouriteDrinksManager(context);
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

        drinkImageView = v.findViewById(R.id.drink_imageView);
        likeImageButton = v.findViewById(R.id.like_imageButton);
        likeImageButton.setOnClickListener(this);

        recipeCardView = v.findViewById(R.id.recipe_cardView);
        ingredientsListCardView = v.findViewById(R.id.ingredients_list_cardView);
        drinkNameTextView = v.findViewById(R.id.drink_name_textView);
        drinkRecipeTextView = v.findViewById(R.id.recipe_textView);
        drinkRecipeTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        serveGlassTextView = v.findViewById(R.id.serve_glass_textView);
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateSpanCount()));

        if (savedInstanceState != null){
            if (savedInstanceState.getCharArray(SAVED_DRINK_KEY) != null) {
                mIsFav = savedInstanceState.getBoolean(IS_FAV_KEY);
                String drinkJson = new String(Objects
                        .requireNonNull(savedInstanceState.getCharArray(SAVED_DRINK_KEY)));
                setupDrinkRecipe(drinkJson, mIsFav);
            }
        }   else {
            loadDrink(v);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save isFavourite
        if (mDrink != null) {
            Saver.updFavDrinkId(getContext(), mDrink.getId(), mIsFav,
                    mJsonParser.serializeDrinkToJsonBar(mDrink));
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

    private void loadDrink(@NotNull View v){
        int drinkId = Objects.requireNonNull(getActivity()).getIntent().getIntExtra(SelectedDrinksFragment.DRINK_ID_KEY, 0);
        final ProgressBar progressBar = v.findViewById(R.id.app_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        BarProviderTask task = new BarProviderTask(this);
        task.setProgressBar(progressBar);
        task.execute(drinkId);
    }

    private void setLikeImageButton(){
        if (Saver.isDrinkFav(getContext(), mDrink)){
            likeImageButton.setImageResource(R.drawable.like_button_pressed);
        }   else {
            likeImageButton.setImageResource(R.drawable.like_button_not_pressed);
        }

    }

    private boolean changeLikeImageButtonState(){
        if (!mIsFav){
            likeImageButton.setImageResource(R.drawable.like_button_pressed);
            mIsFav = true;
        }   else {
            likeImageButton.setImageResource(R.drawable.like_button_not_pressed);
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

    private void setupDrinkRecipe(String drinkJson, Boolean isFav){
        mDrink = mJsonParser.parseJsonToDrinksBar(drinkJson).drinks[0];

        if (isFav != null){
            mIsFav = isFav;
        }   else {
            mIsFav = Saver.isDrinkFav(getContext(), mDrink);
        }
        mDrink.setFavourite(mIsFav);
        setLikeImageButton();
        setupAdapter(mDrink);
        ImageHelper.downloadImage(mDrink.getUrlImage(), drinkImageView);
        recipeCardView.setVisibility(View.VISIBLE);
        ingredientsListCardView.setVisibility(View.VISIBLE);
        likeImageButton.setVisibility(View.VISIBLE);
        drinkNameTextView.setText(mDrink.getName());
        String recipe = " " + mDrink.getInstruction();
        drinkRecipeTextView.setText(recipe);
        String serveGlass = getString(R.string.serve) + " " + mDrink.getGlass();
        serveGlassTextView.setText(serveGlass);
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

    protected static class BarProviderTask extends AsyncTask<Integer, Integer, String> {
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
        protected String doInBackground(Integer... drinksId) {
            return new RequestProvider().downloadDrinkJsonById(drinksId[0]);
        }

        @Override
        protected void onPostExecute(String drinkJson){
            DrinkRecipeFragment fragment = mReference.get();

            if (mProgressBarReference.get() != null) {
                mProgressBarReference.get().setVisibility(View.GONE);
            }

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

        void setProgressBar(ProgressBar progressBar) {
            mProgressBarReference = new WeakReference<>(progressBar);
        }
    }
}
