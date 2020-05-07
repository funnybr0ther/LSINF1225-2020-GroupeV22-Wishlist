package com.example.wishlist.Class;

public class Wishlist {
    private String name;
    private int size;
    private int userID;  //pas dans le diagramme UML
    private int[] productsReferences;
    private int wishlistID;


    public Wishlist(final String name, final int size, final int userID, final int[] products, final int wishlistID ){
        this.name = name;
        this.size = size;
        this.userID = userID;
        productsReferences = products;
        this.wishlistID = wishlistID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getWishlistID() {
        return this.wishlistID;
    }

    public void setWishlistID(final int wishlistID) {
        this.wishlistID = wishlistID;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(final int userID) {
        this.userID = userID;
    }

    public int[] getProductsReferences() {
        return this.productsReferences;
    }

    public void setProductsReferences(final int[] productsReferences) {
        this.productsReferences = productsReferences;
    }
}
