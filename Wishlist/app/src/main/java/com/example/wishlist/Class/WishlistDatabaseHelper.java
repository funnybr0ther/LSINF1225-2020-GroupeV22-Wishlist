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

    /**
     * Constructor for product database helper
     */
    public WishlistDatabaseHelper(@Nullable Context context) { super(context, DATABASE_NAME, null, 1); }

    /*
    Creation de 2 tables de données dans la même classe, afin de respecter notre choix de conception de la BDD,
    tout en facilitant les echanges fréquents entre les 2 tables.

    A : Table avec le contenu des wishlists

    B : Table avec la liste des wishlists
     */

    /**
     * Creation of the product database table
     * @param db the database to be written to
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand_A="CREATE TABLE "+
                USER_TABLE_NAME_A + " ("+
                USER_COL0_A + " INTEGER REFERENCES wishlist (\"wishlistID\") NOT NULL, "+
                USER_COL1_A + " INTEGER REFERENCES product (\"productReference\") NOT NULL)";
        db.execSQL(sqlCommand_A);

        String sqlCommand_B="CREATE TABLE "+
                USER_TABLE_NAME_B + " ("+
                USER_COL0_B + " INTEGER NOT NULL REFERENCES user (\"userID\"), "+
                USER_COL1_B + " INTEGER NOT NULL , "+
                USER_COL2_B + " TEXT NOT NULL)";
        db.execSQL(sqlCommand_B);
    }

    /**
     * Used to create the add the table if it doesn't exist yet in the table
     * @param db the database to add the table to
     */
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

    /**
     Drop table to create new "upgraded" table format, not rly used
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand="DROP IF TABLE EXISTS " + DATABASE_NAME;
        onCreate(db);
    }

    //WISHLIST DETAIL
    /**
     * Get all the product in a wishlist with her ID
     * @param wishlistID unique id of the wishlist
     * @return an array of integer with the product reference in the wishlist
     */
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
    /**
     * change the name of a wishlist
     * @param wishlistID unique id of the wishlist
     * @param newName new name of the wishlist id
     * @param userID id of the user which owne the wishlist
     * @return true if the name was changed and false if the name wasn't changed
     */
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

    /**
     * add a product in the wishlist.
     * @param productID id of the new product
     * @param wishlistID id of the wishlist in which you add the product
     * @return true if the product was add to the wishlist and fasle if the product wasn't add
     */
    public boolean addProduct(int productID, int wishlistID){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL0_A,wishlistID);
        contentValues.put(USER_COL1_A,productID);
        long err=db.insert(USER_TABLE_NAME_A,null,contentValues);
        return err!=-1;
    }

    //WISHLIST

    /**
     * add a wishlist to a user.
     * @param name name of the new wishlist
     * @param userID id of the new wishlist owner
     * @return true if the wishlist was add, false otherwise
     */
    public boolean addWishlist(String name, int userID){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL0_B,userID);
        contentValues.put(USER_COL2_B,name);
        long err=db.insert(USER_TABLE_NAME_B,null,contentValues);
        return err!=-1;
    }

    //WISHLIST

    /**
     * Get all the wishlist of a user
      * @param userID user for which we want the wishlist
     * @return Array with all the wishlist of the user
     */
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

    /**
     * delete all the product in a wishlist
     * @param wishlistID id a the wishlist
     * @return true if the products was deleted, false otherwise
     */
    public int deleteAllWishlistProduct(int wishlistID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL0_A + " = " + wishlistID, null);
    }

    //WISHLIST DETAIL

    /**
     * delete a product in all wishlist
      * @param productID id of the product we want to delete
     * @return true if the products was deleted, false otherwise
     */
    public int deleteProductInAllWishlist(int productID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL1_A + " = " + productID, null);
    }
    //WISHLIST DETAIL

    /**
     * depreciated
     * delete a product in one wishlist
     * @param productID id of the product to delete
     * @param wishlistID id of the wishlist in which we want delete the product
     * @return true if the products was deleted, false otherwise
     */
    public int deleteProductInOneWishlist(int productID, int wishlistID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_A, USER_COL1_A + " = " + productID + " AND " + USER_COL0_A + " = " + wishlistID, null);
    }
    //WISHLIST

    /**
     * delete a wishlist with all it's product
     * @param wishlistID id of the wishlist to delete
     * @return true if the wishlist was deleted, false otherwise
     */
    public int deleteWishlistWithProduct(int wishlistID){
        deleteAllWishlistProduct(wishlistID);
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME_B, USER_COL1_B + " = " + wishlistID, null);
    }
}