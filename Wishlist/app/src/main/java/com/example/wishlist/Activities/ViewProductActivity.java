package com.example.wishlist.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    protected void onCreate(final Bundle savedInstanceState) {
        this.productDatabaseHelper = new ProductDatabaseHelper(this.getApplicationContext());
        this.purchaseDatabaseHelper = new PurchaseDatabaseHelper(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        final Intent intent= this.getIntent();
        this.productID =intent.getIntExtra("productID",-1);
        final boolean myProduct = intent.getBooleanExtra("isMyProduct",true);
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        this.userID =prefs.getInt("userID",-1);
        if(myProduct){
            this.setContentView(R.layout.view_my_product);
            this.editButton = this.findViewById(R.id.editProduct);
            this.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ViewProductActivity.this.switchToEdit(productID);
                }
            });
            this.delButton = this.findViewById(R.id.deleteProduct);
            this.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ViewProductActivity.this.deleteProduct();
                }
            });
        }
        else{
            this.receiverID = intent.getIntExtra("receiverID",-1);
            this.setContentView(R.layout.view_one_product);
            this.addButton = this.findViewById(R.id.copyProduct);
            this.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ViewProductActivity.this.switchToAdd(productID);
                }
            });


        }


        this.description = this.findViewById(R.id.description);
        this.category = this.findViewById(R.id.category);
        this.info = this.findViewById(R.id.info);
        this.productName = this.findViewById(R.id.productName);
        this.desireBar = this.findViewById(R.id.rating);
        this.price = this.findViewById(R.id.priceTag);
        this.amountBought = this.findViewById(R.id.boughtAmount);
        this.productImage = this.findViewById(R.id.productPhoto);
        this.chipGroup = this.findViewById(R.id.categoriesGroup);


        Log.d(this.TAG, "onCreate: tempProductID = " + productID);
        this.displayProductInfo(this.productDatabaseHelper.getProductFromID(productID));
    }

    /**
     * Displays the given product's informations in the right fields, dynamically adjusts visibility
     * of set fields if their contents are null.
     * @param product
     */
    public void displayProductInfo(final Product product){
        Log.d(this.TAG, "displayProductInfo: bilibu updated");
        final String descriptionString = product.getDescription();
        final String name = product.getName();
        final String[] categoryStrings = product.getCategory();
        final String[] dimensionsStringArray = ProductDatabaseHelper.convertStringToArray(product.getDimensions());
        String dimensionsString = null;
        Log.d(this.TAG, "displayProductInfo:"+dimensionsStringArray[0]);
        if(!dimensionsStringArray[0].equals("null")){
            dimensionsString = dimensionsStringArray[0] + " by " + dimensionsStringArray[1] + " by " + dimensionsStringArray[2];
        }
        String weightString=null;
        if(product.getWeight()!=null){
            Log.d(this.TAG, "displayProductInfo: MAIS BORDEL ");
            weightString = Integer.toString(product.getWeight());
        }
        final Integer desire = product.getDesire();
        final String pricePoint = Integer.toString(product.getPrice());
        final String amount = Integer.toString(product.getTotal());
        final String purchased = Integer.toString(product.getPurchased());
        final Bitmap photo = product.getPhoto();
        if(name=="Undefined"){
            Toast.makeText(this, "Something went wrong:\n Missing name", Toast  .LENGTH_SHORT).show();
        }
        else{
            this.productName.setText(name);
        }
        this.price.setText(pricePoint + "€");
        if(descriptionString=="") {
            Log.d(this.TAG, "displayProductInfo: descriptionString was undefined");
            this.description.setVisibility(View.GONE);
        }
        else{
            this.description.setText(descriptionString);
        }
        if(categoryStrings.length==0) {
            this.category.setText("");
        }
        else{
            this.chipGroup.removeAllViews();
            for (int i=0;i<categoryStrings.length;i++){
                if(categoryStrings[i].length()!=0){
                    final Chip chip = new Chip(this);
                    Log.d(this.TAG, "displayProductInfo: " + categoryStrings[i]);
                    chip.setText(categoryStrings[i]);
                    this.chipGroup.addView(chip);
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
            this.productImage.setVisibility(View.VISIBLE);
            this.productImage.setImageBitmap(photo);
        }
        else{
            this.productImage.setVisibility(View.GONE);
        }
        this.info.setText(infoString);
        this.desireBar.setRating((float)desire);
        this.amountBought.setText("Amount Bought : " + purchased + " / " + amount);
    }

    /**
     * Starts the EditProductActivity to allow edition of the currently viewed product
     * @param pID the product to be edited
     */
    void switchToEdit(final int pID){
        final Intent intent=new Intent(this, EditProductActivity.class);
        intent.putExtra("productID",pID);
        this.startActivityForResult(intent,1);
    }

    /**
     * Starts the EditProductActivity to allow edition of currently viewed product, and then
     * allows addition of that newly edited product to one of the user's wishlists.
     * @param pID
     */
    void switchToAdd(final int pID){
        Log.d(this.TAG, "switchToAdd: pID is" + pID);
        final Intent intent = new Intent(this,EditProductActivity.class);
        intent.putExtra("productID",pID);
        intent.putExtra("copyProduct",true);
        this.startActivityForResult(intent,2);
    }

    /**
     * Ends the activity if the back button is pressed.
     * @param view
     */
    public void onBackPressed(final View view) {
        Intent returnIntent = new Intent();
        this.setResult(Activity.RESULT_OK,returnIntent);
        this.finish();
    }

    /**
     * Launches when the buy button is pressed in the toolbar. Asks the user how many products they
     * want to buy, based on how many were already offered by other users and the total amount the
     * owner of the product asked for.
     * @param view
     */
    public void onBuyPressed(final View view){
        final Product buyProduct = this.productDatabaseHelper.getProductFromID(this.productID);
        final RelativeLayout linearLayout = new RelativeLayout(this);
        final NumberPicker aNumberPicker = new NumberPicker(this);
        final int max = buyProduct.getTotal()-buyProduct.getPurchased();
        if(max<1){
            Toast.makeText(this, "This product can't be offered anymore", Toast.LENGTH_LONG).show();
            return;
        }
        aNumberPicker.setMaxValue(max);
        aNumberPicker.setMinValue(1);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        final RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(aNumberPicker,numPicerParams);

        final UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("How many items do you want to gift " + userDatabaseHelper.getUserFromID(this.receiverID).getFirstName() + "?");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                ViewProductActivity.this.buyAmount(aNumberPicker.getValue(),buyProduct);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                ViewProductActivity.this.buyAmount(0,null);
                                dialog.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Is called when the user presses "OK" on the dialog box when offering a product.
     * Accesses the database and updates the product view
     * @param amount the amount of products that were purchased
     * @param buyProduct the currently edited product, used to modify the item in the database
     */
    void buyAmount(final int amount, final Product buyProduct){
        if(amount==0){
            return;
        }else{
            buyProduct.setPurchased(buyProduct.getPurchased() + amount);
            this.productDatabaseHelper.updateProduct(buyProduct, this.productID);
            final DateWish date = new DateWish();
            final Date currentTime = Calendar.getInstance().getTime();
            date.setDateAndHour(currentTime);
            final Purchase achat = new Purchase(this.userID, this.receiverID, this.productID,amount,date);
            this.purchaseDatabaseHelper.addPurchase(achat);
            this.displayProductInfo(buyProduct);
        }
    }

    /**
     * Is called when the user presses the "delete" button in the toolbar. Removes the instances of
     * the product from its wishlist but not from the product database (kept for purchase history)
     */
    void deleteProduct(){
        this.wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
        this.wishlistDatabaseHelper.deleteProductInAllWishlist(this.productID);
        Intent returnIntent = new Intent();
        this.setResult(Activity.RESULT_OK,returnIntent);
        this.finish();
    }

    /**
     * Used when returning from EditProductActivity after editing a product
     * @param requestCode = 1 or 2, depending on the reason the activity was called
     * @param resultCode = RESULT_OK if the product was indeed edited (and not canceled)
     * @param data intent for transferring data from activity to activity
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                this.displayProductInfo(this.productDatabaseHelper.getProductFromID(this.productID));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Do nothing?
            }
        }
        else if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                final Wishlist[] chosenWishlist = {null};
                final Integer[] chosenWishlistID = {null};
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final int tempProductID = data.getIntExtra("newProduct",-1);
                if(tempProductID==-1){
                    return;
                }
                this.displayProductInfo(this.productDatabaseHelper.getProductFromID(tempProductID));
                builder.setTitle("Choose a wishlist");
                this.wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
                final ArrayList<Wishlist> wishlists = this.wishlistDatabaseHelper.getUserWishlist(this.userID);
                final String[] wishlistNames = new String[wishlists.size()];
                for (int i = 0; i < wishlists.size(); i++) {
                    wishlistNames[i] = wishlists.get(i).getName();
                    Log.d(this.TAG, "onActivityResult: wishlistNames" + wishlistNames[i]);
                }

                builder.setSingleChoiceItems(wishlistNames,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        chosenWishlist[0] = wishlists.get(which);
                        chosenWishlistID[0] = wishlists.get(which).getWishlistID();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        ViewProductActivity.this.confirmSelectList(chosenWishlistID[0],tempProductID);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    /**
     * Called from the dialog box that asks to choose a wishlist to copy the product to.
     * @param chosenWishlistID the ID of the selected wishlist.
     * @param productID the ID of the current product that shall be copied to the wishlist.
     */
    void confirmSelectList(final int chosenWishlistID, final int productID){
        if(chosenWishlistID==-1){
            Log.d(this.TAG, "onActivityResult: chosenWishlist null");
        }
        else{
            Log.d(this.TAG, "onActivityResult: " + productID);
            this.wishlistDatabaseHelper.addProduct(productID,chosenWishlistID);
        }
        this.displayProductInfo(this.productDatabaseHelper.getProductFromID(productID));
    }

}
