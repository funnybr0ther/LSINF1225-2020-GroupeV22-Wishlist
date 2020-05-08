package com.example.wishlist.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ImageButton delButton;


    int productID;
    int userID;
    int receiverID;

    ProductDatabaseHelper productDatabaseHelper;
    PurchaseDatabaseHelper purchaseDatabaseHelper;
    WishlistDatabaseHelper wishlistDatabaseHelper;

    /**
     * Reads given intents to determine the product to be displayed, its owner and the current user
     * of the app. The toolbar is adapted so that the current user can either copy and offer
     * someone else's product, or edit and delete their own product.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        purchaseDatabaseHelper = new PurchaseDatabaseHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        productID =intent.getIntExtra("productID",-1);
        boolean myProduct = intent.getBooleanExtra("isMyProduct",true);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        userID =prefs.getInt("userID",-1);
        if(myProduct){
            setContentView(R.layout.view_my_product);
            editButton = findViewById(R.id.editProduct);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToEdit(ViewProductActivity.this.productID);
                }
            });
            delButton = findViewById(R.id.deleteProduct);
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteProduct();
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

    /**
     * Displays the given product's informations in the right fields, dynamically adjusts visibility
     * of set fields if their contents are null.
     * @param product
     */
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
            productImage.setVisibility(View.VISIBLE);
            productImage.setImageBitmap(photo);
        }
        else{
            productImage.setVisibility(View.GONE);
        }
        info.setText(infoString);
        desireBar.setRating((float)desire);
        amountBought.setText("Amount Bought : " + purchased + " / " + amount);
    }

    /**
     * Starts the EditProductActivity to allow edition of the currently viewed product
     * @param pID the product to be edited
     */
    void switchToEdit(int pID){
        Intent intent=new Intent(this, EditProductActivity.class);
        intent.putExtra("productID",pID);
        startActivityForResult(intent,1);
    }

    /**
     * Starts the EditProductActivity to allow edition of currently viewed product, and then
     * allows addition of that newly edited product to one of the user's wishlists.
     * @param pID
     */
    void switchToAdd(int pID){
        Log.d(TAG, "switchToAdd: pID is" + pID);
        Intent intent = new Intent(this,EditProductActivity.class);
        intent.putExtra("productID",pID);
        intent.putExtra("copyProduct",true);
        startActivityForResult(intent,2);
    }

    /**
     * Ends the activity if the back button is pressed.
     * @param view
     */
    public void onBackPressed(View view) {
        final Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    /**
     * Launches when the buy button is pressed in the toolbar. Asks the user how many products they
     * want to buy, based on how many were already offered by other users and the total amount the
     * owner of the product asked for.
     * @param view
     */
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
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker,numPicerParams);

        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("How many items do you want to gift " + userDatabaseHelper.getUserFromID(receiverID).getFirstName() + "?");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
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

    /**
     * Is called when the user presses "OK" on the dialog box when offering a product.
     * Accesses the database and updates the product view
     * @param amount the amount of products that were purchased
     * @param buyProduct the currently edited product, used to modify the item in the database
     */
    void buyAmount(int amount, Product buyProduct){
        if(amount==0){
            return;
        }else{
            buyProduct.setPurchased(buyProduct.getPurchased() + amount);
            productDatabaseHelper.updateProduct(buyProduct, productID);
            DateWish date = new DateWish();
            Date currentTime = Calendar.getInstance().getTime();
            date.setDateAndHour(currentTime);
            Purchase achat = new Purchase(userID, receiverID, productID,amount,date);
            purchaseDatabaseHelper.addPurchase(achat);
            displayProductInfo(buyProduct);
        }
    }

    /**
     * Is called when the user presses the "delete" button in the toolbar. Removes the instances of
     * the product from its wishlist but not from the product database (kept for purchase history)
     */
    void deleteProduct(){
        wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
        wishlistDatabaseHelper.deleteProductInAllWishlist(productID);
        final Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    /**
     * Used when returning from EditProductActivity after editing a product
     * @param requestCode = 1 or 2, depending on the reason the activity was called
     * @param resultCode = RESULT_OK if the product was indeed edited (and not canceled)
     * @param data intent for transferring data from activity to activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                displayProductInfo(productDatabaseHelper.getProductFromID(productID));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Do nothing?
            }
        }
        else if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                final Wishlist[] chosenWishlist = {null};
                final Integer[] chosenWishlistID = {-1};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final int tempProductID = data.getIntExtra("newProduct",-1);
                if(tempProductID==-1){
                    return;
                }
                displayProductInfo(productDatabaseHelper.getProductFromID(tempProductID));
                builder.setTitle("Choose a wishlist");
                wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
                final ArrayList<Wishlist> wishlists = wishlistDatabaseHelper.getUserWishlist(userID);
                String[] wishlistNames = new String[wishlists.size()];
                for (int i = 0; i < wishlists.size(); i++) {
                    wishlistNames[i] = wishlists.get(i).getName();
                    Log.d(TAG, "onActivityResult: wishlistNames" + wishlistNames[i]);
                }

                builder.setSingleChoiceItems(wishlistNames,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenWishlist[0] = wishlists.get(which);
                        chosenWishlistID[0] = wishlists.get(which).getWishlistID();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmSelectList(chosenWishlistID[0],tempProductID);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    /**
     * Called from the dialog box that asks to choose a wishlist to copy the product to.
     * @param chosenWishlistID the ID of the selected wishlist.
     * @param productID the ID of the current product that shall be copied to the wishlist.
     */
    void confirmSelectList(Integer chosenWishlistID, Integer productID){
        if(chosenWishlistID==-1){
            Log.d(TAG, "onActivityResult: chosenWishlist null");
        }
        else{
            Log.d(TAG, "onActivityResult: " + productID);
            wishlistDatabaseHelper.addProduct(productID,chosenWishlistID);
        }
        displayProductInfo(productDatabaseHelper.getProductFromID(productID));
    }

}
