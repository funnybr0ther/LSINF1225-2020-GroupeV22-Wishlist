package com.example.wishlist.Class;

import androidx.annotation.NonNull;

public class Address {
    private String addressLine1;
    private String addressLine2="";
    private String city;
    private String country;
    private int postalCode;

    /*
    * 2 function to transform address into String and String into address to stock it in database
     */
    @NonNull
    public String toString(){
        return this.addressLine1 +"_/"+ this.addressLine2 +"_/"+ this.city +"_/"+ this.country +"_/"+ this.postalCode;
    }

    public static Address fromString(final String str){
        final String[]l=str.split("_/",-1);
        return new Address(l[0],l[1],l[2],l[3],Integer.parseInt(l[4]));
    }

    public Address(final String addressLine1, final String city, final String country, final int postalCode) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Address(final String addressLine1, final String addressLine2, final String city, final String country, final int postalCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public int getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(final int postalCode) {
        this.postalCode = postalCode;
    }
    // Bilibu here
}
