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

    public DateWish(final Date date) {
        this.date = date;
    }
    public DateWish(final int day, final String month, final int year){
        this.setDate( day,  month,  year);
    }

    public DateWish() {
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final int day, final String month, final int year ){
        try{
            final String dateStr= day + " "+month+" "+ year;
            this.date = this.formatter.parse(dateStr);
        }catch (final Exception e){

        }
        Log.d(DateWish.TAG, "setDate: date set!");
    }
    public String toString(){
        Log.d(DateWish.TAG, "toString: ");
        return this.formatter.format(this.date);
    }
    public void setDate(final String dateString){
        try{
            this.date = this.formatter.parse(dateString);
        }catch (final Exception e){

        }
    }
    public void setDateAndHour(final Date time){
        this.date =time;
    }

    public void setDateAndHourFromString(final String str){
        try{
            this.date = this.formatterDateAndHour.parse(str);
        }catch (final Exception e){}
    }

    public int compareTo(final DateWish date2) {return this.date.compareTo(date2.getDate()); }
    public String dateAndHourToString(){
        if(date ==null) return "Error";
        Log.d(DateWish.TAG, "toD&HString: ");
        return this.formatterDateAndHour.format(this.date);
    }
}
