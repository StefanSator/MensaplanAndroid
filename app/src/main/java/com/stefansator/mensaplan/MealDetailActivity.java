package com.stefansator.mensaplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MealDetailActivity extends AppCompatActivity {
    private ImageView mealImage;
    private TextView mealName;
    private TextView studentPrize;
    private TextView guestPrize;
    private TextView employeePrize;
    private Button cancelButton;
    private Meal meal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealdetail);
        // Get the Handle to the UI Elements
        mealImage = findViewById(R.id.meal_image);
        mealName = findViewById(R.id.meal_name_label);
        studentPrize = findViewById(R.id.student_prize_label);
        guestPrize = findViewById(R.id.guest_prize_label);
        employeePrize = findViewById(R.id.employee_prize_label);
        cancelButton = findViewById(R.id.cancel_button);

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
        }
    }

    // Actions
    // Close Dialog Window
    public void cancel(View view) {
        finish();
    }

}
