package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wishlist.Activities.ListWishlistActivity;

import java.util.ArrayList;

public class WishlistDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="wishlist.db";

    //PREMIER TABLE
    private static final String USER_TABLE_NAME_A="detailWishlist";
    public static final String USER_COL0_A="wishlistID";
    public static final String USER_COL1_A="productReference";


    //DEUXIEME TABLE
    private static final String USER_TABLE_NAME_B="wishlist";
    public static final String USER_COL0_B="userID";
    public static final String USER_COL1_B="wishlistID";
    public static final String USER_COL2_B="name";

    public WishlistDatabaseHelper(@Nullable Context context) { super(context, DATABASE_NAME, null, 1); }

    /*

    Creation de 2 tables de données dans la même classe, afin de respecter notre choix de conception de la BDD,
    main en facilitant les echanges fréquents entre les 2 table.

    A : Table avec le contenu des wishlists

    B : Table avec la liste des wishlists
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand_A="CREATE TABLE "+
                USER_TABLE_NAME_A + " ("+
                USER_COL0_A + " INTEGER REFERENCES wishlist (\"wishlistID\") NOT NULL, "+   // PAIRE UNIQUE
                USER_COL1_A + " INTEGER REFERENCES product (\"productReference\") NOT NULL)";
        db.execSQL(sqlCommand_A);

        String sqlCommand_B="CREATE TABLE "+
                USER_TABLE_NAME_B + " ("+
                USER_COL0_B + " INTEGER NOT NULL REFERENCES user (\"userID\"), "+
                USER_COL1_B + " INTEGER NOT NULL , "+
                USER_COL2_B + " TEXT NOT NULL)";
        db.execSQL(sqlCommand_B);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String sqlCommand_A="CREATE TABLE IF NOT EXISTS "+
                USER_TABLE_NAME_A + " ("+
                USER_COL0_A + " INTEGER REFERENCES wishlist (\"wishlistID\") NOT NULL, "+   // PAIRE UNIQUE
                USER_COL1_A + " INTEGER REFERENCES product (\"productReference\") NOT NULL)";
        db.execSQL(sqlCommand_A);

        String sqlCommand_B="CREATE TABLE IF NOT EXISTS "+
                USER_TABLE_NAME_B + " ("+
                USER_COL0_B + " INTEGER NOT NULL REFERENCES user (\"userID\"), "+
                USER_COL1_B + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                USER_COL2_B + " TEXT NOT NULL)";
        db.execSQL(sqlCommand_B);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand="DROP IF TABLE EXISTS " + DATABASE_NAME;
        onCreate(db);
    }

    //WISHLIST DETAIL
    public int[] getProducts(int wishlistID){
        SQLiteDatabase db= getReadableDatabase();
        String strSql = "SELECT " + USER_COL1_A + " FROM " + USER_TABLE_NAME_A + " WHERE " + USER_COL0_A + " = '" + wishlistID + "'";
        Cursor cursor = db.rawQuery(strSql, null);

        int[] productArray = new int[cursor.getCount()];

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int prod = cursor.getInt(cursor.getColumnIndex(USER_COL1_A));

            productArray[i] = prod;
            cursor.moveToNext();
            i++;
        }
        return productArray;
    }

    //WISHLIST
    public boolean changeWishlistName(int wishlistID, String newName, int userID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL0_B,userID);
        contentValues.put(USER_COL1_B,wishlistID);
        contentValues.put(USER_COL2_B,newName);
        int err=db.update(USER_TABLE_NAME_B,contentValues, USER_COL1_B +" = ?",new String[]{String.valueOf(wishlistID)});
        return err!=-1;
    }

    //WISHLIST DETAIL
    /*public int[] getQuantity(int wishlistID){
        SQLiteDatabase db=getReadableDatabase();
        String strSql = "SELECT " + USER_COL2_A + " FROM " + USER_TABLE_NAME_A + " WHERE " + USER_COL0_A + " = '" + wishlistID + "'";
        Cursor cursor = db.rawQuery(strSql, null);

        int[] quantityArray = new int[cursor.getCount()];

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int quantity = cursor.getInt(cursor.getColumnIndex(USER_COL2_A));

            quantityArray[i] = quantity;
            cursor.moveToNext();
            i++;
        }
        return quantityArray;
    }*/

    //WISHLIST DETAIL
    public boolean addProduct(int productID, int wishlistID){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL0_A,wishlistID);
        contentValues.put(USER_COL1_A,productID);
        long err=db.insert(USER_TABLE_NAME_A,null,contentValues);
        return err!=-1;
    }

    //WISHLIST
    public boolean addWishlist(String name, int userID){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL0_B,userID);
        contentValues.put(USER_COL2_B,name);
        long err=db.insert(USER_TABLE_NAME_B,null,contentValues);
        return err!=-1;
    }

    public void deleteProductFromWishlist(int productID){
        SQLiteDatabase db = getWritableDatabase();
        String sqlCommand = "DELETE FROM " + USER_TABLE_NAME_A + " WHERE " + USER_COL1_A + "= " + productID;
        db.execSQL(sqlCommand);
    }
    //WISHLIST
    public ArrayList<Wishlist> getUserWishlist(int userID){
        SQLiteDatabase db= getReadableDatabase();
        ArrayList<Wishlist> wishlists = new ArrayList<Wishlist>();

        String strSql = "SELECT " + USER_COL1_B +", " + USER_COL2_B + " FROM " + USER_TABLE_NAME_B + " WHERE " + USER_COL0_B + " = '" + userID + '\'' ;
        Cursor cursor = db.rawQuery(strSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int wishlistID = cursor.getInt(cursor.getColumnIndex(USER_COL1_B));
            String name = cursor.getString(cursor.getColumnIndex(USER_COL2_B));
            int[] productArray = getProducts(wishlistID);

            Wishlist wishlist = new Wishlist(name,productArray.length, userID, productArray, wishlistID);

            wishlists.add(wishlist);
            cursor.moveToNext();
        }
        return wishlists;
    }

    //WISHLIST DETAIL
    public int deleteAllWishlistProduct(int wishlistID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL0_A + " = " + wishlistID, null);
    }

    //WISHLIST DETAIL
    public int deleteProductInAllWishlist(int productID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL1_A + " = " + productID, null);
    }
    //WISHLIST DETAIL
    public int deleteProductInOneWishlist(int productID, int wishlistID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL1_A + " = " + productID + " AND " + USER_COL0_A + " = " + wishlistID, null);
    }
    //WISHLIST
    public int deleteWishlistWithProduct(int wishlistID){
        deleteAllWishlistProduct(wishlistID);
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_B, USER_COL1_B + " = " + wishlistID, null);
    }
}