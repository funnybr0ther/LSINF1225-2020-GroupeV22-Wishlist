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

    public PurchaseAdapter(final Context context, final ArrayList<Purchase> purchases){
        this.context = context;
        this.purchases = purchases;
        inflater = LayoutInflater.from(context);
    }

    public int getUserID() {
        final SharedPreferences prefs = this.context.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        return prefs.getInt("userID", -1);
    }

    @Override
    public int getCount() {
        return this.purchases.size();
    }

    @Override
    public Purchase getItem(final int position) {
        return this.purchases.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = this.inflater.inflate(R.layout.adapter_purchase, null);
        final UserDatabaseHelper userDatabaseHelper=new UserDatabaseHelper(this.context.getApplicationContext());
        final ProductDatabaseHelper productDatabaseHelper=new ProductDatabaseHelper(this.context.getApplicationContext());
        final Purchase purchase = this.getItem(position);
        final TextView textViewPurchaseSender = view.findViewById(R.id.PurchaseSender);
        final TextView textViewPurchaseReceiver = view.findViewById(R.id.PurchaseReceiver);
        final TextView textViewPurchase = view.findViewById(R.id.PurchaseText);
        String sender=userDatabaseHelper.getUserFromID(purchase.getSender()).getFirstName();
        String receiver=userDatabaseHelper.getUserFromID(purchase.getReceiver()).getFirstName();
        final String product=productDatabaseHelper.getProductFromID(purchase.getProductID()).getName();
        final String purchaseText;
        if (this.getUserID() == purchase.getSender()) {
            sender = "You ";
            purchaseText = " bought " + product + " for ";
            textViewPurchaseSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent myProfileIntent= new Intent(PurchaseAdapter.this.context, MyProfileActivity.class);
                    PurchaseAdapter.this.context.startActivity(myProfileIntent);
                }
            });
            textViewPurchaseReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent otherProfileIntent= new Intent(PurchaseAdapter.this.context, OtherProfile.class);
                    otherProfileIntent.putExtra("otherUserID",purchase.getReceiver());
                    PurchaseAdapter.this.context.startActivity(otherProfileIntent);
                }
            });
            textViewPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent productIntent=new Intent(PurchaseAdapter.this.context, ViewProductActivity.class);
                    productIntent.putExtra("receiverID",purchase.getReceiver());
                    productIntent.putExtra("productID",purchase.getProductID());
                    productIntent.putExtra("isMyProduct",false);
                    PurchaseAdapter.this.context.startActivity(productIntent);
                }
            });
        }
        else{
            purchaseText = " bought " + product + " for ";
            receiver = "you.";
            textViewPurchaseReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent myProfileIntent= new Intent(PurchaseAdapter.this.context, MyProfileActivity.class);
                    PurchaseAdapter.this.context.startActivity(myProfileIntent);
                }
            });
            textViewPurchaseSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent otherProfileIntent= new Intent(PurchaseAdapter.this.context, OtherProfile.class);
                    otherProfileIntent.putExtra("otherUserID",purchase.getSender());
                    PurchaseAdapter.this.context.startActivity(otherProfileIntent);
                }
            });
            textViewPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent productIntent=new Intent(PurchaseAdapter.this.context, ViewProductActivity.class);
                    productIntent.putExtra("productID",purchase.getProductID());
                    productIntent.putExtra("isMyProduct",true);
                    PurchaseAdapter.this.context.startActivity(productIntent);
                }
            });
        }

        textViewPurchaseSender.setText(sender);
        textViewPurchase.setText(purchaseText);

        textViewPurchaseReceiver.setText(receiver);

        final TextView datePurchase=view.findViewById(R.id.DatePurchase);
        /*if (.equals("5 May 2020")){
            Toast.makeText(context,"Bad",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Good",Toast.LENGTH_SHORT).show();
        }*/
        datePurchase.setText(purchase.getDate().dateAndHourToString());
        return view;
    }
}