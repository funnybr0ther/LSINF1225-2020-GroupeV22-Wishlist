package com.example.wishlist.Class;

public class Wishlist {
    private String name;
    private int size;
    private int userID;  //pas dans le diagramme UML
    private int[] productsReferences;
    private int wishlistID;


    public Wishlist(String name, int size, int userID, int[] products, int wishlistID ){
        this.name = name;
        this.size = size;
        this.userID = userID;
        this.productsReferences = products;
        this.wishlistID = wishlistID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(int wishlistID) {
        this.wishlistID = wishlistID;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int[] getProductsReferences() {
        return productsReferences;
    }

    public void setProductsReferences(int[] productsReferences) {
        this.productsReferences = productsReferences;
    }
}
