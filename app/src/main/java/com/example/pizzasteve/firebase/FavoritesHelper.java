package com.example.pizzasteve.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.pizzasteve.models.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesHelper {
    private FirebaseFirestore db;
    private String userId;

    public FavoritesHelper() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Add item to favorites
    public void addToFavorites(String itemId, OperationCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("items", FieldValue.arrayUnion(itemId));

        db.collection("favorites").document(userId)
                .set(data, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess("Added to favorites"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Remove item from favorites
    public void removeFromFavorites(String itemId, OperationCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("items", FieldValue.arrayRemove(itemId));

        db.collection("favorites").document(userId)
                .update(data)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Removed from favorites"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Check if item is favorite
    public void isFavorite(String itemId, FavoriteCheckCallback callback) {
        db.collection("favorites").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> items = (List<String>) documentSnapshot.get("items");
                        callback.onResult(items != null && items.contains(itemId));
                    } else {
                        callback.onResult(false);
                    }
                })
                .addOnFailureListener(e -> callback.onResult(false));
    }

    // Get all favorite items with details
    public void getFavoriteItems(FavoriteItemsCallback callback) {
        db.collection("favorites").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> itemIds = (List<String>) documentSnapshot.get("items");
                        if (itemIds != null && !itemIds.isEmpty()) {
                            fetchItemDetails(itemIds, callback);
                        } else {
                            callback.onSuccess(new ArrayList<>());
                        }
                    } else {
                        callback.onSuccess(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    private void fetchItemDetails(List<String> itemIds, FavoriteItemsCallback callback) {
        db.collection("menuItems")
                .whereIn("itemId", itemIds)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MenuItem> items = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc ->
                            items.add(doc.toObject(MenuItem.class)));
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Callback interfaces
    public interface OperationCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface FavoriteCheckCallback {
        void onResult(boolean isFavorite);
    }

    public interface FavoriteItemsCallback {
        void onSuccess(List<MenuItem> items);
        void onFailure(String error);
    }
}