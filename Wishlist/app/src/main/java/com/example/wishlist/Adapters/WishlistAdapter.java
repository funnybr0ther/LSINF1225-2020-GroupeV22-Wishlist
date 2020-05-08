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

    public WishlistAdapter(Context context, ArrayList<Wishlist> wishlists){
        this.context = context;
        this.wishlists = wishlists;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * get the number wishlists
     * @return size of the wishlists
     */
    @Override
    public int getCount() {
        return wishlists.size();
    }

    /**
     * get wishlist with an index in the ArrayList
     * @param position index of the wishlist
     * @return Wishlist at this index
     */
    @Override
    public Wishlist getItem(int position) {
        return wishlists.get(position);
    }

    /**
     * always return 0
     * @param position Integer
     * @return always 0
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Create a layout personalized for each wishlist
     * @param position Integer
     * @param view View
     * @param parent ViewGrouo
     * @return a View personalized for each wishlist
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_wishlist, null);
        final Wishlist list = getItem(position);
        final String name = list.getName();
        TextView itemNameView = view.findViewById(R.id.wishlistItem_name);
        itemNameView.setText(name);

        view.setOnClickListener(new View.OnClickListener() {
            /**
             * Display the wishlist content
             * @param v View
             */
            @Override
            public void onClick(View v) {
                ListWishlistActivity call = (ListWishlistActivity) context;
                call.wishlistAdapterReturn(list.getWishlistID(), list.getUserID(), name);
            }
        });
        return view;
    }
}
