package com.stefansator.mensaplan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

public class MealsRecyclerViewAdapter extends RecyclerView.Adapter<MealsViewHolder> {
    private List<Meal> meals;
    private Context context;

    public MealsRecyclerViewAdapter(List<Meal> meals, Context context) {
        this.meals = meals;
        this.context = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_detail_cell, parent, false);
        // Initialize the View Holder
        MealsViewHolder holder = new MealsViewHolder(view);
        return holder;

    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(MealsViewHolder mealsViewHolder, int position) {
        // Use View Holder to populate the current row of the Recycler View
        if (meals.get(position).getImageId() != 0) {
            mealsViewHolder.mealImage.setImageResource(meals.get(position).getImageId());
        }
        mealsViewHolder.mealNameLabel.setText(meals.get(position).getName());
        double studentPrize = meals.get(position).getStudentPrize();
        double employeePrize = meals.get(position).getEmployeePrize();
        double guestPrize = meals.get(position).getGuestPrize();
        mealsViewHolder.mealPrizesLabel
                .setText(String.format(Locale.GERMANY,"%f, %f, %f", studentPrize, guestPrize, employeePrize));

    }

    // Returns the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return meals.size();
    }

    // Called by a RecyclerView when it starts observing this Adapter
    // Same Adapter may be observed by multiple RecyclerViews
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    // Insert a new item on specified position
    public void insert(int position, Meal meal) {
        meals.add(position, meal);
        notifyItemInserted(position);
    }

    // Remove item containing specified Meal object
    public void remove(Meal meal) {
        int position = meals.indexOf(meal);
        meals.remove(position);
        notifyItemRemoved(position);
    }

}
