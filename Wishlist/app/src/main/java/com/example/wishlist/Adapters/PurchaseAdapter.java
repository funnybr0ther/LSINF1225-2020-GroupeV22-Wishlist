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
import com.example.wishlist.Activities.MyProfileActivity;
import com.example.wishlist.Activities.OtherProfile;
import com.example.wishlist.Activities.ViewProductActivity;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.R;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Purchase> purchases;
    private final LayoutInflater inflater;

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
        TextView textViewPurchaseSender = view.findViewById(R.id.PurchaseSender);
        TextView textViewPurchaseReceiver = view.findViewById(R.id.PurchaseReceiver);
        TextView textViewPurchase = view.findViewById(R.id.PurchaseText);
        String sender=userDatabaseHelper.getUserFromID(purchase.getSender()).getFirstName();
        String receiver=userDatabaseHelper.getUserFromID(purchase.getReceiver()).getFirstName();
        String product=productDatabaseHelper.getProductFromID(purchase.getProductID()).getName();
        String purchaseText;
        if (getUserID() == purchase.getSender()) {
            sender = "You ";
            purchaseText =  product;
            textViewPurchaseSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myProfileIntent= new Intent(context, MyProfileActivity.class);
                    context.startActivity(myProfileIntent);
                }
            });
            textViewPurchaseReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent otherProfileIntent= new Intent(context, OtherProfile.class);
                    otherProfileIntent.putExtra("otherUserID",purchase.getReceiver());
                    context.startActivity(otherProfileIntent);
                }
            });
            textViewPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productIntent=new Intent(context, ViewProductActivity.class);
                    productIntent.putExtra("receiverID",purchase.getReceiver());
                    productIntent.putExtra("productID",purchase.getProductID());
                    productIntent.putExtra("isMyProduct",false);
                    context.startActivity(productIntent);
                }
            });
        }
        else{
            purchaseText =product;
            receiver = "you";
            textViewPurchaseReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myProfileIntent= new Intent(context, MyProfileActivity.class);
                    context.startActivity(myProfileIntent);
                }
            });
            textViewPurchaseSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent otherProfileIntent= new Intent(context, OtherProfile.class);
                    otherProfileIntent.putExtra("otherUserID",purchase.getSender());
                    context.startActivity(otherProfileIntent);
                }
            });
            textViewPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productIntent=new Intent(context, ViewProductActivity.class);
                    productIntent.putExtra("productID",purchase.getProductID());
                    productIntent.putExtra("isMyProduct",true);
                    context.startActivity(productIntent);
                }
            });
        }

        textViewPurchaseSender.setText(sender);
        textViewPurchase.setText(purchaseText);

        textViewPurchaseReceiver.setText(receiver);

        TextView datePurchase=view.findViewById(R.id.DatePurchase);
        datePurchase.setText(purchase.getDate().dateAndHourToString());
        return view;
    }
}