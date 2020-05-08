package com.example.wishlist.Class;

import android.graphics.Bitmap;

/**
 * Product represents a gift that a user wants to receive. It has multiple fields to differentiate
 * it from other one (name, price, photo, ...). All products start by belonging to a wishlist (this
 * is no longer the case when they are deleted). For more information, see the report or class
 * diagrams
 */
public class Product {
    private String name;
    private Bitmap photo;
    private String description;
    private String[] category;
    private Integer weight;
    private Integer price;
    private Integer desire;
    private String dimensions;
    private Integer total;
    private Integer purchased;

    /**
     * Constructor for the Product class
     * @param name the name of the product
     * @param photo the bitmap image of the product
     * @param description a description of the product, or further information that did not fit
     *                    in any other field
     * @param category the categories of the product (a complete list can be found in string.xml)
     * @param weight the integer weight of the product
     * @param price the price of the product
     * @param desire 0 (no particular desire) or 1->5, the desire of the user.
     * @param dimensions the dimensions of the product
     * @param amount the total amount of 'products' the user wants
     * @param purchased the amount of products already received (auto-incremented when offered)
     */
    public Product(String name, Bitmap photo, String description, String[] category, Integer weight, Integer price, Integer desire, String dimensions, Integer amount, Integer purchased) {
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.category = category;
        this.weight = weight;
        this.price = price;
        this.desire = desire;
        this.dimensions = dimensions;
        this.total = amount;
        this.purchased = purchased;
    }

    public Product(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getDesire() {
        return desire;
    }

    public void setDesire(int desire) {
        this.desire = desire;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(int amount) {
        this.total = amount;
    }

    public Integer getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }
}
