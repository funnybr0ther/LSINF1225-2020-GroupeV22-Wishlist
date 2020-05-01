package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.wishlist.Class.Purchase;

public class PurchaseDatabaseHelper extends SQLiteOpenHelper {

    private static final String PURCHASE_TABLE_NAME = "purchase";
    private static final String PURCHASE_COL0 = "numAcheteur";
    private static final String PURCHASE_COL1 = "numReceveur";
    private static final String PURCHASE_COL2 = "numProduit";
    private static final String PURCHASE_COL3 = "date";
    private static final String PURCHASE_COL4 = "quantité";
    private static final String PURCHASE_COL5 = "numAchat";
    private static final String DATABASE_NAME = "wishlit.db";

    public PurchaseDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlCommand = "CREATE TABLE "+
                PURCHASE_TABLE_NAME + " (" +
                PURCHASE_COL0 + " INTEGER NOT NULL REFERENCES utilisateur(userId), "+
                PURCHASE_COL1 + " INTEGER NOT NULL REFERENCES utilisateur(userId), " +
                PURCHASE_COL2 + " INTEGER NOT NULL REFERENCES produit (numProduit), " +
                PURCHASE_COL3 + " STRING NOT NULL, " +
                PURCHASE_COL4 + " INTEGER NOT NULL," +
                PURCHASE_COL5 + " INTEGER NOT NULL)";
        db.execSQL(sqlCommand);
    }

    public Boolean addPurchase(Purchase achat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(PURCHASE_COL0, achat.getSender());
        contentvalues.put(PURCHASE_COL1, achat.getReceiver());
        contentvalues.put(PURCHASE_COL2,achat.getProduct().getName());
        contentvalues.put(PURCHASE_COL3, achat.getDate().toString());
        contentvalues.put(PURCHASE_COL4, achat.getQuantity());
        contentvalues.put(PURCHASE_COL5, achat.getPurchaseId()); // A moins que la bdd génère elle même un numéro aléatoire ?
        long err = db.insert(PURCHASE_TABLE_NAME, null, contentvalues);
        return err != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
