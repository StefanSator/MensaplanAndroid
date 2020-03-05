package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The controller for handling the Start Screen of the app.
 * @author stefansator
 * @version 1.0
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //Actions
    /**
     * Action to switch from current Activity to RegisterActivity, when register button is clicked
     * by the user of the app.
     * @param view Register button.
     */
    public void registerButtonClicked(View view) {
        // Open the Register Window
        Intent intent = new Intent(this.getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Action to switch from current Activity to LoginActivity, when login button is clicked
     * by the user of the app.
     * @param view Login button.
     */
    public void loginButtonClicked(View view) {
        // Open the Login Window
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
