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

    public UserDatabaseHelper(@Nullable final Context context) {
        super(context, UserDatabaseHelper.DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String sqlCommand="CREATE TABLE "+
                UserDatabaseHelper.USER_TABLE_NAME + " ("+
                UserDatabaseHelper.USER_COL0 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "+
                UserDatabaseHelper.USER_COL1 + " TEXT NOT NULL UNIQUE, "+
                UserDatabaseHelper.USER_COL2 + " VARCHAR(255) NOT NULL, "+
                UserDatabaseHelper.USER_COL3 + " TEXT NOT NULL, "+
                UserDatabaseHelper.USER_COL4 + " TEXT NOT NULL, "+
                UserDatabaseHelper.USER_COL5 + " TEXT NOT NULL, "+
                UserDatabaseHelper.USER_COL6 + " DATE NOT NULL, "+
                UserDatabaseHelper.USER_COL7 + " TEXT,"+
                UserDatabaseHelper.USER_COL8 + " TEXT, "+
                UserDatabaseHelper.USER_COL9 + " TEXT, "+
                UserDatabaseHelper.USER_COL10 + " BLOB, "+
                UserDatabaseHelper.USER_COL11 + " TEXT)";
        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        final String sqlCommand="DROP IF TABLE EXISTS " + UserDatabaseHelper.USER_TABLE_NAME;
        this.onCreate(db);
    }

    //Add user in db return false if something went wrong
    public boolean addUser(final User user){
        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(UserDatabaseHelper.USER_COL1,user.getEmail());
        contentValues.put(UserDatabaseHelper.USER_COL2,user.getPassword());
        contentValues.put(UserDatabaseHelper.USER_COL3,user.getFirstName());
        contentValues.put(UserDatabaseHelper.USER_COL4,user.getLastName());
        contentValues.put(UserDatabaseHelper.USER_COL5,user.getAddress().toString());
        contentValues.put(UserDatabaseHelper.USER_COL6,user.getBirthDate().toString());
        contentValues.put(UserDatabaseHelper.USER_COL7,user.getSize());
        contentValues.put(UserDatabaseHelper.USER_COL8,user.getShoeSize());
        contentValues.put(UserDatabaseHelper.USER_COL9,user.getFavoriteColor());
        if(user.getProfilePhoto()!=null){
            contentValues.put(UserDatabaseHelper.USER_COL10,ImageHelper.getBytes(user.getProfilePhoto()));
        }
        contentValues.put(UserDatabaseHelper.USER_COL11,user.isNotification());
        final long err=db.insert(UserDatabaseHelper.USER_TABLE_NAME,null,contentValues);
        return err!=-1;
    }

    //Check if any user is register with this mail and this password
    public int checkUser(final String mail, final String password){
        final SQLiteDatabase db= this.getReadableDatabase();
        final String[] projection={UserDatabaseHelper.USER_COL0, UserDatabaseHelper.USER_COL1, UserDatabaseHelper.USER_COL2};
        final String[] condition ={mail,password};
        final String selection= UserDatabaseHelper.USER_COL1 +"=? AND "+ UserDatabaseHelper.USER_COL2 +" =?";
        final Cursor cursor=db.query(UserDatabaseHelper.USER_TABLE_NAME,projection,selection,condition,null,null,null);

        if (cursor.getCount()==0){
            cursor.close();
            return -1;
        }
        if(cursor.moveToFirst()) {
            final int userID=cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.USER_COL0));
            cursor.close();
            return userID;
        }
        else {
            cursor.close();
            return -1;
        }
    }

    //Check if mail isn't used by another user
    public boolean checkMail(final String mail){
        final SQLiteDatabase db= this.getReadableDatabase();
        final String[] projection={UserDatabaseHelper.USER_COL0, UserDatabaseHelper.USER_COL1, UserDatabaseHelper.USER_COL2};
        final String[] condition ={mail};
        final String selection= UserDatabaseHelper.USER_COL1 +" =?";
        final Cursor cursor=db.query(UserDatabaseHelper.USER_TABLE_NAME,projection,selection,condition,null,null,null);
        final boolean sol=cursor.getCount()==0;
        cursor.close();
        return sol;
    }

    public User getUserFromID(final int userID){
        final SQLiteDatabase db= this.getReadableDatabase();
        final String[] condition ={String.valueOf(userID)};
        final String selection= UserDatabaseHelper.USER_COL0 +" =?";
        final Cursor cursor=db.query(UserDatabaseHelper.USER_TABLE_NAME,null,selection,condition,null,null,null);
        if (cursor.getCount()==0){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        final String addressString=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL5));
        final Address address=Address.fromString(addressString);
        final String firstName=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL3));
        final String lastName=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL4));
        final String email=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL1));
        final DateWish birthDate= new DateWish();
        birthDate.setDate(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL6)));
        final String password=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL2));
        final byte[] profilePhoto=cursor.getBlob(cursor.getColumnIndex(UserDatabaseHelper.USER_COL10));
        final boolean notification=true;
        final String favoriteColor=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL9));
        final String size=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL7));
        final String shoeSize=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL8));
        final User user=new User(address,firstName,lastName,email,birthDate,password,ImageHelper.getImage(profilePhoto),favoriteColor,size,shoeSize);
        user.setUserID(cursor.getInt(0));
        cursor.close();
        return user;
    }
    public ArrayList<User> getAllUser(){
        final SQLiteDatabase db= this.getReadableDatabase();
        final Cursor cursor=db.rawQuery("SELECT * FROM "+ UserDatabaseHelper.USER_TABLE_NAME,null);
        final ArrayList<User> users=new ArrayList<User>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            final String addressString=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL5));
            final Address address=Address.fromString(addressString);
            final String firstName=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL3));
            final String lastName=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL4));
            final String email=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL1));
            final DateWish birthDate= new DateWish();
            birthDate.setDate(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL6)));
            final String password=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL2));
            final byte[] profilePhoto=cursor.getBlob(cursor.getColumnIndex(UserDatabaseHelper.USER_COL10));
            final boolean notification=true;
            final String favoriteColor=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL9));
            final String size=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL7));
            final String shoeSize=cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.USER_COL8));
            final User user =new User(address,firstName,lastName,email,birthDate,password,ImageHelper.getImage(profilePhoto),favoriteColor,size,shoeSize);
            user.setUserID(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.USER_COL0)));
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public boolean updateUser(final User user, final int userID){
        final SQLiteDatabase db= this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(UserDatabaseHelper.USER_COL1,user.getEmail());
        contentValues.put(UserDatabaseHelper.USER_COL2,user.getPassword());
        contentValues.put(UserDatabaseHelper.USER_COL3,user.getFirstName());
        contentValues.put(UserDatabaseHelper.USER_COL4,user.getLastName());
        contentValues.put(UserDatabaseHelper.USER_COL5,user.getAddress().toString());
        contentValues.put(UserDatabaseHelper.USER_COL6,user.getBirthDate().toString());
        contentValues.put(UserDatabaseHelper.USER_COL7,user.getSize());
        contentValues.put(UserDatabaseHelper.USER_COL8,user.getShoeSize());
        contentValues.put(UserDatabaseHelper.USER_COL9,user.getFavoriteColor());
        if(user.getProfilePhoto()!=null){
            contentValues.put(UserDatabaseHelper.USER_COL10,ImageHelper.getBytes(user.getProfilePhoto()));
        }
        contentValues.put(UserDatabaseHelper.USER_COL11,user.isNotification());
        final int err=db.update(UserDatabaseHelper.USER_TABLE_NAME,contentValues, UserDatabaseHelper.USER_COL0 +" = ?",new String[]{String.valueOf(userID)});
        return err!=-1;
    }
    //Check if the couple password UserID is present in the db

    /**
     *
     * @param userID
     * @param password
     * @return
     */
    public boolean checkPassword(final int userID, final String password){
        final SQLiteDatabase db= this.getReadableDatabase();
        final String[] condition ={String.valueOf(userID),password};
        final String selection= UserDatabaseHelper.USER_COL0 +"=? AND "+ UserDatabaseHelper.USER_COL2 +" =?";
        final Cursor cursor=db.query(UserDatabaseHelper.USER_TABLE_NAME,null,selection,condition,null,null,null);
        final boolean sol=cursor.getCount()==1;
        cursor.close();
        return sol;
    }
    //return number of row delete (here max 1 as userID is a primary key)
    public int delete(final int userID){
        final SQLiteDatabase db= this.getWritableDatabase();
        return db.delete(UserDatabaseHelper.USER_TABLE_NAME, UserDatabaseHelper.USER_COL0 +"=?",new String[]{String.valueOf(userID)});
    }
}
