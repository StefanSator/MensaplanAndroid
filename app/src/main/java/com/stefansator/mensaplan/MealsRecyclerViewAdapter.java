package com.stefansator.mensaplan;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

/**
 * Adapter for managing {@see MealsViewHolder} Objects. Creates and returns new ViewHolder Objects
 * on Request to the RecyclerView or fills them on request with the appropriate data.
 * @author stefansator
 * @version 1.0
 */
public class MealsRecyclerViewAdapter extends RecyclerView.Adapter<MealsViewHolder> {

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

    /** The data set of the RecyclerView. Contains all Mensa meals which should be displayed in the list. */
    private List<Meal> meals;
    /** The current application context. */
    private Context context;
    /** The listener for item selection within the RecyclerView. */
    private ItemSelectedListener listener;

    /**
     * Constructor
     * @param meals List which contains all Mensa Meals.
     * @param context The current application context.
     * @param listener The listener for item selection within the RecyclerView.
     */
    public MealsRecyclerViewAdapter(List<Meal> meals, Context context, ItemSelectedListener listener) {
        this.meals = meals;
        this.context = context;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     */
    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_detail_cell, parent, false);
        // Initialize the View Holder
        MealsViewHolder holder = new MealsViewHolder(view);
        return holder;

    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
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

    /**
     * Returns the size of the dataset (is invoked by the layout manager of the RecyclerView).
     * @return int size of the dataset.
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
     * Insert a new item on the specified position.
     * @param position position to insert item.
     * @param meal Item to insert in data set.
     */
    public void insert(int position, Meal meal) {
        meals.add(position, meal);
        notifyItemInserted(position);
    }

    /**
     * Insert all items at once to the data set of this adapter.
     * @param meals List which contains all meals to display in the list.
     */
    public void insertAll(List<Meal> meals) {
        this.meals = meals;
    }

    /**
     * Remove this meal from the data set of the adapter.
     * @param meal item to remove from data set
     * @return int index of removed item
     */
    public int remove(Meal meal) {
        int position = meals.indexOf(meal);
        if (position != -1) {
            meals.remove(position);
            notifyItemRemoved(position);
        } else {
            System.out.println("Meal to remove is not included in List.");
        }
        return position;
    }

    /**
     * Remove all items in the Data Set.
     */
    public void removeAll() {
        for (Meal meal : meals) {
            int position = meals.indexOf(meal);
            notifyItemRemoved(position);
        }
    }
}
