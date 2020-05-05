package com.example.wishlist.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="wishlist.db";
    private static final String USER_TABLE_NAME="user";
    public static final String USER_COL0="userID";
    public static final String USER_COL1="mail";
    public static final String USER_COL2="password";
    public static final String USER_COL3="firstName";
    public static final String USER_COL4="lastName";
    public static final String USER_COL5="address";
    public static final String USER_COL6="birthDate";
    public static final String USER_COL7="size";
    public static final String USER_COL8="shoeSize";
    public static final String USER_COL9="favoriteColor";
    public static final String USER_COL10="profilePhoto";
    public static final String USER_COL11="notification";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand="CREATE TABLE "+
                USER_TABLE_NAME + " ("+
                USER_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                USER_COL1 + " TEXT NOT NULL UNIQUE, "+
                USER_COL2 + " VARCHAR(255) NOT NULL, "+
                USER_COL3 + " TEXT NOT NULL, "+
                USER_COL4 + " TEXT NOT NULL, "+
                USER_COL5 + " TEXT NOT NULL, "+
                USER_COL6 + " DATE NOT NULL, "+
                USER_COL7 + " TEXT,"+
                USER_COL8 + " TEXT, "+
                USER_COL9 + " TEXT, "+
                USER_COL10 + " BLOB, "+
                USER_COL11 + " TEXT)";
        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand="DROP IF TABLE EXISTS " +USER_TABLE_NAME;
        onCreate(db);
    }

    //Add user in db return false if something went wrong
    public boolean addUser(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL1,user.getEmail());
        contentValues.put(USER_COL2,user.getPassword());
        contentValues.put(USER_COL3,user.getFirstName());
        contentValues.put(USER_COL4,user.getLastName());
        contentValues.put(USER_COL5,user.getAddress().toString());
        contentValues.put(USER_COL6,user.getBirthDate().toString());
        contentValues.put(USER_COL7,user.getSize());
        contentValues.put(USER_COL8,user.getShoeSize());
        contentValues.put(USER_COL9,user.getFavoriteColor());
        if(user.getProfilePhoto()!=null){
            contentValues.put(USER_COL10,ImageHelper.getBytes(user.getProfilePhoto()));
        }
        contentValues.put(USER_COL11,user.isNotification());
        long err=db.insert(USER_TABLE_NAME,null,contentValues);
        return err!=-1;
    }

    //Check if any user is register with this mail and this password
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

    //Check if mail isn't used by another user
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

    public User getUserFromID(int userID){
        SQLiteDatabase db=getReadableDatabase();
        String[] projection={USER_COL0,USER_COL1,USER_COL2};
        String[] condition ={String.valueOf(userID)};
        String selection=USER_COL0+" =?";
        Cursor cursor=db.query(USER_TABLE_NAME,null,selection,condition,null,null,null);
        if (cursor.getCount()==0){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String addressString=cursor.getString(cursor.getColumnIndex(USER_COL5));
        Address address=Address.fromString(addressString);
        String firstName=cursor.getString(cursor.getColumnIndex(USER_COL3));
        String lastName=cursor.getString(cursor.getColumnIndex(USER_COL4));
        String email=cursor.getString(cursor.getColumnIndex(USER_COL1));
        DateWish birthDate= new DateWish();
        birthDate.setDate(cursor.getString(cursor.getColumnIndex(USER_COL6)));
        String password=cursor.getString(cursor.getColumnIndex(USER_COL2));
        byte[] profilePhoto=cursor.getBlob(cursor.getColumnIndex(USER_COL10));
        boolean notification=true;
        String favoriteColor=cursor.getString(cursor.getColumnIndex(USER_COL9));
        String size=cursor.getString(cursor.getColumnIndex(USER_COL7));
        String shoeSize=cursor.getString(cursor.getColumnIndex(USER_COL8));
        return new User(address,firstName,lastName,email,birthDate,password,ImageHelper.getImage(profilePhoto),favoriteColor,size,shoeSize);
    }
    public ArrayList<User> getAllUser(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+USER_TABLE_NAME,null);
        ArrayList<User> users=new ArrayList<User>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String addressString=cursor.getString(cursor.getColumnIndex(USER_COL5));
            Address address=Address.fromString(addressString);
            String firstName=cursor.getString(cursor.getColumnIndex(USER_COL3));
            String lastName=cursor.getString(cursor.getColumnIndex(USER_COL4));
            String email=cursor.getString(cursor.getColumnIndex(USER_COL1));
            DateWish birthDate= new DateWish();
            birthDate.setDate(cursor.getString(cursor.getColumnIndex(USER_COL6)));
            String password=cursor.getString(cursor.getColumnIndex(USER_COL2));
            byte[] profilePhoto=cursor.getBlob(cursor.getColumnIndex(USER_COL10));
            boolean notification=true;
            String favoriteColor=cursor.getString(cursor.getColumnIndex(USER_COL9));
            String size=cursor.getString(cursor.getColumnIndex(USER_COL7));
            String shoeSize=cursor.getString(cursor.getColumnIndex(USER_COL8));
            users.add(new User(address,firstName,lastName,email,birthDate,password,ImageHelper.getImage(profilePhoto),favoriteColor,size,shoeSize));
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public boolean updateUser(User user, int userID){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL1,user.getEmail());
        contentValues.put(USER_COL2,user.getPassword());
        contentValues.put(USER_COL3,user.getFirstName());
        contentValues.put(USER_COL4,user.getLastName());
        contentValues.put(USER_COL5,user.getAddress().toString());
        contentValues.put(USER_COL6,user.getBirthDate().toString());
        contentValues.put(USER_COL7,user.getSize());
        contentValues.put(USER_COL8,user.getShoeSize());
        contentValues.put(USER_COL9,user.getFavoriteColor());
        if(user.getProfilePhoto()!=null){
            contentValues.put(USER_COL10,ImageHelper.getBytes(user.getProfilePhoto()));
        }
        contentValues.put(USER_COL11,user.isNotification());
        int err=db.update(USER_TABLE_NAME,contentValues,USER_COL0+" = ?",new String[]{String.valueOf(userID)});
        return err!=-1;
    }
    //Check if the couple password UserID is present in the db
    public boolean checkPassword(int userID, String password){
        SQLiteDatabase db=getReadableDatabase();
        String[] condition ={String.valueOf(userID),password};
        String selection=USER_COL0+"=? AND "+USER_COL2+" =?";
        Cursor cursor=db.query(USER_TABLE_NAME,null,selection,condition,null,null,null);
        boolean sol=cursor.getCount()==1;
        cursor.close();
        return sol;
    }
    //return number of row delete (here max 1 as userID is a primary key)
    public int delete(int userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(USER_TABLE_NAME,USER_COL0+"=?",new String[]{String.valueOf(userID)});
    }
}
