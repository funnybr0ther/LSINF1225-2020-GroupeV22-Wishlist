package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wishlist.Activities.DetailWishlistActivity;
import com.example.wishlist.Activities.EditProductActivity;
import com.example.wishlist.Activities.ViewProductActivity;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Product> products;
    private final LayoutInflater inflater;

    public ProductAdapter(final Context context, final ArrayList<Product> products){
        this.context = context;
        this.products = products;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Product getItem(final int position) {
        return this.products.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        view = this.inflater.inflate(R.layout.adapter_product, null);
        Product product = this.getItem(position);
        final String name = product.getName();
        final TextView itemNameView = view.findViewById(R.id.productItem_name);
        itemNameView.setText(name);
        final TextView price = view.findViewById(R.id.productItem_price);
        price.setText(product.getPrice() + " € ");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DetailWishlistActivity detail = (DetailWishlistActivity) ProductAdapter.this.context;
                detail.productDetail(position);    //modifier pour wishlist externe
            }
        });

        return view;
    }
}
