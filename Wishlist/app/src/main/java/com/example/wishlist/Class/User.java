package com.example.wishlist.Class;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.wishlist.Class.Address;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class User {
    private Address address;  //commentraire
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
    private int userID;

    public boolean isNotification() {
        return this.notification;
    }

    public void setUserID(final int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setNotification(final boolean notification) {
        this.notification = notification;
    }

    public Bitmap getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(final Bitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public User(final Address address, final String firstName, final String lastName, final String email, final DateWish birthDate, final String password, final Bitmap profilePhoto, final String favoriteColor, final String size, final String shoeSize) {
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

    public User(final Address address, final String firstName, final String lastName, final String email, final DateWish birthDate, final String password) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getShoeSize() {
        return this.shoeSize;
    }

    public void setShoeSize(final String shoeSize) {
        this.shoeSize = shoeSize;
    }


    public User() {
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public DateWish getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(final DateWish birthDate) {
        this.birthDate = birthDate;
    }

    public String getFavoriteColor() {
        return this.favoriteColor;
    }

    public void setFavoriteColor(final String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final User user = (User) o;
        return this.userID ==user.getUserID();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(this.address, this.firstName, this.lastName, this.email, this.birthDate, this.password, this.profilePhoto, this.notification, this.favoriteColor, this.size, this.shoeSize, this.userID);
    }
}