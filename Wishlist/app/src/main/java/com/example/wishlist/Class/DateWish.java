package com.example.wishlist.Class;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateWish {

    private static final String TAG = "DateWish";
    private Date date;
    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat formatterDateAndHour = new SimpleDateFormat("EEE MMM dd h:mm a yyyy", Locale.ENGLISH);

    public DateWish(Date date) {
        this.date = date;
    }
    public DateWish(int day, String month, int year){
        setDate( day,  month,  year);
    }

    public DateWish() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(int day, String month, int year ){
        try{
            String dateStr=Integer.toString(day)+ " "+month+" "+Integer.toString(year);
            date=formatter.parse(dateStr);
        }catch (Exception e){

        }
        Log.d(TAG, "setDate: date set!");
    }
    public String toString(){
        Log.d(TAG, "toString: ");
        return formatter.format(date);
    }
    public void setDate(String dateString){
        try{
            date=formatter.parse(dateString);
        }catch (Exception e){

        }
    }
    public void setDateAndHour(Date time){
        date=time;
    }

    public void setDateAndHourFromString(String str){
        try{
            date=formatterDateAndHour.parse(str);
        }catch (Exception e){}
    }

    public int compareTo(DateWish date2){
        return date.compareTo(date2.getDate());
    }

    public String dateAndHourToString(){
        if(this.date==null) return "Error";
        Log.d(TAG, "toD&HString: ");
        return formatterDateAndHour.format(date);
    }
}
