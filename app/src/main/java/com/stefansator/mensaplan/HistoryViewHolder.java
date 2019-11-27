package com.stefansator.mensaplan;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    TextView likeTypeLabel;
    TextView mealNameLabel;

    HistoryViewHolder(View itemView) {
        super(itemView);
        likeTypeLabel = (TextView) itemView.findViewById(R.id.like_type_label);
        mealNameLabel = (TextView) itemView.findViewById(R.id.meal_name_label);
    }

    // Binds the Click Listener to the ItemView which holds the ViewHolder
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
