package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FollowDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wishlist.db";
    private static final String FOLLOW_TABLE_NAME = "follows";
    private static final String FOLLOW_COL0 = "followerID";
    private static final String FOLLOW_COL1 = "followedID";
    private static final String FOLLOW_COL2 = "relation";

    public FollowDatabaseHelper (@Nullable final Context context){
        super(context, FollowDatabaseHelper.DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db){
        Log.d(TAG, "onCreate: ");
        final String sqlCommand = "CREATE TABLE "+
                FollowDatabaseHelper.FOLLOW_TABLE_NAME + " ( "+
                FollowDatabaseHelper.FOLLOW_COL0 + " INTEGER REFERENCES user (userID) NOT NULL, "+
                FollowDatabaseHelper.FOLLOW_COL1 + " NTEGER REFERENCES user (userID) NOT NULL, "+
                FollowDatabaseHelper.FOLLOW_COL2 + " TEXT NOT NULL )";
        db.execSQL(sqlCommand);
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        final String sqlCommand="CREATE TABLE IF NOT EXISTS "+
                FollowDatabaseHelper.FOLLOW_TABLE_NAME + " ( "+
                FollowDatabaseHelper.FOLLOW_COL0 + " INTEGER REFERENCES user (userID) NOT NULL, "+
                FollowDatabaseHelper.FOLLOW_COL1 + " INTEGER REFERENCES user (userID) NOT NULL, "+
                FollowDatabaseHelper.FOLLOW_COL2 + " TEXT NOT NULL )";
        db.execSQL(sqlCommand);
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        final String sqlCommand="DROP IF TABLE EXISTS " + FollowDatabaseHelper.FOLLOW_TABLE_NAME;
        this.onCreate(db);
    }

    public boolean addFollow(final int followerID, final int followedID, final String relation){
        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(FollowDatabaseHelper.FOLLOW_COL0,followerID);
        contentValues.put(FollowDatabaseHelper.FOLLOW_COL1,followedID);
        contentValues.put(FollowDatabaseHelper.FOLLOW_COL2,relation);
        final long err=db.insert(FollowDatabaseHelper.FOLLOW_TABLE_NAME,null,contentValues);
        return err!=-1;
    }

    public boolean checkIfFollows(final int followerID, final int followedID){
        final SQLiteDatabase db = this.getReadableDatabase();
        final String selection = FollowDatabaseHelper.FOLLOW_COL0 + " =? AND " + FollowDatabaseHelper.FOLLOW_COL1 + " =?";
        final String[] condition = {String.valueOf(followerID),String.valueOf(followedID)};
        final Cursor cursor = db.query(FollowDatabaseHelper.FOLLOW_TABLE_NAME,null,selection,condition,null,null,null);
        final boolean ret = cursor.getCount() != 0;
        cursor.close();
        return ret;
    }

    public void unfollow(final int followerID, final int followedID){
        final SQLiteDatabase db = this.getWritableDatabase();
        final String sqlCommand = "DELETE FROM " + FollowDatabaseHelper.FOLLOW_TABLE_NAME + " WHERE " + FollowDatabaseHelper.FOLLOW_COL1 + "= " + followedID + " AND " + FollowDatabaseHelper.FOLLOW_COL0 + "= " + followerID;
        db.execSQL(sqlCommand);
    }

    public ArrayList<Integer> getFollows(final int id){
        final SQLiteDatabase db = this.getReadableDatabase();
        final String[] condition = {String.valueOf(id)};
        final String[] projection = {FollowDatabaseHelper.FOLLOW_COL1};
        final String selection = FollowDatabaseHelper.FOLLOW_COL0 +" =?";

        final ArrayList<Integer> followList = new ArrayList<>();
        final Cursor cursor = db.query(FollowDatabaseHelper.FOLLOW_TABLE_NAME,null,selection,condition,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            followList.add(cursor.getInt(cursor.getColumnIndex(FollowDatabaseHelper.FOLLOW_COL1)));
            cursor.moveToNext();
        }
        cursor.close();
        return followList;

    }
}
