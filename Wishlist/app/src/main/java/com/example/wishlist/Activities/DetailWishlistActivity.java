package com.example.wishlist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.Adapters.ProductAdapter;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.Fragment.ChangeWishlistNameFragment;
import com.example.wishlist.R;

import java.util.ArrayList;

public class DetailWishlistActivity extends AppCompatActivity {

    private int userID;
    private int wishlistID;
    WishlistDatabaseHelper dbWishlist;
    ArrayList<Product> products;
    private boolean isMyWishlist;
    private int receiverID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wishlist_detail);
        this.dbWishlist = new WishlistDatabaseHelper(this.getApplicationContext());
        final Intent intent= this.getIntent();

        //check que les elements necessaire à l'activite sont present
        if (intent.hasExtra("userID")){
            this.userID = intent.getIntExtra("userID",-1);
        } else{
            final Intent backToLogin=new Intent(this,LoginActivity.class);
            this.startActivity(backToLogin);
        }

        if (intent.hasExtra("wishlistID")){
            this.wishlistID = intent.getIntExtra("wishlistID",-1);
        } else{
            final Intent backToWishlist=new Intent(this,LoginActivity.class);
            backToWishlist.putExtra("userID", this.userID);
            this.startActivity(backToWishlist);
        }
        if (intent.hasExtra("wishlistName")){
           final String wishlistName = intent.getStringExtra("wishlistName");
           final TextView title = this.findViewById(R.id.wishlistToolbarTitle);
           title.setText(wishlistName);
        }
        if (intent.hasExtra("receiverID")){
            this.receiverID = intent.getIntExtra("receiverID",-1);
            if(this.userID != this.receiverID){
                this.isMyWishlist = false;
            }
        }
        if (intent.hasExtra("isMyWishlist")){
            this.isMyWishlist = intent.getBooleanExtra("isMyWishlist",false);
        }

        if(!this.isMyWishlist){
            final View addProductBtn = this.findViewById(R.id.addProductBtn);
            ((ViewGroup) addProductBtn.getParent()).removeView(addProductBtn);
            final View changeName = this.findViewById(R.id.changeNameButton);
            ((ViewGroup) changeName.getParent()).removeView(changeName);
        }

    }

    public void layoutUpdate(){
        //Va chercher dans la BDD les product d'une wishlist grace a sa wishlistID
        final ListView wishlistListView = this.findViewById(R.id.wishlist_DetailView);
        this.products = this.getProductArray();
        wishlistListView.setAdapter(new ProductAdapter(this, this.products));
    }

    public ArrayList<Product> getProductArray(){
        final ProductDatabaseHelper dbProduct = new ProductDatabaseHelper(this.getApplicationContext());
        final ArrayList<Product> products = new ArrayList<Product>();
        final int[] prod = this.dbWishlist.getProducts(this.wishlistID);
        for(final int productID : prod){
            products.add(dbProduct.getProductFromID(productID));
        }
        return products;
    }

    public void pressAddButton(final View view){
        final Intent intent = new Intent(this,EditProductActivity.class);
        intent.putExtra("productID",-1);
        this.startActivityForResult(intent,4);
    }

    public void pressChangeButton(final View view){
        final ChangeWishlistNameFragment dialog=new ChangeWishlistNameFragment();
        final Bundle args = new Bundle();
        args.putInt("wishlistID", this.wishlistID);
        args.putInt("userID", this.userID);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(),"Change name");
    }

    public void fragmentReturn(final String newName){
        final TextView title = this.findViewById(R.id.wishlistToolbarTitle);
        title.setText(newName);
    }

    public void productDetail(final int productPosition){
        final Intent intent1=new Intent(this, ViewProductActivity.class);
        intent1.putExtra("productID", this.dbWishlist.getProducts(this.wishlistID)[productPosition]);
        intent1.putExtra("receiverID", this.receiverID);
        intent1.putExtra("userID", this.userID);
        intent1.putExtra("isMyProduct", this.isMyWishlist);
        this.startActivityForResult(intent1,1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            if (resultCode == Activity.RESULT_OK) {
                if(data.hasExtra("newProduct")) {
                    final int productID = data.getIntExtra("newProduct", -1);
                    this.dbWishlist.addProduct(productID, this.wishlistID);
                    this.addProductReturn();
                }
            }
        }
        else if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Log.d("BILIBU", "onActivityResult: update to products view");
                this.addProductReturn();
            }
        }
    }

    //methode appelé apres l'ajout d'un product
    public void addProductReturn(){
        final ListView wishlistListView = this.findViewById(R.id.wishlist_DetailView);
        this.products = this.getProductArray();
        wishlistListView.setAdapter(new ProductAdapter(this, this.products));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.layoutUpdate();
    }

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }
}
