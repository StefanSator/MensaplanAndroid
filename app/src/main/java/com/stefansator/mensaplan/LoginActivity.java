package com.stefansator.mensaplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (TextInputEditText) findViewById(R.id.email_input);
        passwordInput = (TextInputEditText) findViewById(R.id.password_input);
    }

    //Actions
    // Action for checking login of user and if login is correct than logging in the user
    public void loginButtonClicked(View view) {
        // Validate user input by asking the Backend
        String url = NetworkingManager.getInstance(this).getBackendURL() + "/customers/validate";
        JSONObject body = new JSONObject();
        try {
            body.put("email", emailInput.getText().toString());
            body.put("password", passwordInput.getText().toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest validateUserRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        loginRequestHandler(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                }
        }) { // Declare inner anonymous class and override getHeader() Method to define Header
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        validateUserRequest.setShouldCache(false);
        NetworkingManager.getInstance(this).addToRequestQueue(validateUserRequest);
    }

    // Private Functions
    // Completion Handler for Login Request to Backend
    private void loginRequestHandler(JSONObject response) {
        try {
            Boolean valid = response.getBoolean("successful");
            int sessionToken = response.getInt("sessiontoken");
            if (!valid) {
                showAlertForIncorrectLogin();
                return;
            } else {
                loginUser(sessionToken);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    // Logs in User and sets the Session Token for current Session
    private void loginUser(int sessionToken) {
        UserSession.setSessionToken(sessionToken);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    // Shows Alert Dialog when user input is not valid
    private void showAlertForIncorrectLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login Incorrect.");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
