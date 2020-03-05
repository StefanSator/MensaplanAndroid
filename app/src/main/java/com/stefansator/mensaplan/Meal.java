package com.stefansator.mensaplan;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Class representing a Mensa Meal.
 * @author stefansator
 * @version 1.0
 */
public class Meal implements Parcelable {
    /** ID of the Meal. */
    private int id;
    /** Name of the Meal. */
    private String name;
    /** Weekday on which the meal is available in the mensa. */
    private String day;
    /** Category of the Meal. */
    private String category;
    /** Costs of the Meal. */
    private Hashtable<String, Double> cost;
    /** Number of likes for the meal */
    private int likes;
    /** Number of dislikes for the meal */
    private int dislikes;
    /** ID of the image representing the category of the meal. */
    private int imageId;

    /**
     * Standard Constructor.
     * @param id ID of the meal.
     * @param name Name of the meal.
     * @param day Weekday on which the meal is available in the mensa.
     * @param category Category of the Meal.
     * @param studentPrice Student Price for the Meal.
     * @param employeePrice Employee Price for the Meal.
     * @param guestPrice Guest Price for the Meal.
     * @param likes Number of likes for the meal.
     * @param dislikes Number of dislikes for the meal.
     */
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

    /**
     * Constructor used for initialization with a Parcel.
     * @param parcel The Parcel which is used for creating the object.
     */
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

    /**
     * Constructor used for Initialization with JSONObject.
     * @param json The JSONObject which is used for creating the object.
     * @throws JSONException
     */
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

    /**
     * Get ID of the Meal.
     * @return int ID of the Meal.
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID of the Meal.
     * @param id ID of the Meal.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Name of the Meal.
     * @return String Name of the Meal.
     */
    public String getName() {
        return name;
    }

    /**
     * Set Name of the Meal.
     * @param name Name of the Meal.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Day of the Meal.
     * @return String Day on which the Meal is available in the Mensa.
     */
    public String getDay() {
        return day;
    }

    /**
     * Set Day of the Meal.
     * @param day Day on which the meal is available in the mensa.
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Get Category of the Meal.
     * @return String Category of the Meal.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set Category of the meal.
     * @param category Category of the Meal.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get Student Price of the Meal.
     * @return Double Student Price for the Meal.
     */
    public Double getStudentPrize() {
        return cost.get("students");
    }

    /**
     * Set Student Price of the Meal.
     * @param prize Student price for the meal.
     */
    public void setStudentPrize(double prize) {
        cost.put("students", prize);
    }

    /**
     * Get Employee Price of the Meal.
     * @return Double Employee Price for the Meal.
     */
    public Double getEmployeePrize() {
        return cost.get("employees");
    }

    /**
     * Set Employee Price for the Meal.
     * @param prize Employee Price for the Meal.
     */
    public void setEmployeePrize(double prize) {
        cost.put("employees", prize);
    }

    /**
     * Get Guest Price of the Meal.
     * @return Double Guest Price for the Meal.
     */
    public Double getGuestPrize() {
        return cost.get("guests");
    }

    /**
     * Set Guest Price of the Meal.
     * @param prize Guest Price for the Meal.
     */
    public void setGuestPrize(double prize) {
        cost.put("guests", prize);
    }

    /**
     * Get Number of Likes of the Meal.
     * @return int Number of Likes.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Set Number of Likes of the Meal.
     * @param likes Number of Likes.
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Get Number of Dislikes of the Meal.
     * @return int Number of Dislikes.
     */
    public int getDislikes() {
        return dislikes;
    }

    /**
     * Set Number of Dislikes of the Meal.
     * @param dislikes Number of Dislikes.
     */
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    /**
     * Get ID of the image representing the category of the meal.
     * @return int ID of the category image.
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * Set ID of the image representing the category of the meal.
     * @param id ID of the category image.
     */
    public void setImageId(int id) {
        imageId = id;
    }

    // Private Functions
    /**
     * Selects the right image for a meal category.
     * @param category The category of the Meal.
     */
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
    /** Creator used for generating instances of the Meal Class from a Parcel */
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