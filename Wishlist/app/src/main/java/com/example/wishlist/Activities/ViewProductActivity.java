package com.example.wishlist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Class.Purchase;
import com.example.wishlist.Class.PurchaseDatabaseHelper;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Class.Wishlist;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    int receiverID;

    ProductDatabaseHelper productDatabaseHelper;
    PurchaseDatabaseHelper purchaseDatabaseHelper;
    private String[] testCategoryList = {"Garden","Children"};
    private Product testProduct = new Product("BALANCOIRE",null,"Ceci est une balançoire",testCategoryList,2000,250,4,ProductDatabaseHelper.convertArrayToString(new String[]{"45","46","47"}),33,12);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        purchaseDatabaseHelper = new PurchaseDatabaseHelper(getApplicationContext());
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
            receiverID = intent.getIntExtra("receiverID",-1);
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
        String dimensionsString = null;
        Log.d(TAG, "displayProductInfo:"+dimensionsStringArray[0]);
        if(!dimensionsStringArray[0].equals("null")){
            dimensionsString = dimensionsStringArray[0] + " by " + dimensionsStringArray[1] + " by " + dimensionsStringArray[2];
        }
        String weightString=null;
        if(product.getWeight()!=null){
            Log.d(TAG, "displayProductInfo: MAIS BORDEL ");
            weightString = Integer.toString(product.getWeight());
        }
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
        if(descriptionString=="") {
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
            chipGroup.removeAllViews();
            for (int i=0;i<categoryStrings.length;i++){
                if(categoryStrings[i].length()!=0){
                    Chip chip = new Chip(this);
                    Log.d(TAG, "displayProductInfo: " + categoryStrings[i]);
                    chip.setText(categoryStrings[i]);
                    chipGroup.addView(chip);
                }

            }
        }
        String infoString = "";
        if(dimensionsString!=null){
            infoString += "Dimensions:\n" + "\t" + dimensionsString + "\n";
        }
        if(weightString != null){
            infoString+= "Weight:\n" +"\t" + weightString + " grams\n";
        }
        if(photo!=null){
            productImage.setImageBitmap(photo);
        }
        info.setText(infoString);
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

    public void onBuyPressed(View view){
        final Product buyProduct = productDatabaseHelper.getProductFromID(productID);
        RelativeLayout linearLayout = new RelativeLayout(this);
        final NumberPicker aNumberPicker = new NumberPicker(this);
        int max = buyProduct.getTotal()-buyProduct.getPurchased();
        if(max<1){
            Toast.makeText(this, "This product can't be offered anymore", Toast.LENGTH_LONG).show();
            return;
        }
        aNumberPicker.setMaxValue(max);
        aNumberPicker.setMinValue(1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker,numPicerParams);

        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("How many items do you want to gift " + userDatabaseHelper.getUserFromID(receiverID).getFirstName() + "?");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                buyAmount(aNumberPicker.getValue(),buyProduct);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                buyAmount(0,null);
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    void buyAmount(int amount,Product buyProduct){
        if(amount==0){
            return;
        }else{
            buyProduct.setPurchased(buyProduct.getPurchased() + amount);
            productDatabaseHelper.updateProduct(buyProduct,productID);
            DateWish date = new DateWish();
            Date currentTime = Calendar.getInstance().getTime();
            date.setDateAndHour(currentTime);
            Purchase achat = new Purchase(userID,receiverID,productID,amount,date);
            purchaseDatabaseHelper.addPurchase(achat);
            displayProductInfo(buyProduct);
        }
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
                final Integer[] chosenWishlistID = {null};
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
                        chosenWishlistID[0] = which;
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
                    wishlistDatabaseHelper.addProduct(tempProductID,chosenWishlistID[0]);
                }
                displayProductInfo(productDatabaseHelper.getProductFromID(productID));
            }
        }

    }//onActivityResult

}
