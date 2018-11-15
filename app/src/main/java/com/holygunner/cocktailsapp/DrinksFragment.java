package com.holygunner.cocktailsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.models.IngredientsCategory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DrinksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.RecycledViewPool mViewPool;
    private List<Drink> mSelectedDrinks = new ArrayList<>();
    private List<Ingredient> mIngredients = new ArrayList<>();
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;

    public static DrinksFragment newInstance(){
        return new DrinksFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
        mIngredientManager = new IngredientManager(getContext());
//        mIngredients = mIngredientManager.getIngredientsOfCategory("Beer");
        mIngredientsCategories = mIngredientManager.getAllIngredients();
        setTestItems();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_cocktails_list, container, false);
        mRecyclerView = v.findViewById(R.id.cocktails_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mViewPool = new RecyclerView.RecycledViewPool();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(new DrinksAdapter(mSelectedDrinks));
        setupAdapter();
        return v;
    }


    private void setupAdapter() {
        if (isAdded()){
//            mRecyclerView.setAdapter(new DrinksAdapter(mIngredients));
            RecyclerViewCategoriesAdapter adapter = new RecyclerViewCategoriesAdapter(getContext(),
                    mIngredientManager, mIngredientsCategories);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void setTestItems(){
        String[] ingredients = new String[]{"Scotch", "Lemon%20peel", "Drambuie", "Ice", "Vodka", "Brandy", "Rum"};
        DrinksProviderTask providerTask = new DrinksProviderTask(this);
        providerTask.execute(ingredients);
    }


//    private class RecyclerViewAdapter extends RecyclerView.Adapter<>
//
//    private class DrinksAdapter extends RecyclerView.Adapter<IngredientHolder> {
//        private List<Ingredient> mIngredients;
//
//        DrinksAdapter(List<Ingredient> ingredients){
//            mIngredients = ingredients;
//        }
//
//        @NonNull
//        @Override
//        public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            View view = inflater.inflate(R.layout.single_ingredient_item, parent, false);
//            return new IngredientHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
//            Ingredient item = mIngredients.get(position);
//            holder.bind(item);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mIngredients.size();
//        }
//    }
//
//    private class IngredientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView ingredientNameTextView;
//        private ImageView ingredientImageView;
//
//        IngredientHolder(View itemView) {
//            super(itemView);
//            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
//            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
//        }
//
//        public void bind(Ingredient ingredient){
//            ingredientNameTextView.setText(ingredient.getName());
//            ingredientImageView.setImageDrawable(mIngredientManager.getIngredientDrawable(ingredient.getCategory(),
//                    ingredient.getName()));
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//    }

    private static class DrinksProviderTask extends AsyncTask<String, Void, List<Drink>> {
        private WeakReference<DrinksFragment> mReference;

        DrinksProviderTask(DrinksFragment instance){
            mReference = new WeakReference<>(instance);
        }

        @Override
        protected List<Drink> doInBackground(String... ingredients) {
            return new DrinksProvider().selectDrinks(ingredients);
        }

        @Override
        protected void onPostExecute(List<Drink> selectedDrinks){
            DrinksFragment fragment = mReference.get();
            fragment.mSelectedDrinks = selectedDrinks;
//            fragment.setupAdapter();
        }
    }

}
