package com.example.wishlist.Class;


public class Purchase {
    private int buyer;
    private int receiver;
    private int quantity;
    private DateWish date;
    private int productID;

    public Purchase(int acheteurID, int beneficiaireID, int productid, int quantity,  DateWish date) {
        this.buyer = acheteurID;
        this.receiver = beneficiaireID;
        this.quantity = quantity;
        this.date = date;
        this.productID = productid;
    }

    public int getReceiver() {return this.receiver;}

    public int getSender() {return this.buyer;}

    public int getProductID() {return this.productID;}

    public int getQuantity() {return this.quantity;}

    public DateWish getDate() {return this.date;}
}