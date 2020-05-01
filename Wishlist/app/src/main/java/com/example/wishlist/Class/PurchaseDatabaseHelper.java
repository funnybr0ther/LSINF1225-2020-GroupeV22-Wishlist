package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.wishlist.Class.Purchase;

public class PurchaseDatabaseHelper extends SQLiteOpenHelper {

    private static final String PURCHASE_TABLE_NAME = "purchase";
    private static final String PURCHASE_COL0 = "numAchat";
    private static final String PURCHASE_COL1 = "numAcheteur";
    private static final String PURCHASE_COL2 = "numReceveur";
    private static final String PURCHASE_COL3 = "Produit";
    private static final String PURCHASE_COL4 = "quantité";
    private static final String PURCHASE_COL5 = "date";
    private static final String DATABASE_NAME = "wishlit.db";

    public PurchaseDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){    // Faut pas avoir une KEY pour la db ??
        String sqlCommand = "CREATE TABLE "+
                PURCHASE_TABLE_NAME + " (" +
                PURCHASE_COL0 + " INTEGER NOT NULL, " +
                PURCHASE_COL1 + " INTEGER NOT NULL REFERENCES utilisateur(userId), " +
                PURCHASE_COL2 + " INTEGER NOT NULL REFERENCES utilisateur(userId), " +
                PURCHASE_COL3 + " INTEGER NOT NULL REFERENCES produit (numProduit), " +
                PURCHASE_COL4 + " INTEGER NOT NULL," +
                PURCHASE_COL5 + " STRING NOT NULL )";
        db.execSQL(sqlCommand);
    }

    public Boolean addPurchase(Purchase achat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(PURCHASE_COL0, achat.getPurchaseId());    // Purchase ID
        contentvalues.put(PURCHASE_COL1, achat.getSender());    // Acheteur
        contentvalues.put(PURCHASE_COL2, achat.getReceiver());  //Receveur
        contentvalues.put(PURCHASE_COL3,achat.getProductID());    // Produit
        contentvalues.put(PURCHASE_COL4, achat.getQuantity());   // Quantité
        contentvalues.put(PURCHASE_COL5, achat.getDate().toString());  // Date
        long err = db.insert(PURCHASE_TABLE_NAME, null, contentvalues);
        return err != -1;
    }

    public Cursor getAllPurchases(int UserID){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] condition = {String.valueOf(UserID)};
        String selection = PURCHASE_COL1 +" =?";
        Cursor cursor = db.query(PURCHASE_TABLE_NAME,null,selection,condition,null,null,null);
        if(cursor.getCount() == -1){
            cursor.close();
            return null;
        }
        return cursor;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASE_TABLE_NAME);
        onCreate(db);
    }
}