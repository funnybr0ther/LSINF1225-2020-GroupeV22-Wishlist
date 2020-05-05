package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FollowDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wishlist.db";
    private static final String FOLLOW_TABLE_NAME = "follows";
    private static final String FOLLOW_COL0 = "followerID";
    private static final String FOLLOW_COL1 = "followedID";
    private static final String FOLLOW_COL2 = "relation";

    public FollowDatabaseHelper (@Nullable Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlCommand = "CREATE TABLE "+
                FOLLOW_TABLE_NAME + " ("+
                FOLLOW_COL0 + "INTEGER REFERENCES user (userID) NOT NULL, "+
                FOLLOW_COL1 + "INTEGER REFERENCES user (userID) NOT NULL, "+
                FOLLOW_COL2 + "TEXT NOT NULL )";
        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand="DROP IF TABLE EXISTS " +FOLLOW_TABLE_NAME;
        onCreate(db);
    }

    public boolean addFollow(int followerID, int followedID, String relation){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOLLOW_COL0,followerID);
        contentValues.put(FOLLOW_COL1,followedID);
        contentValues.put(FOLLOW_COL2,relation);
        long err=db.insert(FOLLOW_TABLE_NAME,null,contentValues);
        return err!=-1;
    }
}
