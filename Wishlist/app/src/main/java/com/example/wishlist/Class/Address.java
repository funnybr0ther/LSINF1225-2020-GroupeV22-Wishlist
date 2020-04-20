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
        return addressLine1+"_/"+addressLine2+"_/"+city+"_/"+country+"_/"+postalCode;
    }

    public static Address fromString(String str){
        String[]l=str.split("_",-1);
        return new Address(l[0],l[1],l[2],l[3],Integer.parseInt(l[4]));
    }

    public Address(String addressLine1, String city, String country, int postalCode) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Address(String addressLine1, String addressLine2, String city, String country, int postalCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
