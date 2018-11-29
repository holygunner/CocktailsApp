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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.holygunner.cocktailsapp.models.Bar;
import com.holygunner.cocktailsapp.models.Drink;
import com.holygunner.cocktailsapp.models.Ingredient;
import com.holygunner.cocktailsapp.save.Saver;

import org.jetbrains.annotations.NotNull;

import static com.holygunner.cocktailsapp.save.Saver.CHECKED_INGREDIENTS_KEY;
import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class DrinksFragment extends Fragment {
    public static final String DRINK_ID_KEY = "drink_id_key";

    private RecyclerView mRecyclerView;
    private List<Drink> mDrinks = new ArrayList<>();
    private BarManager mBarManager;
    private int howMuchChecked;

    @NonNull
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
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
//                false);
//        manager.setInitialPrefetchItemCount(3);
        setDrinks(v);
        setupAdapter();
        return v;
    }

    private void setDrinks(View v){
        // realize callback later if required
        mBarManager = new BarManager(getContext());

        String[] added = IngredientManager.countAddedIngredients(
                Saver.readIngredients(getContext(), CHOSEN_INGREDIENTS_KEY),
                Saver.readIngredients(getContext(), CHECKED_INGREDIENTS_KEY)).toArray(new String[0]);

        howMuchChecked = added.length;

        final ProgressBar progressBar = v.findViewById(R.id.drinks_load_progressBar);
//        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        DrinksProviderTask task = new DrinksProviderTask(this);
        task.setProgressBar(progressBar);

        task.execute(added);
    }

    private void setupAdapter(){
        if (isAdded()){
            mRecyclerView.setAdapter(new DrinksAdapter(mDrinks));
        }
    }

    private class DrinksHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Drink mDrink;
        private TextView drinkNameTextView;
        private TextView drinkChosenIngrsTextView;
        private ImageView drinkImageView;
        private CardView drink_CardView;

        DrinksHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name_TextView);
            drinkChosenIngrsTextView = itemView.findViewById(R.id.drink_chosen_ingrs_TextView);
            drinkImageView = itemView.findViewById(R.id.drink_imageView);
            drink_CardView = itemView.findViewById(R.id.drink_CardView);
            drink_CardView.setOnClickListener(this);
        }

        void bindDrink(Drink drink){
            mDrink = drink;
            drinkImageView.setTag(ImageHelper.downloadImage(drink.getUrlImage(), drinkImageView));
            drinkNameTextView.setText(drink.getName());
            setDrinkChosenIngrsTextView(drink);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), DrinkRecipeActivity.class);
            intent.putExtra(DRINK_ID_KEY, mDrink.getId());
            startActivity(intent);
        }

        private void setDrinkChosenIngrsTextView(@NotNull Drink drink){
            StringBuilder text = new StringBuilder();

            for (Ingredient ingr: drink.getChosenIngredients()){
                text.append(ingr.getName()).append(", ");
            }
            text.delete(text.length()-2, text.length()-1);
            drinkChosenIngrsTextView.setText(text);
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

    protected static class DrinksProviderTask extends AsyncTask<String, Integer, List<Bar>> {
        private WeakReference<DrinksFragment> mReference;
        private WeakReference<ProgressBar> mProgressBarReference;

        DrinksProviderTask(DrinksFragment instance){
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
        protected List<Bar> doInBackground(String... ingredients) {
            return new RequestProvider().downloadBars(ingredients);
        }

        @Override
        protected void onPostExecute(List<Bar> downloadBars){
            DrinksFragment fragment = mReference.get();
            mProgressBarReference.get().setVisibility(View.GONE);

            if (fragment != null) {
                if (fragment.howMuchChecked == downloadBars.size()) {
                    Bar selectedBar = fragment.mBarManager.getSelectedBar(downloadBars);
                    fragment.mDrinks = Arrays.asList(selectedBar.drinks);
                    fragment.setupAdapter();
                }   else {
                    Toast toast = ToastBuilder.getFailedConnectionToast(fragment.getContext());
                    toast.show();

                    Objects.requireNonNull(fragment.getActivity()).onBackPressed();
                }
                fragment.howMuchChecked = 0;
            }
        }

        void setProgressBar(ProgressBar progressBar) {
            mProgressBarReference = new WeakReference<>(progressBar);
        }
    }
}
