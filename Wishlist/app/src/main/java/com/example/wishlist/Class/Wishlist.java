package com.example.wishlist.Class;

public class Wishlist {
    private String name;
    private int size;
    private int userID;
    private int[] productsReferences;
    private int wishlistID;


    /**
     * Constructor for the Wishlist class
     * @param name the name of the wishlist
     * @param size the number of product in the wishlist
     * @param userID the userID of the wishlist owner
     * @param products list of product reference that compound the wishlist
     * @param wishlistID number that is unique to the wishlist
     */
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
