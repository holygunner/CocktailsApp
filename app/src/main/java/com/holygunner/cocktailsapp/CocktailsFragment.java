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
import android.widget.TextView;

import com.holygunner.cocktailsapp.logic.Drink;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CocktailsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Drink> mItems = new ArrayList<>();

    public static CocktailsFragment newInstance(){
        return new CocktailsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setTestItems();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_cocktails_list, container, false);
        mRecyclerView = v.findViewById(R.id.cocktails_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setAdapter(new CocktailsAdapter(mItems));
        return v;
    }

    private void setupAdapter() {
        if (isAdded()){
            mRecyclerView.setAdapter(new CocktailsAdapter(mItems));
        }
    }

    private void setTestItems(){
        String url1 = "Scotch";
        String url2 = "Lemon%20peel";
        String url3 = "Drambuie";
        CocktailsProviderTask providerTask = new CocktailsProviderTask(this);
        providerTask.execute(url1, url2, url3);
    }

    private class CocktailsAdapter extends RecyclerView.Adapter<WeatherHolder> {
        private List<Drink> mDrinks;

        CocktailsAdapter(List<Drink> drinks){
            mDrinks = drinks;
        }

        @NonNull
        @Override
        public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.cocktail_item, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
            Drink item = mDrinks.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mDrinks.size();
        }
    }

    private class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView drinkNameTextView;
        private TextView ingredientsCountTextView;

        WeatherHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
            ingredientsCountTextView = itemView.findViewById(R.id.ingredientsCountTextView);
        }

        public void bind(Drink drink){
            drinkNameTextView.setText(drink.getName());
            ingredientsCountTextView.setText(String.valueOf(drink.getChosenIngredients().size()));
        }

        @Override
        public void onClick(View v) {

        }
    }

    private static class CocktailsProviderTask extends AsyncTask<String, Void, List<Drink>> {
        private WeakReference<CocktailsFragment> mReference;

        CocktailsProviderTask(CocktailsFragment instance){
            mReference = new WeakReference<>(instance);
        }

        @Override
        protected List<Drink> doInBackground(String... ingredients) {
            return new CocktailsProvider().selectDrinks(ingredients);
        }

        @Override
        protected void onPostExecute(List<Drink> selectedDrinks){
            CocktailsFragment fragment = mReference.get();
            fragment.mItems = selectedDrinks;
            fragment.setupAdapter();
        }
    }

}
