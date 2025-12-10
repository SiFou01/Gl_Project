package com.itms.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private String status;
    private String barcode;
    private String location;
    private String supplier;
    private String purchaseDate;
    private String warrantyEnd;
    private String description;
    private double price;

    public Product() {
    }

    public Product(int id, String name, String category, int quantity, String status, String barcode, String location,
            String supplier, String purchaseDate, String warrantyEnd, String description, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.status = status;
        this.barcode = barcode;
        this.location = location;
        this.supplier = supplier;
        this.purchaseDate = purchaseDate;
        this.warrantyEnd = warrantyEnd;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(String warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
