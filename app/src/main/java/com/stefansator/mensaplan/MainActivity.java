package com.stefansator.mensaplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Meal> meals = new ArrayList<Meal>();
    TextView testLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testLabel = (TextView) findViewById(R.id.testLabel);
        testLabel.setText("");


        String url = "https://regensburger-forscher.de:9001/mensa/uni/fr";
        getJSONDataFromURL(url);
    }

    // Private Functions
    private void getJSONDataFromURL(String url) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Initialize a new JSONObjectRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try {
                            for (int i = 0 ; i < response.length() ; i++) {
                                JSONObject json = response.getJSONObject(i);
                                Meal meal = new Meal(json);
                                meals.add(meal);
                                testLabel.append(meal.toString());
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Daten konnten nicht vom Server geladen werden.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
}
