package com.stefansator.mensaplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Classes who implement this interface can be informed by a Delegator
 * if changes occurred to a Meal Object, e.g. User disliked a Meal.
 * They can then implement the Function to perform appropriate reactions to the changes,
 * e.g. by updating the right element in the list to show the changes immediately to the user.
 * @author stefansator
 * @version 1.0
 */
interface ChangesLikeDislikeDelegate {
    /**
     * Implementors of the interface can define this function to be informed if changes to a Meal
     * Object occurred and react to it appropriately.
     * @param change true, if changes have occurred, else false.
     * @param updatedMeal the updated Meal Object.
     */
    void changesInLikesDislikes(boolean change, Meal updatedMeal);
}

/**
 * Class holding the possible like types as static members of the class.
 * @author stefansator
 * @version 1.0
 */
class LikeStates {
    public static final String like = "like";
    public static final String dislike = "dislike";
    public static final String neutral = "neutral";
}

/**
 * Activity for controlling the Detail Screen of a Meal, which is shown if a user wants to look
 * at detailed information of a Meal in the app.
 * @author stefansator
 * @version 1.0
 */
public class MealDetailActivity extends AppCompatActivity {
    /** The Delegate which gets informed if changes to the displayed Meal occurred. */
    public static ChangesLikeDislikeDelegate DELEGATE = null;
    /** The NetworkingManager which is used for Communication with the backend service. */
    private NetworkingManager networkingManager = NetworkingManager.getInstance(this);
    /** The ImageView showing the approriate image for a meal. */
    private ImageView mealImage;
    /** The TextView for displaying the name of a meal. */
    private TextView mealName;
    /** The TextView for displaying the student price of a meal. */
    private TextView studentPrize;
    /** The TextView for displaying the guest price of a meal. */
    private TextView guestPrize;
    /** The TextView for displaying the employee price of a meal. */
    private TextView employeePrize;
    /** The Button which is used for closing the Meal Dialog Window. */
    private RoundedButton cancelButton;
    /** The Button which is used for liking a meal. */
    private MaterialButton likeButton;
    /** The Button which is used for disliking a meal. */
    private MaterialButton dislikeButton;
    /** Label which displays the number of likes of a meal. */
    private TextView likeCountLabel;
    /** Label which displays the number of dislikes of a meal. */
    private TextView dislikeCountLabel;
    /** The Meal Object which holds the information about the displayed Mensa Meal. */
    private Meal meal;
    /** Holds the Route for Liking Functionality in the Backendservice. */
    private String likeRoute = "/likes";
    /** Current Like State of the user. A Like State tells if a user has liked, disliked or is neutral to the meal. */
    private String likeState = LikeStates.neutral;
    /** Number of likes. */
    private int likeCount = 0;
    /** Number of dislikes. */
    private int dislikeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealdetail);
        // Get the Handle to the UI Elements
        mealImage = (ImageView) findViewById(R.id.meal_image);
        mealName = (TextView) findViewById(R.id.meal_name_label);
        studentPrize = (TextView) findViewById(R.id.student_prize_label);
        guestPrize = (TextView) findViewById(R.id.guest_prize_label);
        employeePrize = (TextView) findViewById(R.id.employee_prize_label);
        cancelButton = (RoundedButton) findViewById(R.id.cancel_button);
        likeButton = (MaterialButton) findViewById(R.id.like_button);
        dislikeButton = (MaterialButton) findViewById(R.id.dislike_button);
        likeCountLabel = (TextView) findViewById(R.id.like_label_count);
        dislikeCountLabel = (TextView) findViewById(R.id.dislike_label_count);

        // Get the Meal which was sent by the Intent
        meal = getIntent().getParcelableExtra("Meal");
        if (meal == null) {
            System.out.println("ERROR: No Meal defined.");
        } else {
            // Set the UI Components to display the Meal Detail Data
            mealImage.setImageResource(meal.getImageId());
            mealName.setText(meal.getName());
            studentPrize.setText("Studenten: " + meal.getStudentPrize() + "€");
            guestPrize.setText("Gäste: " + meal.getGuestPrize() + "€");
            employeePrize.setText("Angestellte: " + meal.getEmployeePrize() + "€");
            /* If Meal is a like of the user, highlight Like Button or
             * if it is a dislike highlight the dislike button and set
             * number of likes and dislikes
             */
            getLikeDislikeState();
        }
    }

    // Actions
    /**
     * Click Listener for the cancelButton. Closes the Dialog Window and returns to previous window.
     * @param view Holds the view which was clicked. Here: cancelButton.
     */
    public void cancel(View view) {
        DELEGATE = null;
        finish();
    }

    /**
     * Click Listener for the likeButton. Is called if user clicks on likeButton. Performs the
     * appropriate actions for liking a meal by communicating with the backend and updating
     * views.
     * @param view
     */
    public void like(View view) {
        if (meal == null) return;

        try {
            this.likeCount = Integer.parseInt(likeCountLabel.getText().toString());
            this.dislikeCount = Integer.parseInt(dislikeCountLabel.getText().toString());

            if (likeState.equals(LikeStates.like)) {
                // Delete like from DB
                deleteLikeDislikeInDB();
                // Remove highlighting of Like Button
                highlightLikeDislikeButtons(false, false);
                // Decrement Like Count by 1
                likeCount--;
                likeCountLabel.setText(String.format(Locale.GERMAN, "%d", likeCount));
                // Update Like State
                updateLikeState(0);
            } else if (likeState.equals(LikeStates.dislike)) {
                // Update Like in DB
                insertOrUpdateLikeDislikeInDB(1);
                // Highlight Like Button, while removing highlighting of Dislike Button
                highlightLikeDislikeButtons(true, false);
                // Increment Like Count by 1, while decrementing Dislike Count by 1
                likeCount++;
                likeCountLabel.setText(String.format(Locale.GERMAN, "%d", likeCount));
                dislikeCount--;
                dislikeCountLabel.setText(String.format(Locale.GERMAN, "%d", dislikeCount));
                // Update Like State
                updateLikeState(1);
            } else {
                // Insert Like in DB
                insertOrUpdateLikeDislikeInDB(1);
                // Highlight Like Button
                highlightLikeDislikeButtons(true, false);
                // Increment Like Count by 1
                likeCount++;
                likeCountLabel.setText(String.format(Locale.GERMAN, "%d", likeCount));
                // Update Like State
                updateLikeState(1);
            }
        } catch (NumberFormatException ex) {
            Toast toast = Toast.makeText(this, "Leider ist ein Fehler aufgetreten.", Toast.LENGTH_SHORT);
            toast.show();
            ex.printStackTrace();
        }
    }

    /**
     * Click Listener for the dislikeButton. Is called if user clicks on dislikeButton. Performs the
     * appropriate actions for disliking a meal by communicating with the backend and updating
     * views.
     * @param view
     */
    public void dislike(View view) {
        if (meal == null) return;

        try {
            this.likeCount = Integer.parseInt(likeCountLabel.getText().toString());
            this.dislikeCount = Integer.parseInt(dislikeCountLabel.getText().toString());

            if (likeState.equals(LikeStates.dislike)) {
                // Delete Like From DB
                deleteLikeDislikeInDB();
                // Remove highlighting of Dislike Button
                highlightLikeDislikeButtons(false, false);
                // Decrement Dislike Count by 1
                dislikeCount--;
                dislikeCountLabel.setText(String.format(Locale.GERMAN, "%d", dislikeCount));
                // Update Like State
                updateLikeState(0);
            } else if (likeState.equals(LikeStates.like)) {
                // Update Like in DB
                insertOrUpdateLikeDislikeInDB(-1);
                // Highlight Dislike Button, while removing highlighting of Like Button
                highlightLikeDislikeButtons(false, true);
                // Increment Dislike Count by 1, while decrementing Like Count by 1
                dislikeCount++;
                likeCount--;
                dislikeCountLabel.setText(String.format(Locale.GERMAN, "%d", dislikeCount));
                likeCountLabel.setText(String.format(Locale.GERMAN, "%d", likeCount));
                // Update Like State
                updateLikeState(-1);
            } else {
                // Insert Dislike in DB
                insertOrUpdateLikeDislikeInDB(-1);
                // Highlight Dislike Button
                highlightLikeDislikeButtons(false, true);
                // Increment Dislike Count by 1
                dislikeCount++;
                dislikeCountLabel.setText(String.format(Locale.GERMAN, "%d", dislikeCount));
                // Update Like State
                updateLikeState(-1);
            }
        } catch (NumberFormatException ex) {
            Toast toast = Toast.makeText(this, "Leider ist ein Fehler aufgetreten.", Toast.LENGTH_SHORT);
            toast.show();
            ex.printStackTrace();
        }
    }

    // Private Functions
    /**
     * Starts Backend DELETE-Request to delete likes or dislikes from DB.
     */
    private void deleteLikeDislikeInDB() {
        // Construct URL
        String baseUrl = networkingManager.getBackendURL();
        String route = "/meals" + likeRoute;
        String queryParams = String.format(Locale.GERMAN, "?mealId=%s&sessionId=%s", meal.getId(), UserSession.getSessionToken());
        String url = String.format(Locale.GERMAN, "%s%s%s", baseUrl, route, queryParams);
        JsonObjectRequest deleteLikeRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        // TODO: Error Handling
                        informDelegate();
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
     * Starts Backend POST-Request to update or insert like, dislike in DB.
     * @param type the type of the like. Like: 1. Dislike: -1.
     */
    private void insertOrUpdateLikeDislikeInDB(int type) {
        // Construct body
        JSONObject like = new JSONObject();
        try {
            like.put("userId", UserSession.getSessionToken());
            like.put("mealId", meal.getId());
            like.put("type", type);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        // Construct URL
        String url = networkingManager.getBackendURL() + "/meals" + likeRoute;
        JsonObjectRequest insertOrUpdateLikeRequest = new JsonObjectRequest(Request.Method.POST, url, like,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        // TODO: Error Handling
                        informDelegate();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(insertOrUpdateLikeRequest);
    }

    /**
     * Starts Backend GET-Request to check if user likes, dislikes or is neutral to the currently displayed Meal.
     */
    private void getLikeDislikeState() {
        String baseURL = networkingManager.getBackendURL() + "/meals";
        String queryParams = String.format(Locale.GERMAN,"?mealid=%d&userid=%d", meal.getId(), UserSession.getSessionToken());
        String url = String.format(Locale.GERMAN, "%s%s%s", baseURL, likeRoute, queryParams);
        JsonObjectRequest likeDislikeStateRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        likeDislikeStateHandler(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(likeDislikeStateRequest);
    }

    /**
     * Completion Handler for Request to Backend sent by function getLikeDislikeState().
     * @param response The JSON Response of the backend.
     */
    private void likeDislikeStateHandler(JSONObject response) {
        try {
            int state = response.getInt("state");
            String likes = response.getString("likes");
            String dislikes = response.getString("dislikes");
            updateAndShowLikeDislikes(state, likes, dislikes);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Change Button Colors specific to the Like-State of the user regarding this meal and set Number of Likes and Dislikes.
     * @param state tells if user likes, dislikes or is neutral regarding the displayed meal.
     * @param likes Number of likes of the meal.
     * @param dislikes Number of dislikes of the meal.
     */
    private void updateAndShowLikeDislikes(int state, String likes, String dislikes) {
        // Show the number of likes and dislikes in total of the meal
        likeCountLabel.setText(likes);
        dislikeCountLabel.setText(dislikes);
        // Check if user has liked / disliked the meal or nothing of both
        switch (state) {
            case 1:
                likeState = LikeStates.like;
                highlightLikeDislikeButtons(true, false);
                break;
            case -1:
                likeState = LikeStates.dislike;
                highlightLikeDislikeButtons(false, true);
                break;
            default:
                likeState = LikeStates.neutral;
                highlightLikeDislikeButtons(false, false);
        }
    }

    /**
     * Highlight the Buttons appropriately, if the User has liked/disliked the Meal or nothing of both.
     * @param like true, if user likes the displayed meal.
     * @param dislike true, if user dislikes the displayed meal.
     */
    private void highlightLikeDislikeButtons(boolean like, boolean dislike) {
        if (like) {
            likeButton.setTextColor(Color.BLUE);
        } else {
            likeButton.setTextColor(Color.GRAY);
        }
        if (dislike) {
            dislikeButton.setTextColor(Color.RED);
        } else {
            dislikeButton.setTextColor(Color.GRAY);
        }
    }

    //

    /**
     * Update the Like State for a given Like type: 0: neutral, 1: like, -1: dislike.
     * @param type The type of like.
     */
    private void updateLikeState(int type) {
        switch (type) {
            case 1:
                likeState = LikeStates.like;
                break;
            case -1:
                likeState = LikeStates.dislike;
                break;
            default:
                likeState = LikeStates.neutral;
        }
    }

    /**
     * Informs the registered Delegate that changes have occurred and passes the updated Meal Object to the Delegate.
     */
    private void informDelegate() {
        meal.setLikes(likeCount);
        meal.setDislikes(dislikeCount);
        DELEGATE.changesInLikesDislikes(true, meal);
    }

}
