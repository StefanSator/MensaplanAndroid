package com.stefansator.mensaplan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;

public class Meal {
    private String name;
    private String day;
    private String category;
    private Hashtable<String, Double> cost;
    private int imageId;

    // Initialization
    public Meal(String name, String day, String category, double studentPrice, double employeePrice, double guestPrice) {
        this.name = name;
        this.day = day;
        this.category = category;
        cost = new Hashtable<String, Double>();
        cost.put("students", studentPrice);
        cost.put("guests", guestPrice);
        cost.put("employees", employeePrice);
        setRightImage(this.category);
    }

    public Meal(Dictionary<String, String> dictionary) {
        name = dictionary.get("name");
        day = dictionary.get("tag");
        category = dictionary.get("warengruppe");
        String studentCosts = dictionary.get("stud");
        String employeeCosts = dictionary.get("bed");
        String guestCosts = dictionary.get("gast");
        cost = new Hashtable<String, Double>();
        cost.put("students", Double.parseDouble(studentCosts.replace(",", ".")));
        cost.put("employees", Double.parseDouble(employeeCosts.replace(",", ".")));
        cost.put("guests", Double.parseDouble(guestCosts.replace(",", ".")));
        setRightImage(this.category);
    }

    /* Deprecated:
    public Meal(JSONObject json) throws JSONException {
        name = json.getString("name");
        day = json.getString("day");
        category = json.getString("category");
        JSONObject costObject = json.getJSONObject("cost");
        cost = new Hashtable<String, Double>();
        cost.put("students", Double.parseDouble(costObject.getString("students").replace(",", ".")));
        cost.put("guests", Double.parseDouble(costObject.getString("guests").replace(",", ".")));
        cost.put("employees", Double.parseDouble(costObject.getString("employees").replace(",", ".")));
        setRightImage(this.category);
    }
    */

    // Getter / Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getStudentPrize() {
        return cost.get("students");
    }

    public void setStudentPrize(double prize) {
        cost.put("students", prize);
    }

    public Double getEmployeePrize() {
        return cost.get("employees");
    }

    public void setEmployeePrize(double prize) {
        cost.put("employees", prize);
    }

    public Double getGuestPrize() {
        return cost.get("guests");
    }

    public void setGuestPrize(double prize) {
        cost.put("guests", prize);
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int id) {
        imageId = id;
    }

    // Private Functions
    private void setRightImage(String category) {
        if (category.startsWith("S")) {
            imageId = R.drawable.soup;
        } else if (category.startsWith("HG")) {
            imageId = R.drawable.maindish;
        } else if (category.startsWith("B")) {
            imageId = R.drawable.sidedish;
        } else if (category.startsWith("N")) {
            imageId = R.drawable.dessert;
        } else {
            imageId = 0;
        }
    }

    // String Representation
    @Override
    public String toString() {
        return "Meal: " + name + ", Day: " + day + ", Category: " + category + ", studentPrize: "
                + cost.get("students") + ", employeePrize: " + cost.get("employees") + ", guestPrize: "
                + cost.get("guests");
    }
}