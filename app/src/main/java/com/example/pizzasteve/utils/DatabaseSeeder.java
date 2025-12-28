package com.example.pizzasteve.utils;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.pizzasteve.models.MenuItem;

public class DatabaseSeeder {
    private static final String TAG = "DatabaseSeeder";
    private FirebaseFirestore db;
    private int totalItems = 0;
    private int completedItems = 0;

    public DatabaseSeeder() {
        db = FirebaseFirestore.getInstance();
    }

    public interface SeedCallback {
        void onComplete(boolean success, String message);
    }

    public void seedMenuItems(SeedCallback callback) {
        Log.d(TAG, "Starting database seeding...");

        // Count total items to add
        totalItems = 13; // 5 pizzas + 4 drinks + 4 starters
        completedItems = 0;

        // Pizzas
        addMenuItem("pizza1", "Margherita", "Classic tomato sauce, fresh mozzarella, and basil",
                12.99, "pizza", "https://via.placeholder.com/300x200/FF6B6B/FFFFFF?text=Margherita", callback);

        addMenuItem("pizza2", "Pepperoni", "Tomato sauce, mozzarella, and spicy pepperoni",
                14.99, "pizza", "https://via.placeholder.com/300x200/4ECDC4/FFFFFF?text=Pepperoni", callback);

        addMenuItem("pizza3", "Hawaiian", "Tomato sauce, mozzarella, ham, and pineapple",
                13.99, "pizza", "https://via.placeholder.com/300x200/FFE66D/FFFFFF?text=Hawaiian", callback);

        addMenuItem("pizza4", "Vegetarian", "Tomato sauce, mozzarella, peppers, mushrooms, olives",
                13.49, "pizza", "https://via.placeholder.com/300x200/95E1D3/FFFFFF?text=Vegetarian", callback);

        addMenuItem("pizza5", "BBQ Chicken", "BBQ sauce, chicken, red onions, and mozzarella",
                15.99, "pizza", "https://via.placeholder.com/300x200/F38181/FFFFFF?text=BBQ+Chicken", callback);

        // Drinks
        addMenuItem("drink1", "Coca Cola", "Classic Coca Cola 330ml can",
                2.49, "drink", "https://via.placeholder.com/300x200/E74C3C/FFFFFF?text=Coca+Cola", callback);

        addMenuItem("drink2", "Sprite", "Refreshing lemon-lime Sprite 330ml",
                2.49, "drink", "https://via.placeholder.com/300x200/2ECC71/FFFFFF?text=Sprite", callback);

        addMenuItem("drink3", "Orange Juice", "Fresh squeezed orange juice 250ml",
                3.49, "drink", "https://via.placeholder.com/300x200/F39C12/FFFFFF?text=Orange+Juice", callback);

        addMenuItem("drink4", "Water", "Still mineral water 500ml",
                1.99, "drink", "https://via.placeholder.com/300x200/3498DB/FFFFFF?text=Water", callback);

        // Starters
        addMenuItem("starter1", "Garlic Bread", "Toasted Italian bread with garlic butter",
                4.99, "starter", "https://via.placeholder.com/300x200/D4AF37/FFFFFF?text=Garlic+Bread", callback);

        addMenuItem("starter2", "Chicken Wings", "Spicy buffalo chicken wings (6 pieces)",
                7.99, "starter", "https://via.placeholder.com/300x200/CD853F/FFFFFF?text=Chicken+Wings", callback);

        addMenuItem("starter3", "Mozzarella Sticks", "Fried mozzarella sticks with marinara (5 pieces)",
                5.99, "starter", "https://via.placeholder.com/300x200/FFD700/FFFFFF?text=Mozzarella", callback);

        addMenuItem("starter4", "Caesar Salad", "Fresh romaine lettuce with Caesar dressing",
                6.49, "starter", "https://via.placeholder.com/300x200/32CD32/FFFFFF?text=Caesar+Salad", callback);
    }

    private void addMenuItem(String id, String name, String description,
                             double price, String category, String imageUrl, SeedCallback callback) {
        MenuItem item = new MenuItem(id, name, description, price, category, imageUrl);

        db.collection("menuItems").document(id)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    completedItems++;
                    Log.d(TAG, "Added: " + name + " (" + completedItems + "/" + totalItems + ")");

                    if (completedItems == totalItems) {
                        callback.onComplete(true, "Successfully added " + totalItems + " items!");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add: " + name, e);
                    callback.onComplete(false, "Error: " + e.getMessage());
                });
    }
}