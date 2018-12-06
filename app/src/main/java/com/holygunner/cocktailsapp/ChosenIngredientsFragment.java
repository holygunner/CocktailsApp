package com.holygunner.cocktailsapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientManager;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.models.IngredientsComparator;
import com.holygunner.cocktailsapp.save.Saver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChosenIngredientsFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private IngredientsAdapter mIngredientsAdapter;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;
    private ViewGroup parentLayout;
    private Button removeButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Parcelable savedRecyclerViewState;
    private Parcelable savedAdapterState;
    private final int CURRENT_ITEM_ID = R.id.chosen_ingredients;
    private List<Ingredient> mChosenIngrs = new LinkedList<>();
    private ArrayList<Integer> mFilledPositions = new ArrayList<>();

    private static final String CHOSEN_INGRS_SAVED_STATE_KEY = "chosen_ingrs_saved_state_key";
    private static final String FILLED_POSITIONS_SAVED_KEY = "filled_position_saved_key";

    @NotNull
    public static Fragment newInstance(){
        return new ChosenIngredientsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mChosenIngrs = mIngredientManager
                .chosenNameToIngrList(Saver.readIngredients(getContext(),
                        Saver.CHOSEN_INGREDIENTS_KEY));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(CHOSEN_INGRS_SAVED_STATE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chosen_ingredients_layout, container, false);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(CHOSEN_INGRS_SAVED_STATE_KEY);
            mFilledPositions = savedInstanceState.getIntegerArrayList(FILLED_POSITIONS_SAVED_KEY);
        }



        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        parentLayout = v.findViewById(R.id.parent_layout);
        removeButton = v.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(this);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        DrawerMenuHelper.setNavigationMenu(getContext(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mRecyclerView = v.findViewById(R.id.chosen_ingredients_list);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(manager);
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mRecyclerView.getAdapter().notifyDataSetChanged();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(CHOSEN_INGRS_SAVED_STATE_KEY,
                mRecyclerView.getLayoutManager().onSaveInstanceState());

        savedInstanceState.putIntegerArrayList(FILLED_POSITIONS_SAVED_KEY, mFilledPositions);
    }

    @Override
    public void onPause() {
        super.onPause();
        // save new state of chosen ingrs if there was changes
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRemoveButtonVisibility(){}

    private void setupAdapter() {
        if (isAdded()){
            Collections.sort(mChosenIngrs, new IngredientsComparator());
            mIngredientsAdapter = new IngredientsAdapter(mChosenIngrs);
            mRecyclerView.setAdapter(mIngredientsAdapter);
            if (savedRecyclerViewState != null){
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Iterator<Ingredient> iterator = mIngredientsAdapter.mIngredients.listIterator();

        while (iterator.hasNext()){
            Ingredient ingredient = iterator.next();

            if (ingredient.isFill()){
                int position = mIngredientsAdapter.mIngredients.indexOf(ingredient);
                mIngredientsAdapter.notifyItemRemoved(position);
                mIngredientsAdapter.notifyItemRangeChanged(position, mChosenIngrs.size());
                iterator.remove();
                Saver.updChosenIngredient(getContext(), ingredient.getName());
            }
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

    private class IngredientsHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView ingredientImageView;
        private TextView ingredientNameTextView;
        private Ingredient mIngredient;

        IngredientsHolder(View itemView) {
            super(itemView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientTextView);
            ingredientImageView.setOnClickListener(this);
            ingredientNameTextView.setOnClickListener(this);
        }

        void bindIngredient(Ingredient ingredient) {
            mIngredient = ingredient;
            String category = mIngredientManager
                    .findIngredientCategory(ingredient
                            .getName());

            Drawable drawable = mIngredientManager
                    .getIngredientDrawable(category,
                            ingredient.getName());

            ingredientImageView.setImageDrawable(drawable);
            ingredientNameTextView.setText(ingredient.getName());

//            Log.i("", );
            if (mIngredient.isFill()){

                setHolderFill(true);
            }   else {
                setHolderFill(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (mIngredient.isFill()){
                mIngredient.setFill(false);
                setHolderFill(false);
            }   else {
                mIngredient.setFill(true);
                setHolderFill(true);
            }
        }

        private void setHolderFill(boolean isFill){
            IngredientItemHelper.setColorFilterToImageView(getContext(),
                    ingredientImageView, isFill);
            IngredientItemHelper.setFillToNameTextView(getContext(),
                    ingredientNameTextView, isFill);
        }
    }
}
