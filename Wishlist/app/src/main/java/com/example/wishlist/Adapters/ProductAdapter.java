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

    public ProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_product, null);
        final Product product = getItem(position);
        String name = product.getName();
        TextView itemNameView = view.findViewById(R.id.productItem_name);
        itemNameView.setText(name);
        TextView price = view.findViewById(R.id.productItem_price);
        price.setText(product.getPrice() + " € ");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailWishlistActivity detail = (DetailWishlistActivity) context;
                detail.productDetail(position);    //modifier pour wishlist externe
            }
        });

        return view;
    }
}
