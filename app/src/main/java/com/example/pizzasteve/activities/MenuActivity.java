package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.adapters.MenuAdapter;
import com.example.pizzasteve.firebase.MenuHelper;
import com.example.pizzasteve.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView rvPizzas, rvDrinks, rvStarters;
    private MenuAdapter pizzaAdapter, drinkAdapter, starterAdapter;
    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize views
        rvPizzas = findViewById(R.id.rvPizzas);
        rvDrinks = findViewById(R.id.rvDrinks);
        rvStarters = findViewById(R.id.rvStarters);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Initialize MenuHelper
        menuHelper = new MenuHelper();

        // Load menu items
        loadMenuItems();
    }

    private void setupRecyclerViews() {
        // Pizzas
        rvPizzas.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        pizzaAdapter = new MenuAdapter(this, new ArrayList<>());
        rvPizzas.setAdapter(pizzaAdapter);

        // Drinks
        rvDrinks.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        drinkAdapter = new MenuAdapter(this, new ArrayList<>());
        rvDrinks.setAdapter(drinkAdapter);

        // Starters
        rvStarters.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        starterAdapter = new MenuAdapter(this, new ArrayList<>());
        rvStarters.setAdapter(starterAdapter);
    }

    private void loadMenuItems() {
        // Load pizzas
        menuHelper.getItemsByCategory("pizza", new MenuHelper.MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                pizzaAdapter.updateItems(items);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MenuActivity.this, "Failed to load pizzas",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Load drinks
        menuHelper.getItemsByCategory("drink", new MenuHelper.MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                drinkAdapter.updateItems(items);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MenuActivity.this, "Failed to load drinks",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Load starters
        menuHelper.getItemsByCategory("starter", new MenuHelper.MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                starterAdapter.updateItems(items);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MenuActivity.this, "Failed to load starters",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}