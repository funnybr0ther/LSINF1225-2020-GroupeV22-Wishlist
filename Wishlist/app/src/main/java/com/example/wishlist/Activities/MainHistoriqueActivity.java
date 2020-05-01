package com.example.wishlist.Activities;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wishlist.R;

public class MainHistoriqueActivity extends AppCompatActivity {
    private static final String TAG = "MainHistoriqueActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_historique);
        Log.d(TAG, "onCreate: started.");
        
        init();
    }

    private void init(){
        PurchaseViewFragment fragment = new PurchaseViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);   // Je pense qu'on doit ajouter le fragment/activity de la page précédente (menu?)
        transaction.commit();
    }
}
