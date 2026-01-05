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
        totalItems = 20; // 7 pizzas + 6 drinks + 7 starters
        completedItems = 0;

        // Pizzas
        // Use drawable images - store resource name as string
        addMenuItem("pizza1", "Margherita", "Classic tomato sauce, fresh mozzarella, and basil",
                12.99, "pizza", "pizza_margherita", callback);

        addMenuItem("pizza2", "Pepperoni", "Tomato sauce, mozzarella, and spicy pepperoni",
                14.99, "pizza", "pizza_pepperoni", callback);

        addMenuItem("pizza3", "Hawaiian", "Tomato sauce, mozzarella, ham, and pineapple",
                13.99, "pizza", "pizza_hawaiian", callback);

        addMenuItem("pizza4", "Vegetarian", "Tomato sauce, mozzarella, peppers, mushrooms, olives",
                13.49, "pizza", "pizza_vegetarian", callback);

        addMenuItem("pizza5", "BBQ Chicken", "BBQ sauce, chicken, red onions, and mozzarella",
                15.99, "pizza", "pizza_bbq_chicken", callback);

        addMenuItem("pizza7", "Meat Lovers", "Pepperoni, sausage, ham, bacon, and mozzarella",
                16.99, "pizza", "pizza_meat_lovers", callback);

        addMenuItem("pizza8", "Spicy Inferno", "Tomato sauce, mozzarella, jalapeños, chili flakes, spicy beef",
                15.99, "pizza", "pizza_spicy_inferno", callback);

        // Drinks
        addMenuItem("drink1", "Coca Cola", "Classic Coca Cola 330ml can",
                2.49, "drink", "drink_cola", callback);

        addMenuItem("drink2", "Sprite", "Refreshing lemon-lime Sprite 330ml",
                2.49, "drink", "drink_sprite", callback);

        addMenuItem("drink3", "Orange Juice", "Fresh squeezed orange juice 250ml",
                3.49, "drink", "drink_orange", callback);

        addMenuItem("drink4", "Water", "Still mineral water 500ml",
                1.99, "drink", "drink_water", callback);

        addMenuItem("drink6", "Iced Tea", "Cold lemon iced tea 330ml",
                2.99, "drink", "drink_iced_tea", callback);


        // Starters
        addMenuItem("starter1", "Garlic Bread", "Toasted Italian bread with garlic butter",
                4.99, "starter", "starter_garlic_bread", callback);

        addMenuItem("starter2", "Chicken Wings", "Spicy buffalo chicken wings (6 pieces)",
                7.99, "starter", "starter_chicken_wings", callback);

        addMenuItem("starter3", "Mozzarella Sticks", "Fried mozzarella sticks with marinara (5 pieces)",
                5.99, "starter", "starter_mozzarella_sticks", callback);

        addMenuItem("starter4", "Caesar Salad", "Fresh romaine lettuce with Caesar dressing",
                6.49, "starter", "starter_caesar_salad", callback);

        addMenuItem("starter5", "French Fries", "Crispy golden french fries",
                3.99, "starter", "starter_fries", callback);

        addMenuItem("starter6", "Onion Rings", "Crispy battered onion rings",
                4.49, "starter", "starter_onion_rings", callback);

        addMenuItem("starter7", "Stuffed Mushrooms", "Mushrooms stuffed with cheese and herbs",
                6.99, "starter", "starter_stuffed_mushrooms", callback);
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