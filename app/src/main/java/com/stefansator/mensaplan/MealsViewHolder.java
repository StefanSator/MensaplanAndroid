package com.stefansator.mensaplan;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MealsViewHolder extends RecyclerView.ViewHolder {
    ImageView mealImage;
    TextView mealNameLabel;
    TextView mealPrizesLabel;

    MealsViewHolder(View cellView) {
        super(cellView);
        mealImage = (ImageView) cellView.findViewById(R.id.meal_image);
        mealNameLabel = (TextView) cellView.findViewById(R.id.meal_name_label);
        mealPrizesLabel = (TextView) cellView.findViewById(R.id.meal_prizes_label);
    }

}
