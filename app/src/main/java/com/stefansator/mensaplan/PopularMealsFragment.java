package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PopularMealsFragment extends Fragment implements ChangesLikeDislikeDelegate {
    private TextView mostPopularMealLabel;
    private TextView mostUnpopularMealLabel;
    private MaterialCardView mostPopularCard;
    private MaterialCardView mostUnpopularCard;
    private Meal mostPopularMeal;
    private Meal mostUnpopularMeal;

    // Action Listeners
    private View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mostPopularMeal == null || mostUnpopularMeal == null) return;
            showMealDialog(v);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_meals, container, false);
        mostPopularMealLabel = (TextView) view.findViewById(R.id.most_popular_name);
        mostUnpopularMealLabel = (TextView) view.findViewById(R.id.most_unpopular_name);
        mostPopularCard = (MaterialCardView) view.findViewById(R.id.most_popular_card);
        mostUnpopularCard = (MaterialCardView) view.findViewById(R.id.most_unpopular_card);

        // Get Most Popular and Most Unpopular Meal From Backend
        loadPopularAndUnpopularMeal();

        // Set Action Listeners
        mostPopularCard.setOnClickListener(cardClickListener);
        mostUnpopularCard.setOnClickListener(cardClickListener);
        return view;
    }

    // ChangesLikeDislikeDelegate Interface Functions
    public void changesInLikesDislikes(boolean changes, Meal updatedMeal) {
        loadPopularAndUnpopularMeal();
    }

    // Private Functions
    /* Starts GET-Request to Backend to retrieve the most popular and most unpopular Meal of current day */
    private void loadPopularAndUnpopularMeal() {
        Calendar calendar = new GregorianCalendar();
        // int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        int weekOfYear = 46; // TODO: change to real calendar week
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY) {
            weekDay = Calendar.FRIDAY;
        }
        String weekdayString = transformWeekdayIntToString(weekDay);

        // Networking
        NetworkingManager networkingManager = NetworkingManager.getInstance(this.getActivity());
        String baseUrl = networkingManager.getBackendURL() + "/meals/popular";
        String queryParams = String.format(Locale.GERMAN, "?weekday='%s'&calendarweek=%d&year=%d", weekdayString, weekOfYear, year);
        String url = baseUrl + queryParams;
        JsonObjectRequest popularUnpopularMealRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadPopularAndUnpopularMealHandler(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(popularUnpopularMealRequest);
    }

    /* Completion Handler for Backend Request to retrieve the most popular and unpopular Meals */
    private void loadPopularAndUnpopularMealHandler(JSONObject response) {
        try {
            JSONObject popularMealJSON = response.getJSONObject("popular");
            JSONObject unpopularMealJSON = response.getJSONObject("unpopular");
            mostPopularMeal = new Meal(popularMealJSON);
            mostUnpopularMeal = new Meal(unpopularMealJSON);
            mostPopularMealLabel.setText(mostPopularMeal.getName());
            mostUnpopularMealLabel.setText(mostUnpopularMeal.getName());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /* Returns the String Identifier of a weekday for a given Int */
    private String transformWeekdayIntToString(int weekday) {
        switch (weekday) {
            case Calendar.MONDAY:
                return "Mo";
            case Calendar.TUESDAY:
                return "Tu";
            case Calendar.WEDNESDAY:
                return "We";
            case Calendar.THURSDAY:
                return "Th";
            case Calendar.FRIDAY:
                return "Fr";
            case Calendar.SATURDAY:
                return "Sa";
            case Calendar.SUNDAY:
                return "Su";
            default:
                return "Mo";
        }
    }

    /* Open Meal Detail Dialog Window */
    private void showMealDialog(View view) {
        // Open Meal Detail Dialog Window
        Intent intent = new Intent(getActivity().getApplicationContext(), MealDetailActivity.class);
        if (view.getId() == R.id.most_popular_card) {
            intent.putExtra("Meal", mostPopularMeal);
        } else {
            intent.putExtra("Meal", mostUnpopularMeal);
        }
        MealDetailActivity.DELEGATE = this;
        startActivity(intent);
    }
}
