package com.stefansator.mensaplan;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Holds the view for a cell in the list of the RecyclerView. Does not fill the cell with data.
 * @author stefansator
 * @version 1.0
 */
public class MealsViewHolder extends RecyclerView.ViewHolder {
    /** The image which shows the category of the meal. */
    ImageView mealImage;
    /** Label for displaying the name of the meal. */
    TextView mealNameLabel;
    /** Label for displaying the number of likes of the meal. */
    TextView likeCountLabel;
    /** Label for displaying the number of dislikes of the meal. */
    TextView dislikeCountLabel;

    /**
     * Constructor
     * @param itemView The view the ViewHolder will hold.
     */
    MealsViewHolder(View itemView) {
        super(itemView);
        mealImage = (ImageView) itemView.findViewById(R.id.meal_image);
        mealNameLabel = (TextView) itemView.findViewById(R.id.meal_name_label);
        likeCountLabel = (TextView) itemView.findViewById(R.id.like_label_count);
        dislikeCountLabel = (TextView) itemView.findViewById(R.id.dislike_label_count);
    }

    /**
     * Binds the Click Listener to the ItemView, which the ViewHolder holds.
     * @param item The meal item that is currently displayed in the view, which the ViewHolder holds.
     * @param listener The listener which should be bound to the view, which the ViewHolder holds.
     */
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
