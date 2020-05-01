package com.example.wishlist.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.PurchaseDatabaseHelper;
import com.example.wishlist.Class.PurchaseList;
import com.example.wishlist.R;

import java.util.ArrayList;

public class PurchaseViewFragment extends Fragment {
    private static final String TAG = "PurchaseViewFragment";
    private PurchaseList adapter;
    private ListView purchaselist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewhistorique,container,false);
        purchaselist = (ListView) view.findViewById(R.id.HistoriqueItems);
        Log.d(TAG, "onCreateView: started");
        SetUpPurchaselist();

        return view;
    }

    private void SetUpPurchaselist(){
        final ArrayList<Purchase> purhcases = new ArrayList<>();
        PurchaseDatabaseHelper databaseHelper = new PurchaseDatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllPurchases();
        if (!cursor.moveToNext()){
            Toast.makeText(getActivity()," No purchase has been made !", Toast.LENGTH_LONG).show();
        }

        while (cursor.moveToNext()){    //getX où X est le type de donnée stockée
            String[] date = cursor.getString(2).split(" ");
            purhcases.add(new Purchase(
                    cursor.getInt(0),   // ID Acheteur
                    cursor.getInt(1),   // ID Receveur
                    new DateWish(Integer.parseInt(date[1]), date[2],Integer.parseInt(date[3])),    // DateWish
                    cursor.getInt(3),   // Quantity
                    cursor.getString(4)));  // ProductName
        }


        adapter = new PurchaseList(getActivity(),R.layout.historique_liste,purhcases,"");
        purchaselist.setAdapter(adapter);

    }
}
