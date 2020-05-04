package com.example.wishlist.Class;


public class Purchase {
    private static long compteur = 0; // Variable de Classe  > Permet de donner un numéro de commande unique
    private int buyer;
    private int receiver;
    private int quantity;
    private DateWish date;
    private int productID; // A changer si on sait stocker + récup un objet dans la bdd
    private long PurchaseId;

    public Purchase(int acheteurID, int beneficiaireID, int productid, int quantity,  DateWish date) {
        this.buyer = acheteurID;
        this.receiver = beneficiaireID;
        this.quantity = quantity;
        this.date = date;
        this.productID = productid;
        //compteur++;
        //this.PurchaseId = compteur;
    }

    public int getReceiver() {return this.receiver;}

    public int getSender() {return this.buyer;}

    public int getProductID() {return this.productID;}

    public int getQuantity() {return this.quantity;}

    public DateWish getDate() {return this.date;} // Ou autre manière de stocker la date ?

    //public long getPurchaseId() {return this.PurchaseId;}

    public String description() {
        return String.format("On the %s %s bought %s %s for %s.", getDate().toString(), getSender(), String.valueOf(getQuantity()), getProductID(), getReceiver());
    }
}