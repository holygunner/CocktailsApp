package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.holygunner.cocktailsapp.models.IngredientManager;
import com.holygunner.cocktailsapp.models.IngredientsCategory;
import com.holygunner.cocktailsapp.save.Saver;
import com.holygunner.cocktailsapp.tools.DrawerMenuManager;
import com.holygunner.cocktailsapp.tools.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.holygunner.cocktailsapp.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class SelectIngredientsFragment extends Fragment implements View.OnClickListener{
    private final int CURRENT_ITEM_ID = R.id.select_ingredients;
    private static final String SELECT_INGREDIENTS_SAVED_STATE_KEY
            = "select_ingredients_saved_state";
    private Parcelable savedRecyclerViewState;
    private RecyclerView mRecyclerView;
    private Button mMixButton;
    private ViewGroup mParentContainer;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;

    @NonNull
    public static SelectIngredientsFragment newInstance(){
        return new SelectIngredientsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        checkIsVerified();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState.getParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.select_ingredients_layout, container, false);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState.getParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY);
        }

        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        mParentContainer = v.findViewById(R.id.parent_layout);
        mMixButton = v.findViewById(R.id.mix_button);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        DrawerMenuManager drawerMenuManager = new DrawerMenuManager();
        drawerMenuManager.setNavigationMenu(getActivity(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mMixButton.setOnClickListener(this);
        mMixButton.setVisibility(!Saver.readIngredients(getContext(),
                CHOSEN_INGREDIENTS_KEY).isEmpty() ? View.VISIBLE : View.INVISIBLE);

        mRecyclerView = v.findViewById(R.id.ingredients_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        setMixButtonVisibility();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY,
                Objects.requireNonNull(mRecyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), SelectedDrinksActivity.class));
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

    public void setMixButtonVisibility(){
        boolean isExist = Saver.readIngredients(getContext(),
                CHOSEN_INGREDIENTS_KEY).isEmpty();
        boolean visibility;

        if ((mMixButton.getVisibility() == View.INVISIBLE) && !isExist){
            visibility = true;
        }   else if
                ((mMixButton.getVisibility() == View.VISIBLE) && isExist){
            visibility = false;
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .setInterpolator(new FastOutLinearInInterpolator());
            TransitionManager.beginDelayedTransition(mParentContainer, set);
            }   else {
            return;
        }

        mMixButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupAdapter() {
        if (isAdded()){
            RecyclerViewCategoriesAdapter adapter = new RecyclerViewCategoriesAdapter(this,
                    mIngredientManager, mIngredientsCategories);
            mRecyclerView.setAdapter(adapter);

            if (savedRecyclerViewState != null){
                Objects.requireNonNull(mRecyclerView.getLayoutManager())
                        .onRestoreInstanceState(savedRecyclerViewState);
            }
        }
    }

    private void checkIsVerified(){
        if (!Saver.readIsVerificationComplete(getContext())) {
            startActivity(new Intent(getContext(), AgeVerificationActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}
