package com.stefansator.mensaplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

/**
 * The controller for handling the login process within the app.
 * @author stefansator
 * @version 1.0
 */
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

    // Actions

    /**
     * Action which is called automatically if login button is clicked.
     * It checks the login of a user and if the login is correct, the user gets logged in.
     * @param view The view which has triggered the action. Here: the login button defined in the
     *             layout
     */
    // Action for checking login of user and if login is correct than logging in the user
    public void loginButtonClicked(View view) {
        // Check Email Format on Client
        if (!checkEmail()) return;
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

    /**
     * Completion Handler for Login Request to Backend.
     * @param response JSON Response from the backend
     */
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

    /**
     * Logs in User and sets the Session Token for current Session.
     * @param sessionToken the token which was delivered from the backend, after login request
     *                     was successfull
     */
    private void loginUser(int sessionToken) {
        UserSession.setSessionToken(sessionToken);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Checks if Email has a valid Format, before submitting it to the backend.
     * @return boolean true if email is valid, otherwise false.
     */
    private boolean checkEmail() {
        boolean correct = !TextUtils.isEmpty(emailInput.getText()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches();
        if (!correct) {
            showAlertForIncorrectLogin();
            return false;
        }
        return true;
    }

    /**
     * Shows Alert Dialog when something went wrong, during the login process.
     */
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
