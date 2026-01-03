package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.example.pizzasteve.R;

public class StoresActivity extends BaseActivity {
    private TextView tvStoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        // Initialize views
        tvStoreInfo = findViewById(R.id.tvStoreInfo);

        // Setup navigation
        setupBottomNavigation();
        setupActionBar();
        setSelectedNavItem(R.id.nav_stores);

        // Display store information
        displayStoreInfo();
    }

    private void displayStoreInfo() {
        String storeInfo = "📍 Our Locations:\n\n" +
                "Store 1 - Downtown\n" +
                "123 Main Street\n" +
                "Open: 10:00 AM - 11:00 PM\n\n" +
                "Store 2 - North Side\n" +
                "456 Oak Avenue\n" +
                "Open: 11:00 AM - 10:00 PM\n\n" +
                "Store 3 - South Plaza\n" +
                "789 Pine Road\n" +
                "Open: 10:00 AM - 12:00 AM\n\n" +
                "📞 Call us: 1-800-PIZZA-STEVE";

        tvStoreInfo.setText(storeInfo);
    }
}