package com.example.wishlist.Class;

import android.graphics.Bitmap;

import com.example.wishlist.Class.Address;

import java.util.Calendar;
import java.util.Date;

public class User {
    private Address address;
    private String firstName;
    private String lastName;
    private String email;
    private DateWish birthDate;
    private String password;
    private Bitmap profilePhoto;
    private boolean notification=true;
    private String favoriteColor;
    private String size;
    private String shoeSize;

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public Bitmap getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Bitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public User(Address address, String firstName, String lastName, String email, DateWish birthDate, String password, Bitmap profilePhoto, String favoriteColor, String size, String shoeSize) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
        this.profilePhoto = profilePhoto;
        this.favoriteColor = favoriteColor;
        this.size = size;
        this.shoeSize = shoeSize;
    }

    public User(Address address, String firstName, String lastName, String email, DateWish birthDate, String password) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }


    public User() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateWish getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DateWish birthDate) {
        this.birthDate = birthDate;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}