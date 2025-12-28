package com.example.pizzasteve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzasteve.R;
import com.example.pizzasteve.firebase.AuthHelper;
import com.example.pizzasteve.firebase.MenuHelper;
import com.example.pizzasteve.models.MenuItem;
import com.example.pizzasteve.utils.DatabaseSeeder;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvWelcome, tvMenuCount;
    private Button btnSeedDatabase, btnLoadMenu, btnOpenMenu, btnLogout;
    private AuthHelper authHelper;
    private MenuHelper menuHelper;
    private DatabaseSeeder seeder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvMenuCount = findViewById(R.id.tvMenuCount);
        btnSeedDatabase = findViewById(R.id.btnSeedDatabase);
        btnLoadMenu = findViewById(R.id.btnLoadMenu);
        btnOpenMenu = findViewById(R.id.btnOpenMenu);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize helpers
        authHelper = new AuthHelper();
        menuHelper = new MenuHelper();
        seeder = new DatabaseSeeder();

        // Display welcome message
        if (authHelper.getCurrentUser() != null) {
            tvWelcome.setText("Welcome, " + authHelper.getCurrentUser().getEmail());
        }

        // Seed database button
        btnSeedDatabase.setOnClickListener(v -> seedDatabase());

        // Load menu button
        btnLoadMenu.setOnClickListener(v -> loadMenuTest());

        // Open full menu button
        btnOpenMenu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            authHelper.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void seedDatabase() {
        btnSeedDatabase.setEnabled(false);
        tvMenuCount.setText("Seeding database... Please wait...");

        seeder.seedMenuItems(new DatabaseSeeder.SeedCallback() {
            @Override
            public void onComplete(boolean success, String message) {
                tvMenuCount.setText(message);
                btnSeedDatabase.setEnabled(true);

                if (success) {
                    Toast.makeText(MainActivity.this,
                            "Database seeded! You can now view the menu.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Seeding failed: " + message,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadMenuTest() {
        btnLoadMenu.setEnabled(false);
        tvMenuCount.setText("Loading menu...");

        menuHelper.getAllMenuItems(new MenuHelper.MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                tvMenuCount.setText("Menu items found: " + items.size());
                btnLoadMenu.setEnabled(true);

                // Show first item as test
                if (!items.isEmpty()) {
                    MenuItem first = items.get(0);
                    Toast.makeText(MainActivity.this,
                            "First item: " + first.getName() + " - $" + first.getPrice(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String error) {
                tvMenuCount.setText("Error loading menu: " + error);
                btnLoadMenu.setEnabled(true);
            }
        });
    }
}