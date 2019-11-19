package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //Actions
    /* Action to switch from current Activity to RegisterActivity */
    public void registerButtonClicked(View view) {
        // TODO
    }

    /* Action to switch from current Activity to LoginActivity */
    public void loginButtonClicked(View view) {
        // Open a Login Window
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
