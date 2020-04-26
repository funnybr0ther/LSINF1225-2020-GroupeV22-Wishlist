package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="wishlist.db";
    private static final String USER_TABLE_NAME="product";
    public static final String USER_COL0="productReference";
    public static final String USER_COL1="name";
    public static final String USER_COL2="price";
    public static final String USER_COL3="category";
    public static final String USER_COL4="image";
    public static final String USER_COL5="weight";
    public static final String USER_COL6="dimensions";
    public static final String USER_COL7="description";

    public ProductDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand="CREATE TABLE "+
                USER_TABLE_NAME + " ("+
                USER_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                USER_COL1 + " TEXT NOT NULL, "+
                USER_COL2 + " INTEGER NOT NULL, "+
                USER_COL3 + " TEXT, "+
                USER_COL4 + " BLOB, "+
                USER_COL5 + " INTEGER, "+
                USER_COL6 + " STRING, "+
                USER_COL7 + " TEXT)";
        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand="DROP IF TABLE EXISTS " +USER_TABLE_NAME;
        onCreate(db);
    }

    public boolean addProduct(Product product){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL1,product.getName());
        contentValues.put(USER_COL2,product.getPrice());
        contentValues.put(USER_COL3,convertArrayToString(product.getCategory()));
        contentValues.put(USER_COL4,ImageHelper.getBytes(product.getPhoto()));
        contentValues.put(USER_COL5,product.getWeight());
        contentValues.put(USER_COL6,product.getDimensions());
        contentValues.put(USER_COL7,product.getDescription());
        long err=db.insert(USER_TABLE_NAME,null,contentValues);
        return err!=-1;
    }

    public int checkUser(String mail, String password){
        SQLiteDatabase db=getReadableDatabase();
        String[] projection={USER_COL0,USER_COL1,USER_COL2};
        String[] condition ={mail,password};
        String selection=USER_COL1+"=? AND "+USER_COL2+" =?";
        Cursor cursor=db.query(USER_TABLE_NAME,projection,selection,condition,null,null,null);

        if (cursor.getCount()==0){
            cursor.close();
            return -1;
        }
        if(cursor.moveToFirst()) {
            int userID=cursor.getInt(cursor.getColumnIndex(USER_COL0));
            cursor.close();
            return userID;
        }
        else {
            cursor.close();
            return -1;
        }
    }

    public boolean checkMail(String mail){
        SQLiteDatabase db=getReadableDatabase();
        String[] projection={USER_COL0,USER_COL1,USER_COL2};
        String[] condition ={mail};
        String selection=USER_COL1+" =?";
        Cursor cursor=db.query(USER_TABLE_NAME,projection,selection,condition,null,null,null);
        boolean sol=cursor.getCount()==0;
        cursor.close();
        return sol;
    }

    public static String strSeparator = "__,__";
    public static String convertArrayToString(String[] array){
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
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
