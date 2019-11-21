package com.stefansator.mensaplan;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;

/*
 * Instead for implementing the Parcelable Interface it is also possible to implement the
 * Serializable Interface, but it is less performant than implementing Parcelable.
 */
public class Meal implements Parcelable {
    private int id;
    private String name;
    private String day;
    private String category;
    private Hashtable<String, Double> cost;
    private int likes;
    private int dislikes;
    private int imageId;

    // Initialization
    public Meal(int id, String name, String day, String category, double studentPrice, double employeePrice, double guestPrice, int likes, int dislikes) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.category = category;
        cost = new Hashtable<String, Double>();
        cost.put("students", studentPrice);
        cost.put("guests", guestPrice);
        cost.put("employees", employeePrice);
        this.likes = likes;
        this.dislikes = dislikes;
        setRightImage(this.category);
    }

    // constructer used for initialization with parcel
    protected Meal(Parcel parcel) {
        // Important: Read in same order from parcel, as written to parcel
        id = parcel.readInt();
        name = parcel.readString();
        day = parcel.readString();
        category = parcel.readString();
        cost = new Hashtable<String, Double>();
        cost.put("students", parcel.readDouble());
        cost.put("employees", parcel.readDouble());
        cost.put("guests", parcel.readDouble());
        likes = parcel.readInt();
        dislikes = parcel.readInt();
        setRightImage(this.category);
    }

    // constructor used for Initialization with JSONObject
    public Meal(JSONObject json) throws JSONException {
        id = json.getInt("mealid");
        name = json.getString("mealname");
        day = json.getString("weekday");
        category = json.getString("category");
        cost = new Hashtable<String, Double>();
        cost.put("students", json.getDouble("studentprice"));
        cost.put("guests", json.getDouble("guestprice"));
        cost.put("employees", json.getDouble("employeeprice"));
        likes = json.getInt("likes");
        dislikes = json.getInt("dislikes");
        setRightImage(this.category);
    }

    // Getter / Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
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

    // Parcelable Implementation
    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(day);
        dest.writeString(category);
        Double studentCosts, employeeCosts, guestCosts;
        dest.writeDouble((studentCosts = cost.get("students")) != null ? studentCosts : 0.0);
        dest.writeDouble((employeeCosts = cost.get("employees")) != null ? employeeCosts : 0.0);
        dest.writeDouble((guestCosts = cost.get("guests")) != null ? guestCosts : 0.0);
        dest.writeInt(likes);
        dest.writeInt(dislikes);
        dest.writeInt(imageId);
    }
}