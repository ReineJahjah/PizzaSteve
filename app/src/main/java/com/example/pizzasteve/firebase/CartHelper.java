package com.example.pizzasteve.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.pizzasteve.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartHelper {
    private FirebaseFirestore db;
    private String userId;

    public CartHelper() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Add item to cart
    public void addToCart(CartItem item, OperationCallback callback) {
        db.collection("carts").document(userId)
                .collection("items").document(item.getItemId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Item exists, update quantity
                        CartItem existing = documentSnapshot.toObject(CartItem.class);
                        existing.setQuantity(existing.getQuantity() + item.getQuantity());
                        updateCartItem(existing, callback);
                    } else {
                        // New item, add to cart
                        db.collection("carts").document(userId)
                                .collection("items").document(item.getItemId())
                                .set(item)
                                .addOnSuccessListener(aVoid ->
                                        callback.onSuccess("Added to cart"))
                                .addOnFailureListener(e ->
                                        callback.onFailure(e.getMessage()));
                    }
                });
    }

    // Update cart item quantity   ///bl adapter
    public void updateCartItem(CartItem item, OperationCallback callback) {
        db.collection("carts").document(userId)
                .collection("items").document(item.getItemId())
                .set(item)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Cart updated"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Remove item from cart    ///bl adapter
    public void removeFromCart(String itemId, OperationCallback callback) {
        db.collection("carts").document(userId)
                .collection("items").document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess("Item removed"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Get all cart items //////1  & OrderHelper
    public void getCartItems(CartItemsCallback callback) {
        db.collection("carts").document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CartItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        CartItem item = doc.toObject(CartItem.class);
                        items.add(item);
                    }
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Clear cart   ////bl OrderHelper
    public void clearCart(OperationCallback callback) {
        db.collection("carts").document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                    callback.onSuccess("Cart cleared");
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Calculate total price  //////1
    public void calculateTotal(TotalCallback callback) {
        getCartItems(new CartItemsCallback() {
            @Override
            public void onSuccess(List<CartItem> items) {
                double total = 0;
                for (CartItem item : items) {
                    total += item.getTotalPrice();
                }
                callback.onTotal(total);
            }

            @Override
            public void onFailure(String error) {
                callback.onTotal(0);
            }
        });
    }

    // Callback interfaces
    public interface OperationCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface CartItemsCallback {
        void onSuccess(List<CartItem> items);
        void onFailure(String error);
    }

    public interface TotalCallback {
        void onTotal(double total);
    }
}