package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
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
        String purchaseText= sender+" bought "+product+" to "+receiver;
        TextView textViewPurchase = view.findViewById(R.id.PurchaseText);
        textViewPurchase.setText(purchaseText);
        TextView datePurchase=view.findViewById(R.id.DatePurchase);
        /*if (.equals("5 May 2020")){
            Toast.makeText(context,"Bad",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Good",Toast.LENGTH_SHORT).show();
        }*/
        datePurchase.setText(purchase.getDate().dateAndHourToString());
        return view;
    }
}