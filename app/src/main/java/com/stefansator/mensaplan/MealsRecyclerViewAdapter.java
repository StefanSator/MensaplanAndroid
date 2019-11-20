package com.stefansator.mensaplan;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

public class MealsRecyclerViewAdapter extends RecyclerView.Adapter<MealsViewHolder> {

    public interface ItemSelectedListener {
        void itemSelected(Meal item);
    }

    private List<Meal> meals;
    private Context context;
    private ItemSelectedListener listener;

    public MealsRecyclerViewAdapter(List<Meal> meals, Context context, ItemSelectedListener listener) {
        this.meals = meals;
        this.context = context;
        this.listener = listener;
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
        mealsViewHolder.likeCountLabel.setText(String.format(Locale.GERMAN,"%d", meals.get(position).getLikes()));
        mealsViewHolder.dislikeCountLabel.setText(String.format(Locale.GERMAN, "%d", meals.get(position).getDislikes()));
        mealsViewHolder.bind(meals.get(position), listener);
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

    // Insert all items at once to the adapter
    public void insertAll(List<Meal> meals) {
        this.meals = meals;
    }

    // Remove item containing specified Meal object
    public void remove(Meal meal) {
        int position = meals.indexOf(meal);
        if (position != -1) {
            meals.remove(position);
            notifyItemRemoved(position);
        } else {
            System.out.println("Meal to remove is not included in List.");
        }
    }

    // Remove all items in the Data Set
    public void removeAll() {
        for (Meal meal : meals) {
            int position = meals.indexOf(meal);
            notifyItemRemoved(position);
        }
    }
}
