package com.example.wishlist.Activities;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.Product;
import com.example.wishlist.R;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ViewProductActivity extends AppCompatActivity {
    String TAG = "sortTag";
    private TextView description;
    private TextView category;
    private TextView info;
    private TextView productName;
    private RatingBar desireBar;
    private TextView price;
    private TextView amountBought;

    private String[] testCategoryList = {"Garden","Kids"};
    private Product testProduct = new Product("BALANCOIRE",null,"Ceci est une balançoire",testCategoryList,2000,250,4,"40x30x60cm",5,33,12);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product);
        
        description = (TextView)findViewById(R.id.description);
        category = (TextView)findViewById(R.id.category);
        info = (TextView)findViewById(R.id.info);
        productName = (TextView)findViewById(R.id.productName);
        desireBar = (RatingBar)findViewById(R.id.rating);
        price = (TextView)findViewById(R.id.priceTag);
        amountBought = (TextView)findViewById(R.id.boughtAmount);
        displayProductInfo(testProduct);
    }

    public void displayProductInfo(Product product){
        String descriptionString = product.getDescription();
        String name = product.getName();
        String[] categoryStrings = product.getCategory();
        String dimensionsString = product.getDimensions();
        String weightString = Integer.toString(product.getWeight());
        Integer desire = product.getDesire();
        String pricePoint = Integer.toString(product.getPrice());
        String amount = Integer.toString(product.getAmount());
        String purchased = Integer.toString(product.getPurchased());
        String photo = product.getPhoto();
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
            for (int i=0;i<categoryStrings.length;i++){
                categoryString += (categoryStrings[i] + "\t");
            }
            category.setText(categoryString);
        }
        String infoString = "";
        if(dimensionsString!="Undefined"){
            infoString += "Dimensions:\n" + "\t" + dimensionsString + "\n";
        }
        if(weightString != "0"){
            infoString+= "Weight:\n" +"\t" + weightString + " grams\n";
        }
        SpannableString spannableString = new SpannableString(infoString);
        spannableString.setSpan(new UnderlineSpan(), 0, "Dimensions:".length(), 0);
        spannableString.setSpan(new UnderlineSpan(), ("Dimensions:\n" + "\t" + dimensionsString + "\n").length(), ("Dimensions:\n" + "\t" + dimensionsString + "\n").length() + "Weight:".length(), 0);
        info.setText(spannableString);
        desireBar.setRating((float)desire);
        amountBought.setText("Amount Bought : " + purchased + " / " + amount);

    }


}