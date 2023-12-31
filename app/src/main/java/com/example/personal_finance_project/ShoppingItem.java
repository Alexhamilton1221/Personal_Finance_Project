package com.example.personal_finance_project;

public class ShoppingItem {
    private int itemId;
    private int groupId;
    private String name;
    private Double price;  // Use Double instead of double to allow for null
    private Integer quantity;  // Use Integer instead of int to allow for null

    // Constructor
    public ShoppingItem(int itemId, int groupId, String name, Double price, Integer quantity) {
        this.itemId = itemId;
        this.groupId = groupId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter and setter for 'itemId'
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    // Getter and setter for 'groupId'
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    // Getter and setter for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for 'price'
    public double getPrice() {
        return price != null ? price : 0;  // Return 0 if price is null
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Getter and setter for 'quantity'
    public int getQuantity() {
        return quantity != null ? quantity : 0;  // Return 0 if quantity is null
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
