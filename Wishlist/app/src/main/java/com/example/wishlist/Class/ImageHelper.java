package com.example.wishlist.Class;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageHelper {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        if(bitmap==null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
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

    public static Bitmap getBitmapFromFile(String filepath){
        return BitmapFactory.decodeFile(filepath);
    }

//    public static void writeBitmaptoFile(Bitmap bitmap, String filename){
//        File f = new File(context.getCacheDir(), filename);
//        f.createNewFile();
//
////Convert bitmap to byte array
//        Bitmap bitmap = your bitmap;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//        byte[] bitmapdata = bos.toByteArray();
//
////write the bytes in file
//        FileOutputStream fos = new FileOutputStream(f);
//        fos.write(bitmapdata);
//        fos.flush();
//        fos.close();
//    }
}
