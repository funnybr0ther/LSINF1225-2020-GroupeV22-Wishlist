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

    public ProductDatabaseHelper(@Nullable Context context) {
        /**
         * Constructor for product database helper
         */
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Creation of the product database table
         */
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
            Drop table to create new "upgraded" table format
         */
        String sqlCommand="DROP IF TABLE EXISTS " + PRODUCT_TABLE_NAME;
        onCreate(db);
    }

    public int addProduct(Product product){
        /**
            Add Product product to the database
         */
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL1,product.getName());
        contentValues.put(PRODUCT_COL2,product.getPrice());
        contentValues.put(PRODUCT_COL3,convertArrayToString(product.getCategory()));
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

    public Product getProductFromID(long productID){
        /**
         * Retrieves product with ID productID. If productID isn't a valid productID or there is
         * no matching line in the database, returns null.
         */
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

    public boolean updateProduct(Product product, int productID){
        /**
         * Updates the columns of the first line matching productID with the fields of
         * Product product. Return true if the operation succeeded, false otherwise.
         */
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL1,product.getName());
        contentValues.put(PRODUCT_COL2,product.getPrice());
        contentValues.put(PRODUCT_COL3,convertArrayToString(product.getCategory()));
        contentValues.put(PRODUCT_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(PRODUCT_COL5,product.getWeight());
        contentValues.put(PRODUCT_COL6,product.getDimensions());
        contentValues.put(PRODUCT_COL7,product.getDescription());
        contentValues.put(PRODUCT_COL8,product.getDesire());
        contentValues.put(PRODUCT_COL9,product.getTotal());
        contentValues.put(PRODUCT_COL10,product.getPurchased());
        int err=db.update(PRODUCT_TABLE_NAME,contentValues,PRODUCT_COL0+" = ?",new String[]{String.valueOf(productID)});
        return err!=-1;
    }

    public void deleteProduct(int productID){
        SQLiteDatabase db = getWritableDatabase();
        String sqlCommand = "DELETE FROM " + PRODUCT_TABLE_NAME + " WHERE " + PRODUCT_COL0+ "=" + productID;
        db.execSQL(sqlCommand);
    }
    /*
        Conversion of String[] to String using a comma "," as a separator
     */
    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
        /**
         * String[] -> String
         */
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToArray(String str){
        /**
         * String -> String[]
         */
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
