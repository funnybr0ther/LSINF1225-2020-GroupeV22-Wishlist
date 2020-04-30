package com.example.wishlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.wishlist.Class.CategoriesAdapter;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.R;
import com.google.android.material.chip.ChipGroup;

public class EditProductActivity extends AppCompatActivity {
//    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(null,EditProductActivity.this);
    int productId;
    Product product;
    private ProductDatabaseHelper productDatabaseHelper;
    public static int NEW_PRODUCT = -2;

    ChipGroup categoriesChipGroup;

    public void onBackPressed(View view) {
        if(product == null){
            onBackPressed();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        Intent intent = getIntent();
        productId = intent.getIntExtra("productId",-1);
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        if(productId==NEW_PRODUCT){

        }
        product = productDatabaseHelper.getProductFromID(productId);
    }


}
