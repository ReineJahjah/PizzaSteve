package com.example.pizzasteve.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.pizzasteve.models.Order;
import com.example.pizzasteve.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class OrderHelper {
    private FirebaseFirestore db;
    private String userId;
    private CartHelper cartHelper;

    public OrderHelper() {
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartHelper = new CartHelper();
    }

    // Place order from cart
    public void placeOrder(OrderCallback callback) {
        // Get cart items
        cartHelper.getCartItems(new CartHelper.CartItemsCallback() {
            @Override
            public void onSuccess(List<CartItem> items) {
                if (items.isEmpty()) {
                    callback.onFailure("Cart is empty");
                    return;
                }

                // Calculate total
                double total = 0;
                for (CartItem item : items) {
                    total += item.getTotalPrice();
                }

                // Create order
                String orderId = db.collection("orders").document().getId();
                Order order = new Order(orderId, userId, items, total);

                // Save order to Firestore
                db.collection("orders").document(orderId)
                        .set(order)
                        .addOnSuccessListener(aVoid -> {
                            // Clear cart after successful order
                            cartHelper.clearCart(new CartHelper.OperationCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    callback.onSuccess("Order placed successfully", orderId);
                                }

                                @Override
                                public void onFailure(String error) {
                                    // Order placed but cart not cleared
                                    callback.onSuccess("Order placed", orderId);
                                }
                            });
                        })
                        .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    // Get user's order history (optional feature)
    public void getUserOrders(OrderListCallback callback) {
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc ->
                            orders.add(doc.toObject(Order.class)));
                    callback.onSuccess(orders);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Callback interfaces
    public interface OrderCallback {
        void onSuccess(String message, String orderId);
        void onFailure(String error);
    }

    public interface OrderListCallback {
        void onSuccess(List<Order> orders);
        void onFailure(String error);
    }
}