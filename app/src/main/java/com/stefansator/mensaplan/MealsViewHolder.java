package com.stefansator.mensaplan;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MealsViewHolder extends RecyclerView.ViewHolder {
    ImageView mealImage;
    TextView mealNameLabel;
    TextView likeCountLabel;
    TextView dislikeCountLabel;

    MealsViewHolder(View itemView) {
        super(itemView);
        mealImage = (ImageView) itemView.findViewById(R.id.meal_image);
        mealNameLabel = (TextView) itemView.findViewById(R.id.meal_name_label);
        likeCountLabel = (TextView) itemView.findViewById(R.id.like_label_count);
        dislikeCountLabel = (TextView) itemView.findViewById(R.id.dislike_label_count);
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
