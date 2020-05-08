package com.example.wishlist.Activities;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wishlist.Adapters.PurchaseAdapter;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.PurchaseDatabaseHelper;
import com.example.wishlist.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PurchaseViewActivity extends AppCompatActivity {
    private static final String TAG = "MainHistoriqueActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_viewhistorique);
        Log.d(TAG, "onCreate: started.");
        ListView listViewPurchases= findViewById(R.id.HistoriqueItems);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int userID=prefs.getInt("userID",-1);

        PurchaseDatabaseHelper dbHelper=new PurchaseDatabaseHelper(getApplicationContext());
        ArrayList<Purchase> list=dbHelper.getUserHistory(userID);
        ArrayList<Purchase> list2 = dbHelper.getAllPurchases(userID);
        list.addAll(list2);
        Comparator<Purchase> byDateNewestFirst=new Comparator<Purchase>() {
            @Override
            public int compare(Purchase o1, Purchase o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        Collections.sort(list,byDateNewestFirst);
        listViewPurchases.setAdapter(new PurchaseAdapter(this,list));
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
