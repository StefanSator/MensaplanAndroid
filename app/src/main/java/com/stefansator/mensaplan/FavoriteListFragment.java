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

public class FavoriteListFragment extends Fragment {
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
                startActivity(intent);
            }
        });
        mealsRecyclerView.setAdapter(mealsAdapter);

        return view;
    }

    // Private Functions

    // Load all the favorite meals from Shared Preferences
    private void loadAllFavorites() {
        Map<String, ?> mealsMap = sharedPreferences.getAll();
        for (String key : mealsMap.keySet()) {
            Gson gson = new Gson();
            String json = (String) mealsMap.get(key);
            Meal meal = gson.fromJson(json, Meal.class);
            System.out.println(meal);
            favorites.add(meal);
        }
    }
}
