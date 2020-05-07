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
    public static final String PRODUCT_COL0 ="productReference";
    public static final String PRODUCT_COL1 ="name";
    public static final String PRODUCT_COL2 ="price";
    public static final String PRODUCT_COL3 ="category";
    public static final String PRODUCT_COL4 ="image";
    public static final String PRODUCT_COL5 ="weight";
    public static final String PRODUCT_COL6 ="dimensions";
    public static final String PRODUCT_COL7 ="description";
    public static final String PRODUCT_COL8 ="desire";
    public static final String PRODUCT_COL9 ="amount";
    public static final String PRODUCT_COL10 ="purchased";

    public ProductDatabaseHelper(@Nullable final Context context) {
        /**
         * Constructor for product database helper
         */
        super(context, ProductDatabaseHelper.DATABASE_NAME, null, 1);
    }

    /**
     * Creation of the product database table
     * @param db the database to be written to
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {

        Log.d(TAG, "onCreate: ");
        final String sqlCommand="CREATE TABLE "+
                ProductDatabaseHelper.PRODUCT_TABLE_NAME + " ("+
                ProductDatabaseHelper.PRODUCT_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                ProductDatabaseHelper.PRODUCT_COL1 + " TEXT NOT NULL, "+
                ProductDatabaseHelper.PRODUCT_COL2 + " INTEGER NOT NULL, "+
                ProductDatabaseHelper.PRODUCT_COL3 + " TEXT, "+
                ProductDatabaseHelper.PRODUCT_COL4 + " BLOB, "+
                ProductDatabaseHelper.PRODUCT_COL5 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL6 + " STRING, "+
                ProductDatabaseHelper.PRODUCT_COL7 + " TEXT," +
                ProductDatabaseHelper.PRODUCT_COL8 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL9 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL10 + " INTEGER)";
        db.execSQL(sqlCommand);
    }

    /**
     * Used to create the add the table if it doesn't exist yet in the table
     * @param db the database to add the table to
     */
    @Override
    public void onOpen(final SQLiteDatabase db) {
        final String sqlCommand="CREATE TABLE IF NOT EXISTS "+
                ProductDatabaseHelper.PRODUCT_TABLE_NAME + " ("+
                ProductDatabaseHelper.PRODUCT_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                ProductDatabaseHelper.PRODUCT_COL1 + " TEXT NOT NULL, "+
                ProductDatabaseHelper.PRODUCT_COL2 + " INTEGER NOT NULL, "+
                ProductDatabaseHelper.PRODUCT_COL3 + " TEXT, "+
                ProductDatabaseHelper.PRODUCT_COL4 + " BLOB, "+
                ProductDatabaseHelper.PRODUCT_COL5 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL6 + " STRING, "+
                ProductDatabaseHelper.PRODUCT_COL7 + " TEXT," +
                ProductDatabaseHelper.PRODUCT_COL8 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL9 + " INTEGER, "+
                ProductDatabaseHelper.PRODUCT_COL10 + " INTEGER)";
        db.execSQL(sqlCommand);
        super.onOpen(db);
    }

    /**
     Drop table to create new "upgraded" table format, not rly used
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        final String sqlCommand="DROP IF TABLE EXISTS " + ProductDatabaseHelper.PRODUCT_TABLE_NAME;
        this.onCreate(db);
    }
    /**
     Add Product product to the database
     @param product the product to be added to the database
     @return the productID of the newly added line
     */
    public int addProduct(final Product product){

        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL1,product.getName());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL2,product.getPrice());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL3, ProductDatabaseHelper.convertArrayToString(product.getCategory()));
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL5,product.getWeight());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL6,product.getDimensions());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL7,product.getDescription());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL8,product.getDesire());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL9,product.getTotal());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL10,product.getPurchased());
        final int err= (int) db.insert(ProductDatabaseHelper.PRODUCT_TABLE_NAME,null,contentValues);
        return err;
    }

    /**
     * Retrieves product with ID productID. If productID isn't a valid productID or there is
     * no matching line in the database, returns null.
     * @param productID the productID of the producted that must be returned
     * @return The Product object that matches productID, null if productID isn't valid
     */
    public Product getProductFromID(final long productID){

        final SQLiteDatabase db= this.getReadableDatabase();
        final String[] condition = {String.valueOf(productID)};
        final String selection = ProductDatabaseHelper.PRODUCT_COL0 +" =?";
        final Cursor cursor=db.query(ProductDatabaseHelper.PRODUCT_TABLE_NAME,null,selection,condition,null,null,null);
        if(cursor.getCount() == -1){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        final String name = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL1));
        final Integer price = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL2));
        final String[] categories = ProductDatabaseHelper.convertStringToArray(cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL3)));
        final Bitmap picture = ImageHelper.getImage(cursor.getBlob(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL4)));
        final Integer weight = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL5));
        final String dimensions = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL6));
        final String description = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL7));
        final Integer desire = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL8));
        final Integer amount = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL9));
        final Integer purchased = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.PRODUCT_COL10));
        return new Product(name,picture,description,categories,weight,price,desire,dimensions, amount,purchased);
    }

    /**
     * Updates the columns of the first line matching productID with the fields of
     * Product product. Return true if the operation succeeded, false otherwise.
     * @param product the Product object that contains the fields to be overwritten in the database
     * @param productID the productID of the line that must be overwritten
     */
    public boolean updateProduct(final Product product, final int productID){

        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL1,product.getName());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL2,product.getPrice());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL3, ProductDatabaseHelper.convertArrayToString(product.getCategory()));
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL5,product.getWeight());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL6,product.getDimensions());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL7,product.getDescription());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL8,product.getDesire());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL9,product.getTotal());
        contentValues.put(ProductDatabaseHelper.PRODUCT_COL10,product.getPurchased());
        final int err=db.update(ProductDatabaseHelper.PRODUCT_TABLE_NAME,contentValues, ProductDatabaseHelper.PRODUCT_COL0 +" = ?",new String[]{String.valueOf(productID)});
        return err!=-1;
    }

    /**
     * Not used bc history keeping
     * @param productID the productID of the line that must be deleted
     */
    public void deleteProduct(final int productID){
        final SQLiteDatabase db = this.getWritableDatabase();
        final String sqlCommand = "DELETE FROM " + ProductDatabaseHelper.PRODUCT_TABLE_NAME + " WHERE " + ProductDatabaseHelper.PRODUCT_COL0 + "=" + productID;
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
    public static String convertArrayToString(final String[] array){
        /**
         * String[] -> String
         */
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+ ProductDatabaseHelper.strSeparator;
            }
        }
        return str;
    }

    /**
     * Does the opposite of convertArrayToString: re-converts a given string to a String array
     * @param str a valid String, to be separated in a String array
     * @return the String array corresponding to str
     */
    public static String[] convertStringToArray(final String str){
        /**
         * String -> String[]
         */
        final String[] arr = str.split(ProductDatabaseHelper.strSeparator);
        return arr;
    }
}
