package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

/**
 * A helper class to manage products in the database. It allows the creation of the table, as well
 * as the creation, edition and deletion of products in the table.
 */
public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="wishlist.db";
    private static final String PRODUCT_TABLE_NAME ="product";
    private static final String PRODUCT_COL0 ="productReference";
    private static final String PRODUCT_COL1 ="name";
    private static final String PRODUCT_COL2 ="price";
    private static final String PRODUCT_COL3 ="category";
    private static final String PRODUCT_COL4 ="image";
    private static final String PRODUCT_COL5 ="weight";
    private static final String PRODUCT_COL6 ="dimensions";
    private static final String PRODUCT_COL7 ="description";
    private static final String PRODUCT_COL8 ="desire";
    private static final String PRODUCT_COL9 ="amount";
    private static final String PRODUCT_COL10 ="purchased";

    public ProductDatabaseHelper(@Nullable Context context) {
        /**
         * Constructor for product database helper
         */
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Creation of the product database table
     * @param db the database to be written to
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate: ");
        String sqlCommand="CREATE TABLE "+
                PRODUCT_TABLE_NAME + " ("+
                PRODUCT_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                PRODUCT_COL1 + " TEXT NOT NULL, "+
                PRODUCT_COL2 + " INTEGER NOT NULL, "+
                PRODUCT_COL3 + " TEXT, "+
                PRODUCT_COL4 + " BLOB, "+
                PRODUCT_COL5 + " INTEGER, "+
                PRODUCT_COL6 + " STRING, "+
                PRODUCT_COL7 + " TEXT," +
                PRODUCT_COL8 + " INTEGER, "+
                PRODUCT_COL9 + " INTEGER, "+
                PRODUCT_COL10 + " INTEGER)";
        db.execSQL(sqlCommand);
    }

    /**
     * Used to create the add the table if it doesn't exist yet in the table
     * @param db the database to add the table to
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        String sqlCommand="CREATE TABLE IF NOT EXISTS "+
                PRODUCT_TABLE_NAME + " ("+
                PRODUCT_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                PRODUCT_COL1 + " TEXT NOT NULL, "+
                PRODUCT_COL2 + " INTEGER NOT NULL, "+
                PRODUCT_COL3 + " TEXT, "+
                PRODUCT_COL4 + " BLOB, "+
                PRODUCT_COL5 + " INTEGER, "+
                PRODUCT_COL6 + " STRING, "+
                PRODUCT_COL7 + " TEXT," +
                PRODUCT_COL8 + " INTEGER, "+
                PRODUCT_COL9 + " INTEGER, "+
                PRODUCT_COL10 + " INTEGER)";
        db.execSQL(sqlCommand);
        super.onOpen(db);
    }

    /**
     Drop table to create new "upgraded" table format, not rly used
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlCommand="DROP IF TABLE EXISTS " + PRODUCT_TABLE_NAME;
        onCreate(db);
    }
    /**
     Add Product product to the database
     @param product the product to be added to the database
     @return the productID of the newly added line
     */
    public int addProduct(Product product){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL1,product.getName());
        contentValues.put(PRODUCT_COL2,product.getPrice());
        contentValues.put(PRODUCT_COL3, convertArrayToString(product.getCategory()));
        contentValues.put(PRODUCT_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(PRODUCT_COL5,product.getWeight());
        contentValues.put(PRODUCT_COL6,product.getDimensions());
        contentValues.put(PRODUCT_COL7,product.getDescription());
        contentValues.put(PRODUCT_COL8,product.getDesire());
        contentValues.put(PRODUCT_COL9,product.getTotal());
        contentValues.put(PRODUCT_COL10,product.getPurchased());
        int err= (int) db.insert(PRODUCT_TABLE_NAME,null,contentValues);
        return err;
    }

    /**
     * Retrieves product with ID productID. If productID isn't a valid productID or there is
     * no matching line in the database, returns null.
     * @param productID the productID of the producted that must be returned
     * @return The Product object that matches productID, null if productID isn't valid
     */
    public Product getProductFromID(long productID){

        SQLiteDatabase db= getReadableDatabase();
        String[] condition = {String.valueOf(productID)};
        String selection = PRODUCT_COL0 +" =?";
        Cursor cursor=db.query(PRODUCT_TABLE_NAME,null,selection,condition,null,null,null);
        if(cursor.getCount() == -1){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(PRODUCT_COL1));
        Integer price = cursor.getInt(cursor.getColumnIndex(PRODUCT_COL2));
        String[] categories = convertStringToArray(cursor.getString(cursor.getColumnIndex(PRODUCT_COL3)));
        Bitmap picture = ImageHelper.getImage(cursor.getBlob(cursor.getColumnIndex(PRODUCT_COL4)));
        Integer weight = cursor.getInt(cursor.getColumnIndex(PRODUCT_COL5));
        String dimensions = cursor.getString(cursor.getColumnIndex(PRODUCT_COL6));
        String description = cursor.getString(cursor.getColumnIndex(PRODUCT_COL7));
        Integer desire = cursor.getInt(cursor.getColumnIndex(PRODUCT_COL8));
        Integer amount = cursor.getInt(cursor.getColumnIndex(PRODUCT_COL9));
        Integer purchased = cursor.getInt(cursor.getColumnIndex(PRODUCT_COL10));
        return new Product(name,picture,description,categories,weight,price,desire,dimensions, amount,purchased);
    }

    /**
     * Updates the columns of the first line matching productID with the fields of
     * Product product. Return true if the operation succeeded, false otherwise.
     * @param product the Product object that contains the fields to be overwritten in the database
     * @param productID the productID of the line that must be overwritten
     */
    public boolean updateProduct(Product product, int productID){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL1,product.getName());
        contentValues.put(PRODUCT_COL2,product.getPrice());
        contentValues.put(PRODUCT_COL3, convertArrayToString(product.getCategory()));
        contentValues.put(PRODUCT_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(PRODUCT_COL5,product.getWeight());
        contentValues.put(PRODUCT_COL6,product.getDimensions());
        contentValues.put(PRODUCT_COL7,product.getDescription());
        contentValues.put(PRODUCT_COL8,product.getDesire());
        contentValues.put(PRODUCT_COL9,product.getTotal());
        contentValues.put(PRODUCT_COL10,product.getPurchased());
        int err=db.update(PRODUCT_TABLE_NAME,contentValues, PRODUCT_COL0 +" = ?",new String[]{String.valueOf(productID)});
        return err!=-1;
    }

    /**
     * Not used bc history keeping
     * @param productID the productID of the line that must be deleted
     */
    public void deleteProduct(int productID){
        SQLiteDatabase db = getWritableDatabase();
        String sqlCommand = "DELETE FROM " + PRODUCT_TABLE_NAME + " WHERE " + PRODUCT_COL0 + "=" + productID;
        db.execSQL(sqlCommand);
    }
    /*
        Conversion of String[] to String using a comma "," as a separator
     */
    public static final String strSeparator = "__,__";

    /**
     * Allows a String array to be converted to a string using a standard separator __,__
     * @param array the String array to be converted
     * @return the String object that corresponds to array
     */
    public static String convertArrayToString(String[] array){
        /**
         * String[] -> String
         */
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+ strSeparator;
            }
        }
        return str;
    }

    /**
     * Does the opposite of convertArrayToString: re-converts a given string to a String array
     * @param str a valid String, to be separated in a String array
     * @return the String array corresponding to str
     */
    public static String[] convertStringToArray(String str){
        /**
         * String -> String[]
         */
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
