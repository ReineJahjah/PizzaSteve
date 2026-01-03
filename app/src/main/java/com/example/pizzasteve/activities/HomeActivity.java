package com.example.pizzasteve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pizzasteve.R;
import com.example.pizzasteve.firebase.AuthHelper;
import com.example.pizzasteve.utils.DatabaseSeeder;

public class HomeActivity extends BaseActivity {
    private ImageView ivWelcome;
    private TextView tvWelcome, tvTagline;
    private Button btnExploreMenu, btnSeedDatabase, btnLogout;
    private DatabaseSeeder seeder;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        ivWelcome = findViewById(R.id.ivWelcome);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTagline = findViewById(R.id.tvTagline);
        btnExploreMenu = findViewById(R.id.btnExploreMenu);
        btnSeedDatabase = findViewById(R.id.btnSeedDatabase);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize helpers
        seeder = new DatabaseSeeder();
        authHelper = new AuthHelper();

        // Setup navigation FIRST - this sets up the action bar buttons
        setupBottomNavigation();
        setupActionBar();
        setSelectedNavItem(R.id.nav_home);

        // DON'T override the action bar buttons in HomeActivity
        // They are already setup in BaseActivity.setupActionBar()

        // Explore menu button
        btnExploreMenu.setOnClickListener(v -> {
            tvTagline.setText("Choose a category from the buttons above! 🍕🥤🍟");
        });

        // Seed database button
        btnSeedDatabase.setOnClickListener(v -> seedDatabase());

        // Logout button
        btnLogout.setOnClickListener(v -> logout());
    }

    private void seedDatabase() {
        btnSeedDatabase.setEnabled(false);
        btnSeedDatabase.setText("Seeding...");
        tvTagline.setText("Adding menu items to database...");

        seeder.seedMenuItems(new DatabaseSeeder.SeedCallback() {
            @Override
            public void onComplete(boolean success, String message) {
                btnSeedDatabase.setEnabled(true);
                btnSeedDatabase.setText("🌱 Seed Database");

                if (success) {
                    tvTagline.setText("✅ " + message + " Ready to explore!");
                    Toast.makeText(HomeActivity.this,
                            "Database seeded successfully!",
                            Toast.LENGTH_LONG).show();
                } else {
                    tvTagline.setText("❌ " + message);
                    Toast.makeText(HomeActivity.this,
                            "Seeding failed: " + message,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void logout() {
        authHelper.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSearchQueryChanged(String query) {
        if (!query.isEmpty()) {
            tvTagline.setText("Searching for: " + query);
        } else {
            tvTagline.setText("Delicious pizza delivered to your door! 🚀");
        }
    }
}