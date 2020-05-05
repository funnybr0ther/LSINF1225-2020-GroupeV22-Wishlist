package com.example.wishlist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {
    String TAG = "sortTag";
    private TextView description;
    private TextView category;
    private TextView info;
    private TextView productName;
    private RatingBar desireBar;
    private TextView price;
    private TextView amountBought;
    private ImageView productImage;
    private ChipGroup chipGroup;
    private ImageButton editButton;
    private ImageButton addButton;


    int productID;
    int userID;

    ProductDatabaseHelper productDatabaseHelper;
    private String[] testCategoryList = {"Garden","Children"};
    private Product testProduct = new Product("BALANCOIRE",null,"Ceci est une balançoire",testCategoryList,2000,250,4,ProductDatabaseHelper.convertArrayToString(new String[]{"45","46","47"}),33,12);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());

        productID = productDatabaseHelper.addProduct(testProduct);
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
//        productID=intent.getIntExtra("productID",-1);
        boolean myProduct = intent.getBooleanExtra("isMyProduct",true);
        userID = intent.getIntExtra("userID",-1);
        if(myProduct){
            setContentView(R.layout.view_my_product);
            editButton = findViewById(R.id.editProduct);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToEdit(ViewProductActivity.this.productID);
                }
            });
        }
        else{
            setContentView(R.layout.view_one_product);
            addButton = findViewById(R.id.copyProduct);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToAdd(ViewProductActivity.this.productID);
                }
            });
        }

        
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        info = findViewById(R.id.info);
        productName = findViewById(R.id.productName);
        desireBar = findViewById(R.id.rating);
        price = findViewById(R.id.priceTag);
        amountBought = findViewById(R.id.boughtAmount);
        productImage = findViewById(R.id.productPhoto);
        chipGroup = findViewById(R.id.categoriesGroup);


        Log.d(TAG, "onCreate: tempProductID = " + this.productID);
        displayProductInfo(productDatabaseHelper.getProductFromID(this.productID));
    }

    public void displayProductInfo(Product product){
        Log.d(TAG, "displayProductInfo: bilibu updated");
        String descriptionString = product.getDescription();
        String name = product.getName();
        String[] categoryStrings = product.getCategory();
        String[] dimensionsStringArray = ProductDatabaseHelper.convertStringToArray(product.getDimensions());
        String dimensionsString = "";
        if(dimensionsStringArray[0] != ""){
            dimensionsString = dimensionsStringArray[0] + " by " + dimensionsStringArray[1] + " by " + dimensionsStringArray[2];
        }
        String weightString = Integer.toString(product.getWeight());
        Integer desire = product.getDesire();
        String pricePoint = Integer.toString(product.getPrice());
        String amount = Integer.toString(product.getTotal());
        String purchased = Integer.toString(product.getPurchased());
        Bitmap photo = product.getPhoto();
        if(name=="Undefined"){
            Toast.makeText(this, "Something went wrong:\n Missing name", Toast  .LENGTH_SHORT).show();
        }
        else{
            productName.setText(name);
        }
        price.setText(pricePoint + "€");
        if(descriptionString=="Undefined") {
            Log.d(TAG, "displayProductInfo: descriptionString was undefined");
            description.setVisibility(View.GONE);
        }
        else{
            description.setText(descriptionString);
        }
        if(categoryStrings.length==0) {
            category.setText("");
        }
        else{
            String categoryString = "";
            chipGroup.removeAllViews();
            for (int i=0;i<categoryStrings.length;i++){
                Chip chip = new Chip(this);
                chip.setText(categoryStrings[i]);
                chipGroup.addView(chip);
            }
        }
        String infoString = "";
        if(dimensionsString!="Undefined"){
            infoString += "Dimensions:\n" + "\t" + dimensionsString + "\n";
        }
        if(weightString != "0"){
            infoString+= "Weight:\n" +"\t" + weightString + " grams\n";
        }
        if(photo!=null){
            productImage.setImageBitmap(photo);
        }
        SpannableString spannableString = new SpannableString(infoString);
        spannableString.setSpan(new UnderlineSpan(), 0, "Dimensions:".length(), 0);
        spannableString.setSpan(new UnderlineSpan(), ("Dimensions:\n" + "\t" + dimensionsString + "\n").length(), ("Dimensions:\n" + "\t" + dimensionsString + "\n").length() + "Weight:".length(), 0);
        info.setText(spannableString);
        desireBar.setRating((float)desire);
        amountBought.setText("Amount Bought : " + purchased + " / " + amount);
    }

    void switchToEdit(int pID){
        Intent intent=new Intent(this, EditProductActivity.class);
        intent.putExtra("productID",pID);
        startActivityForResult(intent,1);
    }

    void switchToAdd(int pID){
        Log.d(TAG, "switchToAdd: pID is" + pID);
        Intent intent = new Intent(this,EditProductActivity.class);
        intent.putExtra("productID",pID);
        intent.putExtra("copyProduct",true);
        startActivityForResult(intent,2);
    }
    public void onBackPressed(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                displayProductInfo(productDatabaseHelper.getProductFromID(productID));
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
        else if(requestCode==2){
            if(resultCode==RESULT_OK){
                final Wishlist[] chosenWishlist = {null};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                int tempProductID = data.getIntExtra("newProduct",-1);
                if(tempProductID==-1){
                    return;
                }
                displayProductInfo(productDatabaseHelper.getProductFromID(tempProductID));
                builder.setTitle("Choose a wishlist");
                WishlistDatabaseHelper wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
                final ArrayList<Wishlist> wishlists =  wishlistDatabaseHelper.getUserWishlist(userID);
                String[] wishlistNames = new String[wishlists.size()];
                for (int i = 0; i < wishlists.size(); i++) {
                    wishlistNames[i] = wishlists.get(i).getName();
                    Log.d(TAG, "onActivityResult: wishlistNames" + wishlistNames[i]);
                }

                builder.setSingleChoiceItems(wishlistNames,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenWishlist[0] = wishlists.get(which);
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenWishlist[0] = null;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                if(chosenWishlist[0]==null){
                }
                else{
                    //TODO : Add product to selected wishlist
                }
                displayProductInfo(productDatabaseHelper.getProductFromID(productID));
            }
        }

    }//onActivityResult

}
