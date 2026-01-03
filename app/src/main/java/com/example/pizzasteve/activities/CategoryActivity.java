package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.adapters.MenuAdapter;
import com.example.pizzasteve.firebase.MenuHelper;
import com.example.pizzasteve.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity {
    private static final String TAG = "CategoryActivity";
    private TextView tvCategoryTitle;
    private RecyclerView rvItems;
    private MenuAdapter adapter;
    private MenuHelper menuHelper;
    private String category;
    private List<MenuItem> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "CategoryActivity onCreate started");

        try {
            setContentView(R.layout.activity_category);
            Log.d(TAG, "Layout set successfully");

            // Get category from intent
            category = getIntent().getStringExtra("category");
            String title = getIntent().getStringExtra("title");

            Log.d(TAG, "Category: " + category + ", Title: " + title);

            // Initialize views
            tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
            rvItems = findViewById(R.id.rvItems);

            if (tvCategoryTitle == null) {
                Log.e(TAG, "tvCategoryTitle is NULL!");
            }
            if (rvItems == null) {
                Log.e(TAG, "rvItems is NULL!");
            }

            tvCategoryTitle.setText(title != null ? title : "Menu");

            // Setup RecyclerView
            rvItems.setLayoutManager(new GridLayoutManager(this, 2));
            adapter = new MenuAdapter(this, new ArrayList<>());
            rvItems.setAdapter(adapter);

            Log.d(TAG, "RecyclerView setup complete");

            // Setup navigation
            setupBottomNavigation();
            setupActionBar();
            //setSelectedNavItem(R.id.nav_home);

            Log.d(TAG, "Navigation setup complete");

            // Initialize helper
            menuHelper = new MenuHelper();

            // Load items
            loadItems();

            Log.d(TAG, "CategoryActivity onCreate completed successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading category: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadItems() {
        Log.d(TAG, "Loading items for category: " + category);

        menuHelper.getItemsByCategory(category, new MenuHelper.MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                Log.d(TAG, "Successfully loaded " + items.size() + " items");
                allItems = items;
                adapter.updateItems(items);

                if (items.isEmpty()) {
                    Toast.makeText(CategoryActivity.this,
                            "No items found in this category",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Error loading items: " + error);
                Toast.makeText(CategoryActivity.this,
                        "Error: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSearchQueryChanged(String query) {
        if (allItems == null) return;

        if (query.isEmpty()) {
            adapter.updateItems(allItems);
        } else {
            List<MenuItem> filtered = new ArrayList<>();
            for (MenuItem item : allItems) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(item);
                }
            }
            adapter.updateItems(filtered);
        }
    }
}