package com.stefansator.mensaplan;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Holds the view for a cell in the list of the RecyclerView. Does not fill the cell with data.
 * @author stefansator
 * @version 1.0
 */
public class HistoryViewHolder extends RecyclerView.ViewHolder {
    /** TextView for displaying the type of a like. */
    TextView likeTypeLabel;
    /** TextView for displaying the name of the liked meal */
    TextView mealNameLabel;

    /**
     * Constructor
     * @param itemView The view the ViewHolder will hold.
     */
    HistoryViewHolder(View itemView) {
        super(itemView);
        likeTypeLabel = (TextView) itemView.findViewById(R.id.like_type_label);
        mealNameLabel = (TextView) itemView.findViewById(R.id.meal_name_label);
    }

    /**
     * Binds the Click Listener to the ItemView, which the ViewHolder holds.
     * @param item The meal item that is currently displayed in the view, which the ViewHolder holds.
     * @param listener The listener which should be bound to the view, which the ViewHolder holds.
     */
    public void bind(Meal item, HistoryRecyclerViewAdapter.ItemSelectedListener listener) {
        // itemView is public attribute in RecyclerView.ViewHolder Class
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemSelected(item);
            }
        });
    }
}
