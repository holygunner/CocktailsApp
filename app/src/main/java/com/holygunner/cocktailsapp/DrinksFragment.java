package com.holygunner.cocktailsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class DrinksFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Drink> mDrinks = new ArrayList<>();

    public static DrinksFragment newInstance(){
        return new DrinksFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_drinks_list, container, false);
        mRecyclerView = v.findViewById(R.id.drinks_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    private List<Drink> createTestDrinks(){
        List<Drink> drinks = new ArrayList<>();
        Drink drink1 = new Drink("Blue Laguna");
        drink1.addChosenIngredient(new Ingredient("Blue Curasao"));
        Drink drink2 = new Drink("Blood Mary");
        drink2.addChosenIngredient(new Ingredient("Vodka"));
        drink2.addChosenIngredient(new Ingredient("Tomato"));
        drinks.add(drink1);
        drinks.add(drink2);
        return drinks;
    }

    private void setupAdapter(){
        if (isAdded()){
            mDrinks = createTestDrinks();
            mRecyclerView.setAdapter(new DrinksAdapter(mDrinks));
        }
    }


    private class DrinksHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView drinkNameTextView;
        private TextView ingredientsMatchesTextView;

        DrinksHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name_TextView);
            ingredientsMatchesTextView = itemView.findViewById(R.id.ingredients_matches_TextView);
        }

        public void bindDrink(Drink drink){
            drinkNameTextView.setText(drink.getName());
            ingredientsMatchesTextView.setText(String.valueOf(drink.getChosenIngredients().size()));
        }

        @Override
        public void onClick(View v) {}
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
            View view = inflater.inflate(R.layout.single_drink_item, parent, false);
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
}
