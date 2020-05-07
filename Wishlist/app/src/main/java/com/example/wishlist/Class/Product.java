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
    public Product(final String name, final Bitmap photo, final String description, final String[] category, final Integer weight, final Integer price, final Integer desire, final String dimensions, final Integer amount, final Integer purchased) {
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.category = category;
        this.weight = weight;
        this.price = price;
        this.desire = desire;
        this.dimensions = dimensions;
        total = amount;
        this.purchased = purchased;
    }

    public Product(){

    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return this.photo;
    }

    public void setPhoto(final Bitmap photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String[] getCategory() {
        return this.category;
    }

    public void setCategory(final String[] category) {
        this.category = category;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public Integer getDesire() {
        return this.desire;
    }

    public void setDesire(final int desire) {
        this.desire = desire;
    }

    public String getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(final String dimensions) {
        this.dimensions = dimensions;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(final int amount) {
        total = amount;
    }

    public Integer getPurchased() {
        return this.purchased;
    }

    public void setPurchased(final int purchased) {
        this.purchased = purchased;
    }
}
