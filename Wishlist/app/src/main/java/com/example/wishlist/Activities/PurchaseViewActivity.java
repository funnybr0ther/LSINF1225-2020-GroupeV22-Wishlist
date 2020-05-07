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
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_viewhistorique);
        Log.d(PurchaseViewActivity.TAG, "onCreate: started.");
        final ListView listViewPurchases= this.findViewById(R.id.HistoriqueItems);
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int userID=prefs.getInt("userID",-1);

        final PurchaseDatabaseHelper dbHelper=new PurchaseDatabaseHelper(this.getApplicationContext());
        final ArrayList<Purchase> list=dbHelper.getUserHistory(userID);   // Historique quand user = bénéficiaire
        final ArrayList<Purchase> list2 = dbHelper.getAllPurchases(userID);   // Historique quand user = acheteur
        list.addAll(list2);
        final Comparator<Purchase> byDateNewestFirst=new Comparator<Purchase>() {
            @Override
            public int compare(final Purchase o1, final Purchase o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        Collections.sort(list,byDateNewestFirst);
        listViewPurchases.setAdapter(new PurchaseAdapter(this,list));
    }

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }
}
