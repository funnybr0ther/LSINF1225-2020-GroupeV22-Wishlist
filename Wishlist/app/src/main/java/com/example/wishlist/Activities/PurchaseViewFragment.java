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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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
        //SetUpPurchaselist(UserID);  -> Cfr l'userID qu'on se passe par Singleton/Bundle/... ?

        return view;
    }

    private void SetUpPurchaselist(int UserID){
        final ArrayList<Purchase> purhcases = new ArrayList<>();
        PurchaseDatabaseHelper databaseHelper = new PurchaseDatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllPurchases(UserID);
        if (!cursor.moveToNext()){
            Toast.makeText(getActivity()," No purchase has been made !", Toast.LENGTH_LONG).show();
        }
        while (cursor.moveToNext()){    //getX où X est le type de donnée stockée
            DateWish date = new DateWish();
            date.setDateAndHourFromString(cursor.getString(5));
            purhcases.add(new Purchase(
                    cursor.getInt(1),   // ID Acheteur
                    cursor.getInt(2),   // ID Receveur
                    cursor.getInt(3),    //  Product
                    cursor.getInt(4),   // quantité
                    date));  // DateWish
        }

        // On trie les Purchase selon leur date sous le format : (YYYY MM DD) !!!
        Collections.sort(purhcases, new Comparator<Purchase>() {
            @Override
            public int compare(Purchase A, Purchase B) {
                return A.getDate().compareTo(B.getDate());
            }
        });

        adapter = new PurchaseList(getActivity(),R.layout.historique_liste,purhcases,"");
        purchaselist.setAdapter(adapter);

    }
}
