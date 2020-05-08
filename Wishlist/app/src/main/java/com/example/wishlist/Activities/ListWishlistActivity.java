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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_list);
        Intent intent= getIntent();

        //check si l'activité recoit bien le userID
        if (intent.hasExtra("userID")){
            userID = intent.getIntExtra("userID",-1);
        } else{
            Intent backToLogin=new Intent(this,LoginActivity.class);
            startActivity(backToLogin);
        }
        if (intent.hasExtra("receiverID")){
            receiverID = intent.getIntExtra("receiverID",-1);
            if(userID != receiverID){
                isMyWishlist = false;
            }
        }
        if (intent.hasExtra("isMyWishlist")){
            isMyWishlist = intent.getBooleanExtra("isMyWishlist",false);
        }

        displayID = userID;

        if(!isMyWishlist){
            View addBtn = findViewById(R.id.contextMenuWishlist);
            ((ViewGroup) addBtn.getParent()).removeView(addBtn);
            displayID = receiverID;
        }
    }

    public void layoutUpdate(){
        //Va chercher dans la BDD les wishlist d'un utilisateur grace a son userID
        WishlistDatabaseHelper db = new WishlistDatabaseHelper(getApplicationContext());
        ArrayList<Wishlist> list = db.getUserWishlist(displayID);

        ListView wishlistListView = findViewById(R.id.wishlist_listview);
        wishlistListView.setAdapter(new WishlistAdapter(this, list));
    }

    public void pressAddButton(View view){
        AddWishlistFragment dialog=new AddWishlistFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        dialog.setArguments(args);
        dialog.show(this.getSupportFragmentManager(),"Add list");

    }

    //methode appelé apres l'ajout d'une wishlist
    public void fragmentReturn(){
        layoutUpdate();
    }

    public void wishlistAdapterReturn(int wishlistID, int receiverID, String wishlistName){
        Intent goToDetail = new Intent(this, DetailWishlistActivity.class);
        goToDetail.putExtra("wishlistID",wishlistID);
        goToDetail.putExtra("receiverID",receiverID);
        goToDetail.putExtra("userID", userID);
        goToDetail.putExtra("wishlistName",wishlistName);
        goToDetail.putExtra("isMyWishlist", isMyWishlist);
        this.startActivity(goToDetail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutUpdate();
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }


}
