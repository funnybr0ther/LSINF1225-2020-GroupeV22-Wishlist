package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wishlist.Activities.DetailWishlistActivity;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.R;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Purchase> purchases;
    private LayoutInflater inflater;

    public PurchaseAdapter(Context context, ArrayList<Purchase> purchases){
        this.context = context;
        this.purchases = purchases;
        this.inflater = LayoutInflater.from(context);
    }

    public int getUserID() {
        SharedPreferences prefs = context.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        return prefs.getInt("userID", -1);
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Purchase getItem(int position) {
        return purchases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_purchase, null);
        UserDatabaseHelper userDatabaseHelper=new UserDatabaseHelper(context.getApplicationContext());
        ProductDatabaseHelper productDatabaseHelper=new ProductDatabaseHelper(context.getApplicationContext());
        final Purchase purchase = getItem(position);

        String sender=userDatabaseHelper.getUserFromID(purchase.getSender()).getFirstName();
        String receiver=userDatabaseHelper.getUserFromID(purchase.getReceiver()).getFirstName();
        String product=productDatabaseHelper.getProductFromID(purchase.getProductID()).getName();
        String purchaseText;
        if ( getUserID() == purchase.getSender()) {
            sender = "You ";
            purchaseText = " bought " + product + " for ";}
        else{
            purchaseText = " bought " + product + " for ";
            receiver = "you.";}

        TextView textViewPurchaseSender = view.findViewById(R.id.PurchaseSender);
        textViewPurchaseSender.setText(sender);

        TextView textViewPurchase = view.findViewById(R.id.PurchaseText);
        textViewPurchase.setText(purchaseText);

        TextView textViewPurchaseReceiver = view.findViewById(R.id.PurchaseReceiver);
        textViewPurchaseReceiver.setText(receiver);

        TextView datePurchase=view.findViewById(R.id.DatePurchase);
        datePurchase.setText(purchase.getDate().dateAndHourToString());
        return view;
    }
}