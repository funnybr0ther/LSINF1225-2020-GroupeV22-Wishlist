package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wishlist.Activities.DetailWishlistActivity;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.R;

import java.util.ArrayList;

public class WishlistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Wishlist> wishlists;
    private LayoutInflater inflater;

    public WishlistAdapter(Context context, ArrayList<Wishlist> wishlists){
        this.context = context;
        this.wishlists = wishlists;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wishlists.size();
    }

    @Override
    public Wishlist getItem(int position) {
        return wishlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_wishlist, null);
        final Wishlist list = getItem(position);
        final String name = list.getName();
        TextView itemNameView = view.findViewById(R.id.wishlistItem_name);
        itemNameView.setText(name);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetail = new Intent(context, DetailWishlistActivity.class);
                goToDetail.putExtra("wishlistID",list.getWishlistID());
                goToDetail.putExtra("userID",list.getUserID());
                goToDetail.putExtra("wishlistName",name);
                context.startActivity(goToDetail);
            }
        });

        return view;
    }
}
