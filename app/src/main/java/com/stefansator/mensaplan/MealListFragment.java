package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fragment for implementing the Mensaspeiseplan Functionality. It handles a list
 * which contains all current meals of the current week. It implements functionality used for
 * managing the list.
 * @author stefansator
 * @version 1.0
 */
public class MealListFragment extends Fragment implements ChangesLikeDislikeDelegate {
    /** List containing the current Mensa Meals */
    private List<Meal> meals = new ArrayList<Meal>();
    /** The RecyclerView for displaying the list */
    private RecyclerView mealsRecyclerView;
    /** The Adapter for the RecyclerView. */
    private MealsRecyclerViewAdapter mealsAdapter;
    /** The LayoutManager used for the RecyclerView. */
    private RecyclerView.LayoutManager layoutManager;
    /** The TabLayout used for switching through a tab bar between different days of the week. */
    private TabLayout tabLayout;
    /** Current Calendar Week */
    private int weekOfYear;
    /** Current Year */
    private int year;

    /**
     * This function is called, when the MealListFragment is created. It instantiates the User
     * Interface, by setting up the RecyclerView.
     * @param inflater inflater for inflating views in the fragment
     * @param container parent view to which the fragment should be attached to
     * @param savedInstanceState If non-null, fragment is being reconstructed from previous state
     * @return View This returns the created Layout as a View Object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        Calendar calendar = new GregorianCalendar();
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        year = calendar.get(Calendar.YEAR);
        loadMealData("Mo");

        // Obtain Handle to the RecyclerView
        mealsRecyclerView = (RecyclerView) view.findViewById(R.id.meals_recycler_view);
        // improve Performance of RecyclerView if Layout has always fixed size during Execution
        mealsRecyclerView.setHasFixedSize(true);
        // Set LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mealsRecyclerView.setLayoutManager(layoutManager);
        // Set Adapter for RecyclerView
        mealsAdapter = new MealsRecyclerViewAdapter(meals, getActivity().getApplicationContext(), new MealsRecyclerViewAdapter.ItemSelectedListener() {
            @Override
            public void itemSelected(Meal item) {
                // Open Meal Detail Dialog Window
                Intent intent = new Intent(getActivity().getApplicationContext(), MealDetailActivity.class);
                intent.putExtra("Meal", item);
                MealDetailActivity.DELEGATE = MealListFragment.this;
                startActivity(intent);
            }
        });
        mealsRecyclerView.setAdapter(mealsAdapter);

        // Set TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        // Set Action Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                clearAllMealData();
                String tabName = (String) tab.getText();
                switch (tabName) {
                    case "Mon":
                        reloadListWithData("Mo");
                        break;
                    case "Tue":
                        reloadListWithData("Tu");
                        break;
                    case "Wed":
                        reloadListWithData("We");
                        break;
                    case "Thu":
                        reloadListWithData("Th");
                        break;
                    case "Fri":
                        reloadListWithData("Fr");
                        break;
                    default:
                        System.out.println("The selected Tab does not exist in TabLayout.");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });
        // Inflate the layout
        return view;
    }

    // ChangesLikeDislikeDelegate
    public void changesInLikesDislikes(boolean changes, Meal updatedMeal) {
        if (changes) {
            Meal originalMeal = getOriginalMeal(updatedMeal);
            int position = mealsAdapter.remove(originalMeal);
            mealsAdapter.insert(position, updatedMeal);
            mealsAdapter.notifyDataSetChanged();
        }
    }

    // Private Functions
    /**
     * Get Meal Data for a selected week day from the backend service through a rest call.
     * @param weekDay selected week day.
     */
    private void loadMealData(String weekDay) {
        // Get the NetworkingManager
        NetworkingManager networkingManager = NetworkingManager.getInstance(this.getActivity());
        // Construct URL
        String url = networkingManager.getBackendURL() + "/meals?weekDay='" + weekDay + "'&calendarWeek=" + weekOfYear + "&year=" + year;
        // Request the data as a JSON Array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        initializeMealsArray(response);
                        // Notify Adapter that Data Set has changed
                        mealsAdapter.insertAll(meals);
                        mealsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
            }
        });
        // Add the Request to the Request Queue
        networkingManager.addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Fill the meals List with data from the JSONArray Response returned by the backend.
     * @param jsonArray Response from the backend service.
     */
    private void initializeMealsArray(JSONArray jsonArray) {
        try {
            for (int i = 0 ; i < jsonArray.length() ; i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Meal meal = new Meal(json);
                meals.add(meal);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Clears the RecyclerView.
     */
    private void clearAllMealData() {
        meals.clear();
        mealsRecyclerView.removeAllViews();
        mealsAdapter.removeAll();
    }

    /**
     * Reloads the RecyclerView, to display data of a different selected weekDay.
     * @param weekDay The weekday for which the list should show the Mensa Meals.
     */
    private void reloadListWithData(String weekDay) {
        loadMealData(weekDay);
    }

    /**
     * Returns the original Meal Object in a Data Set of Recycler View from a copy.
     * @param mealCopy Copy of the Meal Object.
     * @return Meal Original Meal.
     */
    private Meal getOriginalMeal(Meal mealCopy) {
        Meal original = meals.stream().filter(m -> mealCopy.getName().equals(m.getName()))
                .findAny()
                .orElse(null);
        return original;
    }
}
