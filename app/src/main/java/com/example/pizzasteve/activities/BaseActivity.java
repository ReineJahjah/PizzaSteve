package com.example.pizzasteve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.pizzasteve.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    protected EditText etSearch;
    protected Button btnPizza, btnDrinks, btnStarters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Setup bottom navigation
    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    navigateToHome();
                    return true;
                } else if (itemId == R.id.nav_favorites) {
                    navigateToFavorites();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    navigateToCart();
                    return true;
                } else if (itemId == R.id.nav_stores) {
                    navigateToStores();
                    return true;
                }
                return false;
            });
        }
    }

    // Setup custom action bar
    protected void setupActionBar() {
        View actionBar = findViewById(R.id.customActionBar);
        if (actionBar != null) {
            etSearch = actionBar.findViewById(R.id.etSearch);
            btnPizza = actionBar.findViewById(R.id.btnPizza);
            btnDrinks = actionBar.findViewById(R.id.btnDrinks);
            btnStarters = actionBar.findViewById(R.id.btnStarters);

            // Search functionality
            if (etSearch != null) {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        onSearchQueryChanged(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }

            // Category buttons with Toast for visibility
            if (btnPizza != null) {
                btnPizza.setOnClickListener(v -> {
                    //android.widget.Toast.makeText(this, "Pizza Button Clicked!", android.widget.Toast.LENGTH_SHORT).show();
                    //android.util.Log.d("BaseActivity", "Pizza button clicked!");
                    navigateToPizza();
                });
            } else {
                android.util.Log.e("BaseActivity", "btnPizza is NULL!");
            }

            if (btnDrinks != null) {
                btnDrinks.setOnClickListener(v -> {
                    //android.widget.Toast.makeText(this, "Drinks Button Clicked!", android.widget.Toast.LENGTH_SHORT).show();
                    //android.util.Log.d("BaseActivity", "Drinks button clicked!");
                    navigateToDrinks();
                });
            } else {
                android.util.Log.e("BaseActivity", "btnDrinks is NULL!");
            }

            if (btnStarters != null) {
                btnStarters.setOnClickListener(v -> {
                    //android.widget.Toast.makeText(this, "Starters Button Clicked!", android.widget.Toast.LENGTH_SHORT).show();
                    //android.util.Log.d("BaseActivity", "Starters button clicked!");
                    navigateToStarters();
                });
            } else {
                android.util.Log.e("BaseActivity", "btnStarters is NULL!");
            }
        } else {
            android.util.Log.e("BaseActivity", "customActionBar is NULL!");
        }
    }

    // Highlight current page in bottom navigation
    protected void setSelectedNavItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    // Navigation methods
    private void navigateToHome() {
        if (this.getClass() != HomeActivity.class) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void navigateToFavorites() {
        if (this.getClass() != FavoritesActivity.class) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
    }

    private void navigateToCart() {
        if (this.getClass() != CartActivity.class) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
    }

    private void navigateToStores() {
        if (this.getClass() != StoresActivity.class) {
            Intent intent = new Intent(this, StoresActivity.class);
            startActivity(intent);
        }
    }

    private void navigateToPizza() {
        android.util.Log.d("BaseActivity", "navigateToPizza called");
        android.util.Log.d("BaseActivity", "Current activity: " + this.getClass().getSimpleName());

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category", "pizza");
        intent.putExtra("title", "Pizzas");
        startActivity(intent);

        android.util.Log.d("BaseActivity", "Intent sent to CategoryActivity");
    }

    private void navigateToDrinks() {
        android.util.Log.d("BaseActivity", "navigateToDrinks called");

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category", "drink");
        intent.putExtra("title", "Drinks");
        startActivity(intent);
    }

    private void navigateToStarters() {
        android.util.Log.d("BaseActivity", "navigateToStarters called");

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("category", "starter");
        intent.putExtra("title", "Starters");
        startActivity(intent);
    }

    // Override this method in child activities to handle search
    protected void onSearchQueryChanged(String query) {
        // Override in child activities
    }
}