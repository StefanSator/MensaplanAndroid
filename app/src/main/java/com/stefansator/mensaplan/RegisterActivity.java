package com.stefansator.mensaplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput1;
    private TextInputEditText passwordInput2;
    private NetworkingManager networkingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = (TextInputEditText) findViewById(R.id.email_input);
        usernameInput = (TextInputEditText) findViewById(R.id.username_input);
        passwordInput1 = (TextInputEditText) findViewById(R.id.password_input1);
        passwordInput2 = (TextInputEditText) findViewById(R.id.password_input2);
        networkingManager = NetworkingManager.getInstance(this);
    }

    //Actions
    //Action to check if Registration is compatible and if compatible send Request for Registration of user to Backend */
    public void registerButtonClicked(View view) {
        // Check registration
        checkRegistration();
    }

    // Private Functions
    // Checks registration and if correct, then call backend to register new user
    private void checkRegistration() {
        if (!checkEmail()) return;
        if (!checkUsername()) return;
        if (!checkPasswords()) return;
        checkIfUserIsAvailable();
    }

    // Checks if Email has correct Format
    private boolean checkEmail() {
        boolean correct = !TextUtils.isEmpty(emailInput.getText()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches();
        if (!correct) {
            showAlert("Not a valid Email.");
            return false;
        }
        return true;
    }

    // Checks if Username has correct Format
    private boolean checkUsername() {
        Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
        boolean correct = !TextUtils.isEmpty(usernameInput.getText()) && USERNAME_PATTERN.matcher(usernameInput.getText()).matches();
        if (!correct) {
            showAlert("Not a valid Username.");
            return false;
        }
        return true;
    }

    // Checks if both Password Inputs are correct
    private boolean checkPasswords() {
        if (!passwordInput1.getText().toString().equals(passwordInput2.getText().toString())) {
            showAlert("Passwords are different.");
            return false;
        }
        if (passwordInput1.getText().length() == 0 || passwordInput2.getText().length() == 0) {
            showAlert("Passwords are empty.");
            return false;
        }
        return true;
    }

    // Checks if user is already available in System by asking the Backend
    private void checkIfUserIsAvailable() {
        // Construct the URL
        String url = networkingManager.getBackendURL() + "/customers?email=" + emailInput.getText();
        // Construct the Request
        JsonArrayRequest checkIfUserInSystemRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        checkIfUserIsAvailableHandler(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(checkIfUserInSystemRequest);
    }

    // Registers new User in Backend
    private void registerUser() {
        // Construct the URL
        String url = networkingManager.getBackendURL() + "/customers";
        // Construct the body
        JSONObject body = new JSONObject();
        try {
            body.put("username", usernameInput.getText());
            body.put("password", passwordInput1.getText());
            body.put("email", emailInput.getText());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        // Construct the request
        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        registerUserHandler(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        networkingManager.addToRequestQueue(registerUserRequest);
    }

    // Completion Handler for Request to Backend in function checkIfUserIsAvailable()
    private void checkIfUserIsAvailableHandler(JSONArray response) {
        if (response.length() != 0) {
            showAlert("User already available in System.");
            return;
        }
        registerUser();
    }

    // Completion Handler for Request to Backend for registration of a new user
    private void registerUserHandler(JSONObject response) {
        try {
            int sessionToken = response.getInt("customerid");
            UserSession.setSessionToken(sessionToken);
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    // Shows Alert Dialog when user input is not valid
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
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
