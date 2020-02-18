package com.stefansator.mensaplan;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for implementing the Like-History Functionality. It handles a list
 * which contains all liked and disliked meals. It implements functionality used for
 * managing the list.
 * @author stefansator
 * @version 1.0
 */
public class FavoriteListFragment extends Fragment implements ChangesLikeDislikeDelegate {
    /** Contains all meals the user has liked and disliked. */
    private List<Meal> favorites = new ArrayList<Meal>();
    /** Contains the mealid of every liked meal. */
    private List<Integer> likes = new ArrayList<Integer>();
    /** Contains the mealid of every disliked meal. */
    private List<Integer> dislikes = new ArrayList<Integer>();
    /** The RecyclerView for displaying the list. */
    private RecyclerView historyRecyclerView;
    /** The Adapter for the RecyclerView. */
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    /** The LayoutManager used for the RecyclerView. */
    private RecyclerView.LayoutManager layoutManager;
    /** Paint object for drawing geometries and bitmaps. Is used by the {@link #addSwipeToDelete()} function. */
    private Paint paint = new Paint();

    /**
     * This function is called, when the FavoriteListFragment is created. It instantiates the User
     * Interface, by setting up the RecyclerView.
     * @param inflater inflater for inflating views in the fragment
     * @param container parent view to which the fragment should be attached to
     * @param savedInstanceState If non-null, fragment is being reconstructed from previous state
     * @return View This returns the created Layout as a View Object
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        // Obtain Handle to the RecyclerView
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.meals_recycler_view);
        // improve Performance of RecyclerView if Layout has always fixed size during Execution
        historyRecyclerView.setHasFixedSize(true);
        // Set LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        historyRecyclerView.setLayoutManager(layoutManager);
        // Set Adapter for RecyclerView
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(favorites, likes, dislikes, getActivity().getApplicationContext(), new HistoryRecyclerViewAdapter.ItemSelectedListener() {
            @Override
            public void itemSelected(Meal item) {
                // Open Meal Detail Dialog Window
                Intent intent = new Intent(getActivity().getApplicationContext(), MealDetailActivity.class);
                intent.putExtra("Meal", item);
                MealDetailActivity.DELEGATE = FavoriteListFragment.this;
                startActivity(intent);
            }
        });
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
        addSwipeToDelete();

        // Load all Favorite Meals from User
        loadListData();

        return view;
    }

    // Delegate Pattern
    @Override
    public void changesInLikesDislikes(boolean changes, Meal updatedMeal) {
        if (changes) {
            loadListData();
        }
    }

    // Private Functions
    /**
     * Starts GET-Request to Backend to get all meals the user either liked or disliked.
     */
    private void loadListData() {
        clearAllListData();
        System.out.println("Starting Backend Request.");
        NetworkingManager networkingManager = NetworkingManager.getInstance(this.getActivity());
        String baseUrl = networkingManager.getBackendURL() + "/meals/userlikes";
        String queryParams = "?userid=" + UserSession.getSessionToken();
        String url = baseUrl + queryParams;
        JsonObjectRequest userlikesRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        initializeMealArrays(response);
                        historyRecyclerViewAdapter.insertAll(favorites, likes, dislikes);
                        historyRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(userlikesRequest);
    }

    /**
     * Starts DELETE-Request to Backend to delete a like or dislike of the logged in user regarding
     * a specified Meal.
     * @param meal The meal for which the like or dislike should be deleted
     */
    private void deleteLikeOrDislike(Meal meal) {
        NetworkingManager networkingManager = NetworkingManager.getInstance(this.getActivity());
        String baseUrl = networkingManager.getBackendURL() + "/meals/likes";
        String queryParams = String.format(Locale.GERMAN, "?mealId=%s&sessionId=%s", meal.getId(), UserSession.getSessionToken());
        String url = baseUrl + queryParams;
        JsonObjectRequest deleteLikeRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        // TODO: Error Handling
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(deleteLikeRequest);
    }

    /**
     * Constructs and fills {@link #favorites}, {@link #likes}, {@link #dislikes} with appropriate data.
     * @param response The json response from the backend
     */
    private void initializeMealArrays(JSONObject response) {
        try {
            JSONArray jsonMeals = response.getJSONArray("meals");
            JSONArray jsonLikes = response.getJSONArray("likes");
            JSONArray jsonDislikes = response.getJSONArray("dislikes");
            // Fill Meal Array
            for (int i = 0 ; i < jsonMeals.length() ; i++) {
                Meal meal = new Meal(jsonMeals.getJSONObject(i));
                favorites.add(meal);
            }
            // Fill Likes Array
            for (int i = 0 ; i < jsonLikes.length() ; i++) {
                likes.add(jsonLikes.getJSONObject(i).getInt("mealid"));
            }
            // Fill Dislikes Array
            for (int i = 0 ; i < jsonDislikes.length() ; i++) {
                dislikes.add(jsonDislikes.getJSONObject(i).getInt("mealid"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Implements the Swipe-To-Delete Functionality for the RecyclerView.
     */
    private void addSwipeToDelete() {
        ItemTouchHelper.SimpleCallback defaultItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                System.out.println("Position onSwiped(): " + position);
                System.out.println("SIZE: onSwiped(): " + favorites.size());

                Toast toast = Toast.makeText(getContext(), "Als Favorit entfernt.", Toast.LENGTH_SHORT);
                toast.show();

                if (swipeDirection == ItemTouchHelper.LEFT) {
                    final Meal deletedMeal = favorites.get(position);
                    historyRecyclerViewAdapter.remove(deletedMeal);
                    deleteLikeOrDislike(deletedMeal);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX <= 0) {
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
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);
    }

    /**
     * Clears all data displayed within the RecyclerView.
     */
    private void clearAllListData() {
        favorites.clear();
        likes.clear();
        dislikes.clear();
        historyRecyclerView.removeAllViews();
    }
}
