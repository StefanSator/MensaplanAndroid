package com.stefansator.mensaplan;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * The main controller for handling the navigation within the app, by switching between different
 * Fragments.
 * @author stefansator
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** Bottom Navgation Bar which is used for navigation within the app */
    private BottomNavigationView bottomNavigationView;
    /** FragmentTransaction Object, used for replacing Fragments within the app. */
    private FragmentTransaction fragmentTransaction;
    /** Fragment Manager, which is used for instantiation of Fragment Transactions. */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (bottomNavigationView.getSelectedItemId() == R.id.bottomNavigationMealMenuId) {
            MealListFragment mealListFragment = new MealListFragment();
            fragmentTransaction.replace(R.id.meal_list, mealListFragment);
        } else if (bottomNavigationView.getSelectedItemId() == R.id.bottomNavigationFavoritesMenuId) {
            PopularMealsFragment popularMealsFragment = new PopularMealsFragment();
            fragmentTransaction.replace(R.id.meal_list, popularMealsFragment);
        } else if (bottomNavigationView.getSelectedItemId() == R.id.bottomNavigationHistoryMenuId) {
            // TODO: Rename Favorite Class Names to History
            FavoriteListFragment favoriteListFragment = new FavoriteListFragment();
            fragmentTransaction.replace(R.id.meal_list, favoriteListFragment);
        } else {
            AccountFragment accountFragment = new AccountFragment();
            fragmentTransaction.replace(R.id.meal_list, accountFragment);
        }
        fragmentTransaction.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.bottomNavigationMealMenuId:
                        MealListFragment mealListFragment = new MealListFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.meal_list, mealListFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.bottomNavigationFavoritesMenuId:
                        PopularMealsFragment popularMealsFragment = new PopularMealsFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.meal_list, popularMealsFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.bottomNavigationHistoryMenuId:
                        FavoriteListFragment favoriteListFragment = new FavoriteListFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.meal_list, favoriteListFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.bottomNavigationAccountMenuId:
                        AccountFragment accountFragment = new AccountFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.meal_list, accountFragment);
                        fragmentTransaction.commit();
                    default:
                        return false;
                }
            }
        });
    }
}
