package com.stefansator.mensaplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private List<Meal> meals = new ArrayList<Meal>();
    private RecyclerView mealsRecyclerView;
    private RecyclerView.Adapter mealsAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String url = "https://regensburger-forscher.de:9001/mensa/uni/fr";
        String url = "https://www.stwno.de/infomax/daten-extern/csv/UNI-R/38.csv";
        getJSONDataFromURL(url);

        // Obtain Handle to the RecyclerView
        mealsRecyclerView = (RecyclerView) findViewById(R.id.meals_recycler_view);
        // improve Performance of RecyclerView if Layout has always fixed size during Execution
        mealsRecyclerView.setHasFixedSize(true);
        // Set LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(this);
        mealsRecyclerView.setLayoutManager(layoutManager);
        // Set Adapter for RecyclerView
        mealsAdapter = new MealsRecyclerViewAdapter(meals, getApplicationContext());
        mealsRecyclerView.setAdapter(mealsAdapter);
    }

    // Networking with Volley
    private void getJSONDataFromURL(String url) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Request the CSV as a String Response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String lines[] = response.split("\n");
                        // TODO: Explain how to configure to use JAVA 8 Features in Android in Bachelor Paper. Desugar
                        List<String> linesForSpecifiedDay = Arrays.stream(lines).filter(str -> str.contains(";Mo;")).collect(Collectors.toList());
                        initializeMealsArray(linesForSpecifiedDay, lines[0].replace("\r", "").split(";"));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("An Error occurred while performing Request to Server. Error: " + error.getLocalizedMessage());
                    }
        });
        // Add the Request to the Request Queue
        requestQueue.add(stringRequest);
    }

    // Private Functions
    private void initializeMealsArray(List<String> CSVLines, String[] keys) {
        for (String line : CSVLines) {
            String cleanLine = line.replace("\r", "");
            Hashtable<String, String> dictionary = convertCSVToDictionary(cleanLine, keys);
            Meal meal = new Meal(dictionary);
            meals.add(meal);
        }
    }

    private Hashtable<String, String> convertCSVToDictionary(String line, String[] keys) {
        String values[] = line.split(";");
        Hashtable<String, String> dictionary = new Hashtable<String, String>();
        for (int i = 0 ; i < values.length ; i++) {
            dictionary.put(keys[i], values[i]);
        }
        return dictionary;
    }

    /* Deprecated Functions */

    /*
     * These Functions where used for loading JSON converted Meal Data from Mensa API of regensburger-forscher.de.
     * Unfortunately the Certificate of the Mensa API became invalid, so i can not retrieve data from the webservice anymore.
     * Android allows only secure connections over https to a webservice.
     * Instead of retrieving Data from the Mensa API of regensburger-forscher.de, i retrieve my Meal Data now directly from the website
     * of the Studentenwerk Niederbayern/Oberpfalz.
     * The change of Service is the reason why these functions are deprecated and not used anymore.
     */

    /*
    private void getJSONDataFromURL(String url) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Initialize a new JSONArrayRequest instance
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
    */
}
