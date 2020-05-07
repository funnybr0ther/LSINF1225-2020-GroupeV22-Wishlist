package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wishlist.Activities.DetailWishlistActivity;
import com.example.wishlist.Activities.ListWishlistActivity;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.R;

import java.util.ArrayList;

public class WishlistAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Wishlist> wishlists;
    private final LayoutInflater inflater;

    public WishlistAdapter(final Context context, final ArrayList<Wishlist> wishlists){
        this.context = context;
        this.wishlists = wishlists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.wishlists.size();
    }

    @Override
    public Wishlist getItem(final int position) {
        return this.wishlists.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = this.inflater.inflate(R.layout.adapter_wishlist, null);
        final Wishlist list = this.getItem(position);
        final String name = list.getName();
        final TextView itemNameView = view.findViewById(R.id.wishlistItem_name);
        itemNameView.setText(name);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                /*Intent goToDetail = new Intent(context, DetailWishlistActivity.class);
                goToDetail.putExtra("wishlistID",list.getWishlistID());
                goToDetail.putExtra("userID",list.getUserID());
                goToDetail.putExtra("wishlistName",name);
                goToDetail.putExtra("isMyWishlist",context.);
                context.startActivity(goToDetail);*/
                final ListWishlistActivity call = (ListWishlistActivity) WishlistAdapter.this.context;
                call.wishlistAdapterReturn(list.getWishlistID(), list.getUserID(), name);
            }
        });

        return view;
    }
}
