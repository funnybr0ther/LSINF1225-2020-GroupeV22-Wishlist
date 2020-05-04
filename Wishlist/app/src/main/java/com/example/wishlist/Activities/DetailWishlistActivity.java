package com.example.wishlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.wishlist.Adapters.WishlistAdapter;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.Fragment.AddWishlistFragment;
import com.example.wishlist.R;

import java.util.ArrayList;

public class DetailWishlistActivity extends AppCompatActivity {

    private int userID;
    private int wishlistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_detail);
        Intent intent=getIntent();

        //check que les elements necessaire à l'activite sont present
        if (intent.hasExtra("userID")){
            userID = intent.getIntExtra("userID",-1);
        } else{
            Intent backToLogin=new Intent(this,LoginActivity.class);
            startActivity(backToLogin);
        }

        if (intent.hasExtra("wishlistID")){
            wishlistID = intent.getIntExtra("wishlistID",-1);
        } else{
            Intent backToWishlist=new Intent(this,LoginActivity.class);
            backToWishlist.putExtra("userID",userID);
            startActivity(backToWishlist);
        }

        //Va chercher dans la BDD les product d'une wishlist grace a sa wishlistID
        WishlistDatabaseHelper dbWishlist = new WishlistDatabaseHelper(getApplicationContext());
        int[] productID = dbWishlist.getProducts(wishlistID);
    }

    public ArrayList<Product> getProductFromReference(int[] productID){
        ArrayList<Product> products = new ArrayList<Product>();
        //TODO
        return products;
    }

}
