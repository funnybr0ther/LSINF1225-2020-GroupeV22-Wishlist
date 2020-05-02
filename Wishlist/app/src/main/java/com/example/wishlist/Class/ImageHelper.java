package com.example.wishlist.Class;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageHelper {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        if(bitmap==null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap compress(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        return bitmap;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        if(image==null)return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
