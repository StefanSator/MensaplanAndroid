package com.stefansator.mensaplan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class Meal {
    private String name;
    private String day;
    private String category;
    private Hashtable<String, Double> cost;

    // Initialization
    public Meal(String name, String day, String category, double studentPrice, double employeePrice, double guestPrice) {
        this.name = name;
        this.day = day;
        this.category = category;
        cost = new Hashtable<String, Double>();
        cost.put("students", studentPrice);
        cost.put("guests", guestPrice);
        cost.put("employees", employeePrice);
    }

    public Meal(JSONObject json) throws JSONException {
        name = json.getString("name");
        day = json.getString("day");
        category = json.getString("category");
        JSONObject costObject = json.getJSONObject("cost");
        cost = new Hashtable<String, Double>();
        cost.put("students", Double.parseDouble(costObject.getString("students").replace(",", ".")));
        cost.put("guests", Double.parseDouble(costObject.getString("guests").replace(",", ".")));
        cost.put("employees", Double.parseDouble(costObject.getString("employees").replace(",", ".")));
    }


    @Override
    public String toString() {
        return "Meal: " + name + ", Day: " + day + ", Category: " + category + ", studentPrize: "
                + cost.get("students") + ", employeePrize: " + cost.get("employees") + ", guestPrize: "
                + cost.get("guests");
    }
}