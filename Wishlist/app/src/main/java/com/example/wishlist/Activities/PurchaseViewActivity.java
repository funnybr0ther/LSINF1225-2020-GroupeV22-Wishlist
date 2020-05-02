package com.example.wishlist.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.PurchaseList;
import com.example.wishlist.R;

import java.util.ArrayList;

public class PurchaseViewActivity extends Fragment {
    private static final String TAG = "PurchaseViewActivity";
    private PurchaseList adapter;
    private ListView purchaselistt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewhistorique,container,false);
        /*purchaselistt = (ListView) view.findViewById(R.id.HistoriqueItems);
        Log.d(TAG, "onCreateView: started");
        SetUpPurchaselist();*/

        return view;
    }

    private void SetUpPurchaselist(){
        final ArrayList<Purchase> purhcases = new ArrayList<>();
        purhcases.add(new Purchase(7,9,new DateWish(27,"april",2020),5,new Product()));
        purhcases.add(new Purchase(7,9,new DateWish(27,"april",2020),5,new Product()));
        purhcases.add(new Purchase(7,9,new DateWish(27,"april",2020),5,new Product()));
        purhcases.add(new Purchase(7,9,new DateWish(27,"april",2020),5,new Product()));
        adapter = new PurchaseList(getActivity(),R.layout.historique_liste,purhcases,"");
        purchaselistt.setAdapter(adapter);

    }
}
