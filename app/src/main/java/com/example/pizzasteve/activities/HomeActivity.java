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
    private Button btnExploreMenu, btnLogout;
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
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize helpers
        seeder = new DatabaseSeeder();
        authHelper = new AuthHelper();

        //this sets up the action bar buttons
        setupBottomNavigation();
        setupActionBar();
        setSelectedNavItem(R.id.nav_home);

        // Explore menu button
        btnExploreMenu.setOnClickListener(v -> {
            tvTagline.setText("Choose a category from the buttons above! 🍕🥤🍟");
        });

        // Logout button
        btnLogout.setOnClickListener(v -> logout());
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