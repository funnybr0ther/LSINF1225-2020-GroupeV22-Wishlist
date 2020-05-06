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

import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Adapters.WishlistAdapter;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.Fragment.AddWishlistFragment;
import com.example.wishlist.R;

import java.util.ArrayList;

public class ListWishlistActivity extends AppCompatActivity  {

    private int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_list);
        Intent intent=getIntent();

        //check si l'activité recoit bien le userID
        if (intent.hasExtra("userID")){
            userID = intent.getIntExtra("userID",-1);
        } else{
            Intent backToLogin=new Intent(this,LoginActivity.class);
            startActivity(backToLogin);
        }

        //Va chercher dans la BDD les wishlist d'un utilisateur grace a son userID
        WishlistDatabaseHelper db = new WishlistDatabaseHelper(getApplicationContext());
        ArrayList<Wishlist> list = db.getUserWishlist(userID);

        ListView wishlistListView = findViewById(R.id.wishlist_listview);
        wishlistListView.setAdapter(new WishlistAdapter(this, list));
    }

    public void pressAddButton(View view){
        AddWishlistFragment dialog=new AddWishlistFragment();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        dialog.setArguments(args);
        dialog.show(ListWishlistActivity.this.getSupportFragmentManager(),"Add list");

    }

    //methode appelé apres l'ajout d'une wishlist
    public void fragmentReturn(){
        WishlistDatabaseHelper db = new WishlistDatabaseHelper(getApplicationContext());
        ArrayList<Wishlist> list = db.getUserWishlist(userID);

        ListView wishlistListView = findViewById(R.id.wishlist_listview);
        wishlistListView.setAdapter(new WishlistAdapter(this, list));
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }


}
