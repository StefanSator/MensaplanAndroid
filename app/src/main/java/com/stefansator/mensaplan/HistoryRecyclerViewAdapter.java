package com.stefansator.mensaplan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    public interface ItemSelectedListener {
        void itemSelected(Meal item);
    }

    private List<Meal> meals;
    private List<Integer> likes;
    private List<Integer> dislikes;
    private Context context;
    private HistoryRecyclerViewAdapter.ItemSelectedListener listener;

    public HistoryRecyclerViewAdapter(List<Meal> meals, List<Integer> likes, List<Integer> dislikes, Context context, HistoryRecyclerViewAdapter.ItemSelectedListener listener) {
        this.meals = meals;
        this.likes = likes;
        this.dislikes = dislikes;
        this.context = context;
        this.listener = listener;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_dislike_detail_cell, parent, false);
        // Initialize the View Holder
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;

    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(HistoryViewHolder mealsViewHolder, int position) {
        // Use View Holder to populate the current row of the Recycler View
        mealsViewHolder.mealNameLabel.setText(meals.get(position).getName());
        if (likes.contains(meals.get(position).getId())) {
            mealsViewHolder.likeTypeLabel.setText("✓");
            mealsViewHolder.likeTypeLabel.setTextColor(Color.BLUE);
        } else if (dislikes.contains(meals.get(position).getId())) {
            mealsViewHolder.likeTypeLabel.setText("×");
            mealsViewHolder.likeTypeLabel.setTextColor(Color.RED);
        }
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

    // Remove item containing specified Meal object
    public int remove(Meal meal) {
        int position = meals.indexOf(meal);
        if (position != -1) {
            meals.remove(position);
            likes.removeIf(id -> (id == meals.get(position).getId()));
            dislikes.removeIf(id -> (id == meals.get(position).getId()));
            notifyItemRemoved(position);
        } else {
            System.out.println("Meal to remove is not included in List.");
        }
        return position;
    }

    // Insert all items at once to the adapter
    public void insertAll(List<Meal> meals, List<Integer> likes, List<Integer> dislikes) {
        this.meals = meals;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
