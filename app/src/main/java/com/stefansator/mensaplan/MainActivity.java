package com.stefansator.mensaplan;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
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
                switch (menuItem.getItemId()) {
                    case R.id.bottomNavigationMealMenuId:
                        MealListFragment mealListFragment = new MealListFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.meal_list, mealListFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.bottomNavigationFavoritesMenuId:
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
