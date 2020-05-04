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
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.Fragment.AddWishlistFragment;
import com.example.wishlist.R;

import java.util.ArrayList;

public class DetailWishlistActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_detail);

    }
}
