package com.stefansator.mensaplan;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MealsViewHolder extends RecyclerView.ViewHolder {
    ImageView mealImage;
    TextView mealNameLabel;
    TextView mealPrizesLabel;

    MealsViewHolder(View itemView) {
        super(itemView);
        mealImage = (ImageView) itemView.findViewById(R.id.meal_image);
        mealNameLabel = (TextView) itemView.findViewById(R.id.meal_name_label);
        mealPrizesLabel = (TextView) itemView.findViewById(R.id.meal_prizes_label);
    }

    // Binds the Click Listener to the ItemView which holds the ViewHolder
    public void bind(Meal item, MealsRecyclerViewAdapter.ItemSelectedListener listener) {
        // itemView is public attribute in RecyclerView.ViewHolder Class
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemSelected(item);
            }
        });
    }

}
