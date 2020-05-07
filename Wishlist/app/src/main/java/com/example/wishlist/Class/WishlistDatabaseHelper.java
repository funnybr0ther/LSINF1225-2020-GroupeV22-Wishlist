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

    public WishlistDatabaseHelper(@Nullable final Context context) { super(context, WishlistDatabaseHelper.DATABASE_NAME, null, 1); }

    /*

    Creation de 2 tables de données dans la même classe, afin de respecter notre choix de conception de la BDD,
    main en facilitant les echanges fréquents entre les 2 table.

    A : Table avec le contenu des wishlists

    B : Table avec la liste des wishlists
     */

    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String sqlCommand_A="CREATE TABLE "+
                WishlistDatabaseHelper.USER_TABLE_NAME_A + " ("+
                WishlistDatabaseHelper.USER_COL0_A + " INTEGER REFERENCES wishlist (\"wishlistID\") NOT NULL, "+   // PAIRE UNIQUE
                WishlistDatabaseHelper.USER_COL1_A + " INTEGER REFERENCES product (\"productReference\") NOT NULL)";
        db.execSQL(sqlCommand_A);

        final String sqlCommand_B="CREATE TABLE "+
                WishlistDatabaseHelper.USER_TABLE_NAME_B + " ("+
                WishlistDatabaseHelper.USER_COL0_B + " INTEGER NOT NULL REFERENCES user (\"userID\"), "+
                WishlistDatabaseHelper.USER_COL1_B + " INTEGER NOT NULL , "+
                WishlistDatabaseHelper.USER_COL2_B + " TEXT NOT NULL)";
        db.execSQL(sqlCommand_B);
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        final String sqlCommand_A="CREATE TABLE IF NOT EXISTS "+
                WishlistDatabaseHelper.USER_TABLE_NAME_A + " ("+
                WishlistDatabaseHelper.USER_COL0_A + " INTEGER REFERENCES wishlist (\"wishlistID\") NOT NULL, "+   // PAIRE UNIQUE
                WishlistDatabaseHelper.USER_COL1_A + " INTEGER REFERENCES product (\"productReference\") NOT NULL)";
        db.execSQL(sqlCommand_A);

        final String sqlCommand_B="CREATE TABLE IF NOT EXISTS "+
                WishlistDatabaseHelper.USER_TABLE_NAME_B + " ("+
                WishlistDatabaseHelper.USER_COL0_B + " INTEGER NOT NULL REFERENCES user (\"userID\"), "+
                WishlistDatabaseHelper.USER_COL1_B + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                WishlistDatabaseHelper.USER_COL2_B + " TEXT NOT NULL)";
        db.execSQL(sqlCommand_B);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        final String sqlCommand="DROP IF TABLE EXISTS " + WishlistDatabaseHelper.DATABASE_NAME;
        this.onCreate(db);
    }

    //WISHLIST DETAIL
    public int[] getProducts(final int wishlistID){
        final SQLiteDatabase db= this.getReadableDatabase();
        final String strSql = "SELECT " + WishlistDatabaseHelper.USER_COL1_A + " FROM " + WishlistDatabaseHelper.USER_TABLE_NAME_A + " WHERE " + WishlistDatabaseHelper.USER_COL0_A + " = '" + wishlistID + "'";
        final Cursor cursor = db.rawQuery(strSql, null);

        final int[] productArray = new int[cursor.getCount()];

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final int prod = cursor.getInt(cursor.getColumnIndex(WishlistDatabaseHelper.USER_COL1_A));

            productArray[i] = prod;
            cursor.moveToNext();
            i++;
        }
        return productArray;
    }

    //WISHLIST
    public boolean changeWishlistName(final int wishlistID, final String newName, final int userID){
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(WishlistDatabaseHelper.USER_COL0_B,userID);
        contentValues.put(WishlistDatabaseHelper.USER_COL1_B,wishlistID);
        contentValues.put(WishlistDatabaseHelper.USER_COL2_B,newName);
        final int err=db.update(WishlistDatabaseHelper.USER_TABLE_NAME_B,contentValues, WishlistDatabaseHelper.USER_COL1_B +" = ?",new String[]{String.valueOf(wishlistID)});
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
    public boolean addProduct(final int productID, final int wishlistID){
        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(WishlistDatabaseHelper.USER_COL0_A,wishlistID);
        contentValues.put(WishlistDatabaseHelper.USER_COL1_A,productID);
        final long err=db.insert(WishlistDatabaseHelper.USER_TABLE_NAME_A,null,contentValues);
        return err!=-1;
    }

    //WISHLIST
    public boolean addWishlist(final String name, final int userID){

        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(WishlistDatabaseHelper.USER_COL0_B,userID);
        contentValues.put(WishlistDatabaseHelper.USER_COL2_B,name);
        final long err=db.insert(WishlistDatabaseHelper.USER_TABLE_NAME_B,null,contentValues);
        return err!=-1;
    }

    public void deleteProductFromWishlist(final int productID){
        final SQLiteDatabase db = this.getWritableDatabase();
        final String sqlCommand = "DELETE FROM " + WishlistDatabaseHelper.USER_TABLE_NAME_A + " WHERE " + WishlistDatabaseHelper.USER_COL1_A + "= " + productID;
        db.execSQL(sqlCommand);
    }
    //WISHLIST
    public ArrayList<Wishlist> getUserWishlist(final int userID){
        final SQLiteDatabase db= this.getReadableDatabase();
        final ArrayList<Wishlist> wishlists = new ArrayList<Wishlist>();

        final String strSql = "SELECT " + WishlistDatabaseHelper.USER_COL1_B +", " + WishlistDatabaseHelper.USER_COL2_B + " FROM " + WishlistDatabaseHelper.USER_TABLE_NAME_B + " WHERE " + WishlistDatabaseHelper.USER_COL0_B + " = '" + userID + '\'' ;
        final Cursor cursor = db.rawQuery(strSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final int wishlistID = cursor.getInt(cursor.getColumnIndex(WishlistDatabaseHelper.USER_COL1_B));
            final String name = cursor.getString(cursor.getColumnIndex(WishlistDatabaseHelper.USER_COL2_B));
            final int[] productArray = this.getProducts(wishlistID);

            final Wishlist wishlist = new Wishlist(name,productArray.length, userID, productArray, wishlistID);

            wishlists.add(wishlist);
            cursor.moveToNext();
        }
        return wishlists;
    }

    //WISHLIST DETAIL
    public int deleteAllWishlistProduct(final int wishlistID){
        final SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WishlistDatabaseHelper.USER_TABLE_NAME_A, WishlistDatabaseHelper.USER_COL0_A + " = " + wishlistID, null);
    }

    //WISHLIST DETAIL
    public int deleteProductInAllWishlist(final int productID){
        final SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WishlistDatabaseHelper.USER_TABLE_NAME_A, WishlistDatabaseHelper.USER_COL1_A + " = " + productID, null);
    }
    //WISHLIST DETAIL
    public int deleteProductInOneWishlist(final int productID, final int wishlistID){
        final SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WishlistDatabaseHelper.USER_TABLE_NAME_A, WishlistDatabaseHelper.USER_COL1_A + " = " + productID + " AND " + WishlistDatabaseHelper.USER_COL0_A + " = " + wishlistID, null);
    }
    //WISHLIST
    public int deleteWishlistWithProduct(final int wishlistID){
        this.deleteAllWishlistProduct(wishlistID);
        final SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WishlistDatabaseHelper.USER_TABLE_NAME_B, WishlistDatabaseHelper.USER_COL1_B + " = " + wishlistID, null);
    }
}