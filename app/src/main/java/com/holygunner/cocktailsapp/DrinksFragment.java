package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.save.Saver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DrinksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Drink> mDrinks = new ArrayList<>();

    public static DrinksFragment newInstance(){
        return new DrinksFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
        setDrinks();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_drinks_list, container, false);
        mRecyclerView = v.findViewById(R.id.drinks_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    private void setDrinks(){
        Set<String> namesSet = Saver.readChosenIngredientsNames(getContext());
        String[] ingredients = namesSet.toArray(new String[namesSet.size()]);
        // realize callback later if required
        new DrinksProviderTask(this).execute(ingredients);
    }

    private void setupAdapter(){
        if (isAdded()){
//            mDrinks = createTestDrinks();
            mRecyclerView.setAdapter(new DrinksAdapter(mDrinks));
        }
    }


    private class DrinksHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView drinkNameTextView;
        private TextView ingredientsMatchesTextView;
        private CardView drink_CardView;

        DrinksHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name_TextView);
            ingredientsMatchesTextView = itemView.findViewById(R.id.ingredients_matches_TextView);
            drink_CardView = itemView.findViewById(R.id.drink_CardView);
            drink_CardView.setOnClickListener(this);
        }

        public void bindDrink(Drink drink){
            drinkNameTextView.setText(drink.getName());
            ingredientsMatchesTextView.setText(String.valueOf(drink.getChosenIngredients().size()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), DrinkRecipeActivity.class);
            startActivity(intent);
        }
    }

    private class DrinksAdapter extends RecyclerView.Adapter<DrinksHolder>{
        private List<Drink> mDrinks;

        DrinksAdapter(List<Drink> drinks){
            mDrinks = drinks;
        }

        @NonNull
        @Override
        public DrinksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.drink_item, parent, false);
            return new DrinksHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DrinksHolder holder, int position) {
            holder.bindDrink(mDrinks.get(position));
        }

        @Override
        public int getItemCount() {
            return mDrinks.size();
        }
    }

    protected static class DrinksProviderTask extends AsyncTask<String, Void, List<Drink>> {
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
            if (fragment != null) {
                fragment.mDrinks = selectedDrinks;
                fragment.setupAdapter();
            }
        }
    }
}
