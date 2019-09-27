package com.stefansator.mensaplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteListFragment extends Fragment implements ChangedFavoritesDelegate {
    static final int INTENT_REQUEST_CODE = 1;
    private List<Meal> favorites = new ArrayList<Meal>();
    private RecyclerView mealsRecyclerView;
    private MealsRecyclerViewAdapter mealsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        // Get Handle to Shared Preferences Object
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        // Load all Favorite Meals from User
        loadAllFavorites();

        // Obtain Handle to the RecyclerView
        mealsRecyclerView = (RecyclerView) view.findViewById(R.id.meals_recycler_view);
        // improve Performance of RecyclerView if Layout has always fixed size during Execution
        mealsRecyclerView.setHasFixedSize(true);
        // Set LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mealsRecyclerView.setLayoutManager(layoutManager);
        // Set Adapter for RecyclerView
        mealsAdapter = new MealsRecyclerViewAdapter(favorites, getActivity().getApplicationContext(), new MealsRecyclerViewAdapter.ItemSelectedListener() {
            @Override
            public void itemSelected(Meal item) {
                // Open Meal Detail Dialog Window
                Intent intent = new Intent(getActivity().getApplicationContext(), MealDetailActivity.class);
                intent.putExtra("Meal", item);
                MealDetailActivity.DELEGATE = FavoriteListFragment.this;
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });
        mealsRecyclerView.setAdapter(mealsAdapter);

        return view;
    }

    // Delegate Pattern
    @Override
    public void changesInFavorites(boolean changes, Meal meal) {
        if (changes) {
            Meal mealToRemove = removeFavorite(meal);
            mealsAdapter.remove(mealToRemove);
            mealsAdapter.notifyDataSetChanged();
        }
    }

    // Private Functions

    // Load all the favorite meals from Shared Preferences
    private void loadAllFavorites() {
        favorites = new ArrayList<Meal>();
        Map<String, ?> mealsMap = sharedPreferences.getAll();
        for (String key : mealsMap.keySet()) {
            Gson gson = new Gson();
            String json = (String) mealsMap.get(key);
            Meal meal = gson.fromJson(json, Meal.class);
            favorites.add(meal);
        }
    }

    // Remove the Meal from Favorites List
    private Meal removeFavorite(Meal favoriteToDelete) {
        Meal removedMeal = favorites.stream().filter(meal -> favoriteToDelete.getName().equals(meal.getName()))
                .findAny()
                .orElse(null);
        favorites.remove(removedMeal);
        return removedMeal;
    }

    /*
    Customer james = customers.stream()
  .filter(customer -> "James".equals(customer.getName()))
  .findAny()
  .orElse(null);
     */
}
