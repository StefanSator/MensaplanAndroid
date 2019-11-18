package com.stefansator.mensaplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

interface ChangedFavoritesDelegate {
    void changesInFavorites(boolean changes, Meal meal);
}

public class MealDetailActivity extends AppCompatActivity {
    public static ChangedFavoritesDelegate DELEGATE = null;
    private ImageView mealImage;
    private TextView mealName;
    private TextView studentPrize;
    private TextView guestPrize;
    private TextView employeePrize;
    private RoundedButton cancelButton;
    private Button subscribeButton;
    private TextView subscribeLabel;
    private SharedPreferences sharedPreferences;
    private Meal meal;
    private boolean isSubscribed;


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
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeLabel = (TextView) findViewById(R.id.subscribeText);

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
            // Get the Handle to the SharedPreferences Object
            sharedPreferences = getApplicationContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
            if (mealIsFavorite()) {
                setUnsubscribeButton();
            } else {
                setSubscribeButton();
            }
        }
    }

    // Actions
    // Close Dialog Window
    public void cancel(View view) {
        DELEGATE = null;
        finish();
    }

    public void subscribe(View view) {
        if (isSubscribed == false) {
            saveMealAsFavorite();
            Toast toast = Toast.makeText(getApplicationContext(), "Als Favorit hinzugefügt.", Toast.LENGTH_SHORT);
            toast.show();
            setUnsubscribeButton();
        } else {
            deleteMealAsFavorite();
            Toast toast = Toast.makeText(getApplicationContext(), "Als Favorit entfernt.", Toast.LENGTH_SHORT);
            toast.show();
            setSubscribeButton();
        }
    }

    // Private Functions
    // Checks if current displayed Meal is saved as a favorite */
    private boolean mealIsFavorite() {
        String json = sharedPreferences.getString(meal.getName(), null);
        System.out.println(json);
        if (json == null) return false;
        else return true;
    }

    // Save current selected Meal as a Favorite
    private void saveMealAsFavorite() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(meal, Meal.class);
        editor.putString(meal.getName(), json);
        editor.apply();
        if (DELEGATE != null) DELEGATE.changesInFavorites(true, meal);
    }

    // Remove current selected Meal as a Favorite
    private void deleteMealAsFavorite() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(meal.getName());
        editor.apply();
        if (DELEGATE != null) DELEGATE.changesInFavorites(true, meal);
    }

    private void setSubscribeButton() {
        subscribeButton.setText(R.string.add);
        subscribeLabel.setText(R.string.subscribeText);
        isSubscribed = false;
    }

    private void setUnsubscribeButton() {
        subscribeButton.setText(R.string.minus);
        subscribeLabel.setText(R.string.unsubscribeText);
        isSubscribed = true;
    }

    // TODO: Adapt app for landscape orientation

}
