package com.example.wishlist.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.example.wishlist.Class.CategoriesAdapter;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This is the activity that is executed whenever the user edits or creates a new product
 */
public class EditProductActivity extends AppCompatActivity {
//    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(null,EditProductActivity.this);
    int productId;
    Product product;
    private boolean newProduct;
    private boolean copyProduct;

    Resources resources;

    private ProductDatabaseHelper productDatabaseHelper;
    private CircleImageView productImage;
    private ImageView newImage;
    private ImageView deleteImage;
    private EditText nameField;
    private EditText descriptionField;
    private ChipGroup categoriesField;
    private EditText priceField;
    private EditText weightField;
    private EditText dimensionsXField;
    private EditText dimensionsYField;
    private EditText dimensionsZField;
    private RatingBar desireField;
    private EditText amountPurchasedField;
    private EditText amountTotalField;
    private String[] categoriesList;

    private static final int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> checkedCategories;

    /**
     * Asks the user if they want to save their changes/their product
     * @param view
     */
    public void onBackPressed(final View view) {

        final Intent returnIntent = new Intent();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        returnIntent.putExtra("newProduct", -1);
                        EditProductActivity.this.finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        EditProductActivity.this.saveProduct(null);
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quit without saving?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).setNeutralButton("Save",dialogClickListener).show();
    }

    /**
     * Sets the image chosen by the user in their own files in the productImage viewer
     * @param requestCode, identifies the request type that the activity result comes from
     * @param resultCode should be RESULT_OK if the image was successfully chosen
     * @param data image URI container
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditProductActivity.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            final Uri selectedImage = data.getData();
            final String[] filePathColumn = { MediaStore.Images.Media.DATA };

            final Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();
            this.productImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resources = this.getResources();
        this.setContentView(R.layout.edit_product);
        this.productImage = this.findViewById(R.id.productEditPhoto);
        this.newImage = this.findViewById(R.id.logoCamera);
        this.nameField = this.findViewById(R.id.newNameEdit);
        this.descriptionField = this.findViewById(R.id.newDescriptionEdit);
        this.categoriesField = this.findViewById(R.id.newCategoriesGroup);
        this.priceField = this.findViewById(R.id.newPrice);
        this.weightField = this.findViewById(R.id.newWeight);
        this.dimensionsXField = this.findViewById(R.id.dimensionsX);
        this.dimensionsYField = this.findViewById(R.id.dimensionsY);
        this.dimensionsZField = this.findViewById(R.id.dimensionsZ);
        this.desireField = this.findViewById(R.id.newDesire);
        this.amountPurchasedField = this.findViewById(R.id.newReceived);
        this.amountTotalField = this.findViewById(R.id.newTotal);
        this.categoriesList = this.resources.getStringArray(R.array.categories);
        this.deleteImage = this.findViewById(R.id.deleteImage);
        this.newImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {
                EditProductActivity.this.askPicture();
            }
        });
        this.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (EditProductActivity.this.productImage.getDrawable() != null){
                    EditProductActivity.this.productImage.setImageBitmap(null);
                }
            }
        });
        final Intent intent = this.getIntent();
        this.productId = intent.getIntExtra("productID",-1);
        this.copyProduct = intent.getBooleanExtra("copyProduct",false);
        this.productDatabaseHelper = new ProductDatabaseHelper(this.getApplicationContext());
        if(this.productId ==-1){
            Log.d("BILIBU", "onCreate: NEW PRODUCT");
            this.newProduct =true;
            this.editCategories(new String[]{});
        }
        else{
            this.newProduct =false;
            this.product = this.productDatabaseHelper.getProductFromID(this.productId);
            this.editProduct(this.product);
        }

    }

    /**
     * Launches file explorer to choose a fitting image for the product
     */
    public void askPicture(){
        final Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        this.startActivityForResult(i, EditProductActivity.RESULT_LOAD_IMAGE);
    }

    /**
     * Reads the different fields, checks whether they are valid, and writes/updates the product
     * in the database
     * @param view
     */
    public void saveProduct(final View view){
        boolean error = false;
        String newName = null;
        int wrongColor = this.resources.getColor(R.color.wrongInformation);
        int trueColor = this.resources.getColor(R.color.white);
        if(this.nameField.getText().length()<=0) {
            error = true;
            this.nameField.setBackgroundColor(wrongColor);
        }else{
            newName= this.nameField.getText().toString();
            this.nameField.setBackgroundColor(trueColor);
        }
        final String newDescription = this.descriptionField.getText().toString();
        final String[] newCategories = this.checkedCategories.toArray(new String[0]);
        Integer newPrice = null;
        if(this.priceField.getText().length()<=0){
            error = true;
            this.priceField.setBackgroundColor(wrongColor);
        }else{
            this.priceField.setBackgroundColor(trueColor);
        newPrice = Integer.parseInt(this.priceField.getText().toString());
        }
        Integer newWeight = null;
        if(this.weightField.getText().length()>0){
            newWeight = Integer.parseInt(this.weightField.getText().toString());
        }

        String[] dimensionsArray = null;
        final boolean noDimenX = this.dimensionsXField.getText().length()==0;
        final boolean noDimenY = this.dimensionsYField.getText().length()==0;
        final boolean noDimenZ = this.dimensionsZField.getText().length()==0;
        if(noDimenX&&noDimenY&&noDimenZ){
            dimensionsArray = new String[]{null, null, null};
        }
        else if((!noDimenX)&&(!noDimenY)&&(!noDimenZ)){
            dimensionsArray = new String[]{this.dimensionsXField.getText().toString(), this.dimensionsYField.getText().toString(), this.dimensionsZField.getText().toString()};
        }else{
            error = true;
            this.dimensionsXField.setBackgroundColor(wrongColor);
            this.dimensionsYField.setBackgroundColor(wrongColor);
            this.dimensionsZField.setBackgroundColor(wrongColor);
        }
        String newDimensions = null;
        if(!error){
            newDimensions = ProductDatabaseHelper.convertArrayToString(dimensionsArray);
        }

        final int newDesire = (int) this.desireField.getRating();
        Integer newPurchased = null;
        if(this.amountPurchasedField.getText().length()!=0){
            newPurchased = Integer.parseInt(this.amountPurchasedField.getText().toString());
        }
        Integer newTotal = null;
        if(this.amountTotalField.getText().length()!=0){
            newTotal = Integer.parseInt(this.amountTotalField.getText().toString());
        }
        final BitmapDrawable imageBitmapDrawable = (BitmapDrawable) this.productImage.getDrawable();
        Bitmap newImage = null;
        if(imageBitmapDrawable != null){
            newImage = (imageBitmapDrawable).getBitmap();
        }

        if(!error) {

            this.product = new Product(newName, newImage, newDescription, newCategories, newWeight, newPrice, newDesire, newDimensions, newTotal, newPurchased);
            if (!this.newProduct && !this.copyProduct) {
                this.productDatabaseHelper.updateProduct(this.product, this.productId);
                final Intent returnIntent = new Intent();
                this.setResult(Activity.RESULT_OK, returnIntent);
                this.finish();
            } else {
                this.productId = this.productDatabaseHelper.addProduct(this.product);
                final Intent returnIntent = new Intent();
                this.setResult(Activity.RESULT_OK, returnIntent);
                returnIntent.putExtra("newProduct", this.productId);
                this.finish();
            }
        }
    }

    /**
     * Reads the informations product and displays them in the corresponding fields
     * @param product non-null: the product whose information should be displayed
     */
    public void editProduct(final Product product) {
        final String productName = product.getName();
        final Bitmap image = product.getPhoto();
        final String description = product.getDescription();
        final String[] categories = product.getCategory();
        final int weight = product.getWeight();
        final int price = product.getPrice();
        final int desire = product.getDesire();
        final String dimensions = product.getDimensions();
        final int total = product.getTotal();
        final int purchased = product.getPurchased();
        this.editCategories(categories);
        if(image!=null){
            this.productImage.setImageBitmap(image);
        }
        this.nameField.setText(productName);
        this.descriptionField.setText(description);
        this.priceField.setText(String.valueOf(price));
        this.desireField.setRating(desire);
        this.amountPurchasedField.setText(String.valueOf(purchased));
        this.amountTotalField.setText(String.valueOf(total));

        final String[] dimensionsArray = ProductDatabaseHelper.convertStringToArray(dimensions);
        if(!dimensionsArray[0].equals("null")){
            this.dimensionsXField.setText(dimensionsArray[0]);
            this.dimensionsYField.setText(dimensionsArray[1]);
            this.dimensionsZField.setText(dimensionsArray[2]);
        }
        if(weight!=0){
            this.weightField.setText(String.valueOf(weight));
        }
    }

    /**
     * Adds the Strings in categories to the categoriesField chip group for categories,
     * checks the ones of the currently displayed product. When a chip is checked, it automatically
     * adds its string to the checkedCategories ArrayList, which is then read when writing to the
     * database.
     * @param categories
     */
    public void editCategories(final String[] categories){
        this.checkedCategories = new ArrayList<String>(Arrays.asList(categories));
        for (final String s : this.categoriesList) {
            final Chip chip = new Chip(this);
            chip.setCheckable(true);
            chip.setText(s);
            if (this.checkedCategories.contains(s)) {
                chip.setChecked(true);
            }
            this.categoriesField.addView(chip);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton view, final boolean isChecked) {
                    if (isChecked) {
                        if (EditProductActivity.this.checkedCategories.contains(view.getText())) {

                        } else {
                            EditProductActivity.this.checkedCategories.add((String) view.getText());
                        }

                    } else {
                        if (EditProductActivity.this.checkedCategories.contains(view.getText())) {
                            EditProductActivity.this.checkedCategories.remove(view.getText());
                        } else {

                        }
                    }
                }
            });
        }
    }

}
