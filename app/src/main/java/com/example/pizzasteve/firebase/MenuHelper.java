package com.example.pizzasteve.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.pizzasteve.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    private FirebaseFirestore db;

    public MenuHelper() {
        db = FirebaseFirestore.getInstance();
    }

    // Get all menu items
    public void getAllMenuItems(MenuCallback callback) {
        db.collection("menuItems")
                .whereEqualTo("available", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MenuItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        MenuItem item = doc.toObject(MenuItem.class);
                        items.add(item);
                    }
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Get items by category
    public void getItemsByCategory(String category, MenuCallback callback) {
        db.collection("menuItems")
                .whereEqualTo("category", category)
                .whereEqualTo("available", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MenuItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        MenuItem item = doc.toObject(MenuItem.class);
                        items.add(item);
                    }
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Search items by name
    public void searchItems(String query, MenuCallback callback) {
        getAllMenuItems(new MenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                List<MenuItem> filteredItems = new ArrayList<>();
                for (MenuItem item : items) {
                    if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                callback.onSuccess(filteredItems);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    // Get single item by ID
    public void getItemById(String itemId, SingleItemCallback callback) {
        db.collection("menuItems").document(itemId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        MenuItem item = documentSnapshot.toObject(MenuItem.class);
                        callback.onSuccess(item);
                    } else {
                        callback.onFailure("Item not found");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Callback interfaces
    public interface MenuCallback {
        void onSuccess(List<MenuItem> items);
        void onFailure(String error);
    }

    public interface SingleItemCallback {
        void onSuccess(MenuItem item);
        void onFailure(String error);
    }
}