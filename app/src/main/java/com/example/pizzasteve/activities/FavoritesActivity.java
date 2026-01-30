package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.adapters.MenuAdapter;
import com.example.pizzasteve.firebase.FavoritesHelper;
import com.example.pizzasteve.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends BaseActivity {
    private RecyclerView rvFavorites;
    private TextView tvEmptyMessage;
    private MenuAdapter adapter;
    private FavoritesHelper favoritesHelper;
    private List<MenuItem> allFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize views
        rvFavorites = findViewById(R.id.rvFavorites);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        // Setup RecyclerView
        rvFavorites.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MenuAdapter(this, new ArrayList<>());
        rvFavorites.setAdapter(adapter);

        // Setup navigation
        setupBottomNavigation();
        setupActionBar();
        setSelectedNavItem(R.id.nav_favorites);

        favoritesHelper = new FavoritesHelper();

        loadFavorites();
    }

    private void loadFavorites() {
        favoritesHelper.getFavoriteItems(new FavoritesHelper.FavoriteItemsCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                allFavorites = items;
                adapter.updateItems(items);

                if (items.isEmpty()) {
                    tvEmptyMessage.setVisibility(android.view.View.VISIBLE);
                    rvFavorites.setVisibility(android.view.View.GONE);
                } else {
                    tvEmptyMessage.setVisibility(android.view.View.GONE);
                    rvFavorites.setVisibility(android.view.View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(FavoritesActivity.this,
                        "Error: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSearchQueryChanged(String query) {
        if (allFavorites == null) return;

        if (query.isEmpty()) {
            adapter.updateItems(allFavorites);
        } else {
            List<MenuItem> filtered = new ArrayList<>();
            for (MenuItem item : allFavorites) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(item);
                }
            }
            adapter.updateItems(filtered);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Reload when returning to this page
    }
}