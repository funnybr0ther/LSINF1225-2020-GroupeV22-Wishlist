package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    /**
     * get the size of the wishlists
     * @return size of the wishlists
     */
    @Override
    public int getCount() {
        return products.size();
    }

    /**
     * get product with an index in the ArrayList
     * @param position index of the product
     * @return Product at this index
     */
    @Override
    public Product getItem(int position) {
        return products.get(position);
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
     * Create a layout personalized for each product
     * @param position Integer
     * @param view View
     * @param parent ViewGrouo
     * @return a View personalized for each wishlist
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.adapter_product, null);
        final Product product = getItem(position);
        String name = product.getName();
        TextView itemNameView = view.findViewById(R.id.productItem_name);
        itemNameView.setText(name);
        TextView price = view.findViewById(R.id.productItem_price);
        price.setText(product.getPrice() + " € ");
        ImageView icon = view.findViewById(R.id.productItem_icon);
        Bitmap photo = product.getPhoto();
        if(photo != null) {
            int w = photo.getWidth();
            int h = photo.getHeight();
            if(w > 2000 || h > 2000){
                photo = Bitmap.createScaledBitmap(photo, photo.getWidth() / 20, photo.getHeight() / 20, true);
                icon.setImageBitmap(photo);
            }
            else if(w > 1000 || h > 1000) {
                photo = Bitmap.createScaledBitmap(photo, photo.getWidth() / 10, photo.getHeight() / 10, true);
                icon.setImageBitmap(photo);
            }else if(w > 300 || h > 300) {
                photo = Bitmap.createScaledBitmap(photo, photo.getWidth() / 3, photo.getHeight() / 3, true);
                icon.setImageBitmap(photo);
            }else{
                icon.setImageBitmap(photo);
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Display the product content
                 * @param v View
                 */
                DetailWishlistActivity detail = (DetailWishlistActivity) context;
                detail.productDetail(position);    //modifier pour wishlist externe
            }
        });

        return view;
    }
}
