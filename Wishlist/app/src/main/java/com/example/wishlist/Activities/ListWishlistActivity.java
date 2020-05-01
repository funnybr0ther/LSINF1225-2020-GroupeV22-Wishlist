package com.example.wishlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.wishlist.Fragment.AddWishlistFragment;
import com.example.wishlist.R;

public class ListWishlistActivity extends AppCompatActivity {

    private int userID;
    private LinearLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_list);
        Intent intent=getIntent();

        if (intent.hasExtra("userID")){
            userID = intent.getIntExtra("userID",-1);
            TextView textView = findViewById(R.id.textView);
            textView.setText(Integer.toString(userID));
        }
        else{//If no userID go back to LoginActivity
            //Toast toast=new Toast(this,);
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }

        /*
        this.myLayout = (LinearLayout) findViewById(R.id.wishlistDynamicLayout);
        WishlistDatabaseHelper db = new WishlistDatabaseHelper(getApplicationContext());
        ArrayList<Wishlist> list = db.getUserWishlist(userID);
        for(int i = 0; i < list.size(); i++){

        }*/
    }

    public void pressAddButton(View view){
        //AddWishlist menu = new AddWishlist();
        //menu.show(ListWishlistActivity.this.getSupportFragmentManager(), "ha");

        //AddWishlistFragment addMenu = new AddWishlistFragment();
        //addMenu.onCreateView(getLayoutInflater(), null,null);
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragment_container, addMenu);
        //transaction.add(R.id.fragment_container, addMenu).commit();
        //transaction.addToBackStack(null);
        //transaction.commit();

        Button wishlist = new Button(this);
        wishlist.setText("ajout");
        wishlist.setWidth(200);
        myLayout.addView(wishlist);
    }

    public void printWishlist(String name){
        Button wishlist = new Button(this);
        wishlist.setText(name);
        wishlist.setWidth(200);
        myLayout.addView(wishlist);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

}
