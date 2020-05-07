package com.example.wishlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.Adapters.WishlistAdapter;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.Fragment.AddWishlistFragment;
import com.example.wishlist.R;

import java.util.ArrayList;

public class ListWishlistActivity extends AppCompatActivity  {

    private int userID; //
    private boolean isMyWishlist;
    private int receiverID;
    private int displayID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wishlist_list);
        final Intent intent= this.getIntent();

        //check si l'activité recoit bien le userID
        if (intent.hasExtra("userID")){
            this.userID = intent.getIntExtra("userID",-1);
        } else{
            final Intent backToLogin=new Intent(this,LoginActivity.class);
            this.startActivity(backToLogin);
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

        this.displayID = this.userID;

        if(!this.isMyWishlist){
            final View addBtn = this.findViewById(R.id.contextMenuWishlist);
            ((ViewGroup) addBtn.getParent()).removeView(addBtn);
            this.displayID = this.receiverID;
        }
    }

    public void layoutUpdate(){
        //Va chercher dans la BDD les wishlist d'un utilisateur grace a son userID
        final WishlistDatabaseHelper db = new WishlistDatabaseHelper(this.getApplicationContext());
        final ArrayList<Wishlist> list = db.getUserWishlist(this.displayID);

        final ListView wishlistListView = this.findViewById(R.id.wishlist_listview);
        wishlistListView.setAdapter(new WishlistAdapter(this, list));
    }

    public void pressAddButton(final View view){
        final AddWishlistFragment dialog=new AddWishlistFragment();
        final Bundle args = new Bundle();
        args.putInt("userID", this.userID);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(),"Add list");

    }

    //methode appelé apres l'ajout d'une wishlist
    public void fragmentReturn(){
        this.layoutUpdate();
    }

    public void wishlistAdapterReturn(final int wishlistID, final int receiverID, final String wishlistName){
        final Intent goToDetail = new Intent(this, DetailWishlistActivity.class);
        goToDetail.putExtra("wishlistID",wishlistID);
        goToDetail.putExtra("receiverID",receiverID);
        goToDetail.putExtra("userID", this.userID);
        goToDetail.putExtra("wishlistName",wishlistName);
        goToDetail.putExtra("isMyWishlist", this.isMyWishlist);
        startActivity(goToDetail);
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
