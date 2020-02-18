package com.stefansator.mensaplan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for managing {@see HistoryViewHolder} Objects. Creates and returns new ViewHolder Objects
 * on Request to the RecyclerView or fills them on request with the appropriate data.
 * @author stefansator
 * @version 1.0
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    /**
     * Interface for implementing a listener for item selection.
     * @author stefansator
     * @version 1.0
     */
    public interface ItemSelectedListener {
        /**
         * Implement this function to define what should happen when a item in list is selected.
         * @param item The meal in the list which was selected.
         */
        void itemSelected(Meal item);
    }

    /** The data set of the RecyclerView. Contains all meals the user has liked or disliked. */
    private List<Meal> meals;
    /** Contains all mealids of the meals the user has liked. */
    private List<Integer> likes;
    /** Contains all mealids of the meals the user has disliked. */
    private List<Integer> dislikes;
    /** The current application context */
    private Context context;
    /** The listener for item selection within the RecyclerView */
    private HistoryRecyclerViewAdapter.ItemSelectedListener listener;

    /**
     * Constructor
     * @param meals List which contains all meals the user has liked or disliked.
     * @param likes List which contains all mealids of the meals the user has liked.
     * @param dislikes List which contains all mealids of the meals the user has disliked.
     * @param context The current application context
     * @param listener The listener for item selection within the RecyclerView
     */
    public HistoryRecyclerViewAdapter(List<Meal> meals, List<Integer> likes, List<Integer> dislikes, Context context, HistoryRecyclerViewAdapter.ItemSelectedListener listener) {
        this.meals = meals;
        this.likes = likes;
        this.dislikes = dislikes;
        this.context = context;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_dislike_detail_cell, parent, false);
        // Initialize the View Holder
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;

    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
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

    /**
     * Returns the size of the dataset (is invoked by the layout manager of the RecyclerView).
     */
    @Override
    public int getItemCount() {
        return meals.size();
    }

    /**
     * Register RecyclerView as a observer of this adapter.
     * @param recyclerView recyclerview to register as a observer
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    /**
     * Remove this meal from the data set of the adapter.
     * @param meal item to remove from data set
     * @return int index of removed item
     */
    public int remove(Meal meal) {
        int position = meals.indexOf(meal);
        System.out.println("Position remove(): " + position);
        System.out.println("SIZE remove(): " + meals.size());
        if (position != -1) {
            likes.removeIf(id -> (id == meals.get(position).getId()));
            dislikes.removeIf(id -> (id == meals.get(position).getId()));
            meals.remove(position);
            notifyItemRemoved(position);
        } else {
            System.out.println("Meal to remove is not included in List.");
        }
        return position;
    }

    /**
     * Insert all items at once to the data set of this adapter.
     * @param meals List which contains all meals the user has liked or disliked.
     * @param likes List which contains all mealids of the meals the user has liked.
     * @param dislikes List which contains all mealids of the meals the user has disliked.
     */
    public void insertAll(List<Meal> meals, List<Integer> likes, List<Integer> dislikes) {
        this.meals = meals;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
