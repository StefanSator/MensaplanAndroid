package com.stefansator.mensaplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteListFragment extends Fragment implements ChangesLikeDislikeDelegate {
    private List<Meal> favorites = new ArrayList<Meal>();
    private RecyclerView mealsRecyclerView;
    private MealsRecyclerViewAdapter mealsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private Paint paint = new Paint();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        // Get Handle to Shared Preferences Object
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        // Load all Favorite Meals from User
        loadAllFavorites();

        // Obtain Handle to the RecyclerView
        mealsRecyclerView = (RecyclerView) view.findViewById(R.id.meals_recycler_view);
        // improve Performance of RecyclerView if Layout has always fixed size during Execution
        mealsRecyclerView.setHasFixedSize(true);
        // Set LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mealsRecyclerView.setLayoutManager(layoutManager);
        // Set Adapter for RecyclerView
        mealsAdapter = new MealsRecyclerViewAdapter(favorites, getActivity().getApplicationContext(), new MealsRecyclerViewAdapter.ItemSelectedListener() {
            @Override
            public void itemSelected(Meal item) {
                // Open Meal Detail Dialog Window
                Intent intent = new Intent(getActivity().getApplicationContext(), MealDetailActivity.class);
                intent.putExtra("Meal", item);
                MealDetailActivity.DELEGATE = FavoriteListFragment.this;
                startActivity(intent);
            }
        });
        mealsRecyclerView.setAdapter(mealsAdapter);
        addSwipeToDelete();

        return view;
    }

    // Delegate Pattern
    @Override
    public void changesInLikesDislikes(boolean changes, Meal updatedMeal) {
        if (changes) {
            // TODO
        }
    }

    // Private Functions

    // Load all the favorite meals from Shared Preferences
    private void loadAllFavorites() {
        favorites = new ArrayList<Meal>();
        Map<String, ?> mealsMap = sharedPreferences.getAll();
        for (String key : mealsMap.keySet()) {
            Gson gson = new Gson();
            String json = (String) mealsMap.get(key);
            Meal meal = gson.fromJson(json, Meal.class);
            favorites.add(meal);
        }
    }

    // Remove current selected Meal as a Favorite from Shared Preferences
    private void deleteMealAsFavorite(Meal favoriteToDelete) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(favoriteToDelete.getName());
        editor.apply();
    }

    // Remove the Meal from Favorites List
    private Meal removeFavorite(Meal favoriteToDelete) {
        Meal removedMeal = favorites.stream().filter(meal -> favoriteToDelete.getName().equals(meal.getName()))
                .findAny()
                .orElse(null);
        favorites.remove(removedMeal);
        return removedMeal;
    }

    // Implements the Swipe To Delete Functionality of the Recycler View
    private void addSwipeToDelete() {
        ItemTouchHelper.SimpleCallback defaultItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();

                Toast toast = Toast.makeText(getContext(), "Als Favorit entfernt.", Toast.LENGTH_SHORT);
                toast.show();

                if (swipeDirection == ItemTouchHelper.LEFT) {
                    final Meal deletedMeal = favorites.get(position);
                    mealsAdapter.remove(deletedMeal);
                    deleteMealAsFavorite(deletedMeal);
                }
            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        paint.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        canvas.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2 * width,(float) itemView.getBottom() - width);
                        canvas.drawBitmap(icon,null, icon_dest, paint);
                    } else {
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        canvas.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float) itemView.getBottom() - width);
                        canvas.drawBitmap(icon,null, icon_dest, paint);
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(defaultItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mealsRecyclerView);
    }
}
