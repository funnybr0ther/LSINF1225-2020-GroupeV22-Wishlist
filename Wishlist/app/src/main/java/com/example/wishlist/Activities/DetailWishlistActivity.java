package com.example.wishlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
    WishlistDatabaseHelper dbWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_detail);
        dbWishlist = new WishlistDatabaseHelper(getApplicationContext());
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
        int[] productID = dbWishlist.getProducts(wishlistID);
    }

    public ArrayList<Product> getProductFromReference(int[] productID){
        ArrayList<Product> products = new ArrayList<Product>();
        //TODO
        return products;
    }

    public void pressAddButton(View view){
        Intent intent = new Intent(this,EditProductActivity.class);
        intent.putExtra("productID",-1);
        startActivityForResult(intent,4);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == RESULT_OK) {
                if(data.hasExtra("newProduct")) {
                    int productID = data.getIntExtra("newProduct", -1);
                    dbWishlist.addProduct(productID,wishlistID);
                    //TODO quantity
                }
            }
        }
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
