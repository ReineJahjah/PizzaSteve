package com.example.pizzasteve.models;

public class CartItem {
    private String itemId;
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;

    // Empty constructor required for Firestore
    public CartItem() {}

    public CartItem(String itemId, String name, double price, int quantity, String imageUrl) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getTotalPrice() {
        return price * quantity;
    }
}