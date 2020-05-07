package com.example.wishlist.Class;


public class Purchase {
    private final int buyer;
    private final int receiver;
    private final int quantity;
    private final DateWish date;
    private final int productID;

    public Purchase(final int acheteurID, final int beneficiaireID, final int productid, final int quantity, final DateWish date) {
        buyer = acheteurID;
        receiver = beneficiaireID;
        this.quantity = quantity;
        this.date = date;
        productID = productid;
    }

    public int getReceiver() {return receiver;}

    public int getSender() {return buyer;}

    public int getProductID() {return productID;}

    public int getQuantity() {return quantity;}

    public DateWish getDate() {return date;}
}