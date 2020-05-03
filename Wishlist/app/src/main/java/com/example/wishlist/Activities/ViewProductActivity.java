package com.example.wishlist.Activities;

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
import com.example.wishlist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


import androidx.appcompat.app.AppCompatActivity;

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

    long tempProductID;

    private String[] testCategoryList = {"Garden","Children"};
    private Product testProduct = new Product("BALANCOIRE",null,"Ceci est une balançoire",testCategoryList,2000,250,4,"40,30,60", 33,12);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());

        tempProductID = productDatabaseHelper.addProduct(testProduct);
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        final long productID=intent.getIntExtra("productID",-1);
        setContentView(R.layout.view_product);
        
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        info = findViewById(R.id.info);
        productName = findViewById(R.id.productName);
        desireBar = findViewById(R.id.rating);
        price = findViewById(R.id.priceTag);
        amountBought = findViewById(R.id.boughtAmount);
        productImage = findViewById(R.id.productPhoto);
        chipGroup = findViewById(R.id.categoriesGroup);
        editButton = findViewById(R.id.editProduct);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToEdit(tempProductID);
            }
        });
        Log.d(TAG, "onCreate: tempProductID = " +tempProductID );
        displayProductInfo(productDatabaseHelper.getProductFromID(tempProductID));
    }

    public void displayProductInfo(Product product){
        Log.d(TAG, "displayProductInfo: bilibu updated");
        String descriptionString = product.getDescription();
        String name = product.getName();
        String[] categoryStrings = product.getCategory();
        String dimensionsString = product.getDimensions();
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
//        productImage.setImageURI();
    }

    void switchToEdit(long pID){
        Intent intent1=new Intent(this, EditProductActivity.class);
        intent1.putExtra("productID",pID);
        startActivityForResult(intent1,1);
    }
    public void onBackPressed(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(this);
                displayProductInfo(productDatabaseHelper.getProductFromID(tempProductID));
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }//onActivityResult

}
