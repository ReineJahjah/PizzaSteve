package com.example.pizzasteve.models;

import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<CartItem> items;
    private double totalPrice;
    private String status; // "pending", "completed", "cancelled"
    private long orderDate;

    // Empty constructor required for Firestore
    public Order() {}

    public Order(String orderId, String userId, List<CartItem> items, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = "pending";
        this.orderDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }
}