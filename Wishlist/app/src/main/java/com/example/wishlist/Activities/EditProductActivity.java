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
    public void onBackPressed(View view) {

        final Intent returnIntent = new Intent();
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        returnIntent.putExtra("newProduct", -1);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        saveProduct(null);
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            productImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.edit_product);
        productImage = findViewById(R.id.productEditPhoto);
        newImage = findViewById(R.id.logoCamera);
        nameField = findViewById(R.id.newNameEdit);
        descriptionField = findViewById(R.id.newDescriptionEdit);
        categoriesField = findViewById(R.id.newCategoriesGroup);
        priceField = findViewById(R.id.newPrice);
        weightField = findViewById(R.id.newWeight);
        dimensionsXField = findViewById(R.id.dimensionsX);
        dimensionsYField = findViewById(R.id.dimensionsY);
        dimensionsZField = findViewById(R.id.dimensionsZ);
        desireField = findViewById(R.id.newDesire);
        amountPurchasedField = findViewById(R.id.newReceived);
        amountTotalField = findViewById(R.id.newTotal);
        categoriesList = resources.getStringArray(R.array.categories);
        deleteImage = findViewById(R.id.deleteImage);
        newImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                askPicture();
            }
        });
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productImage.getDrawable() != null){
                    productImage.setImageBitmap(null);
                }
            }
        });
        Intent intent = getIntent();
        productId = intent.getIntExtra("productID",-1);
        copyProduct = intent.getBooleanExtra("copyProduct",false);
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        if(productId ==-1){
            Log.d("BILIBU", "onCreate: NEW PRODUCT");
            newProduct =true;
            editCategories(new String[]{});
        }
        else{
            newProduct =false;
            product = productDatabaseHelper.getProductFromID(productId);
            editProduct(product);
        }

    }

    /**
     * Launches file explorer to choose a fitting image for the product
     */
    public void askPicture(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * Reads the different fields, checks whether they are valid, and writes/updates the product
     * in the database
     * @param view
     */
    public void saveProduct(View view){
        boolean error = false;
        String newName = null;
        final int wrongColor = resources.getColor(R.color.wrongInformation);
        final int trueColor = resources.getColor(R.color.white);
        if(nameField.getText().length()<=0) {
            error = true;
            nameField.setBackgroundColor(wrongColor);
        }else{
            newName= nameField.getText().toString();
            nameField.setBackgroundColor(trueColor);
        }
        String newDescription = descriptionField.getText().toString();
        String[] newCategories = checkedCategories.toArray(new String[0]);
        Integer newPrice = null;
        if(priceField.getText().length()<=0){
            error = true;
            priceField.setBackgroundColor(wrongColor);
        }else{
            priceField.setBackgroundColor(trueColor);
        newPrice = Integer.parseInt(priceField.getText().toString());
        }
        Integer newWeight = null;
        if(weightField.getText().length()>0){
            newWeight = Integer.parseInt(weightField.getText().toString());
        }

        String[] dimensionsArray = null;
        boolean noDimenX = dimensionsXField.getText().length()==0;
        boolean noDimenY = dimensionsYField.getText().length()==0;
        boolean noDimenZ = dimensionsZField.getText().length()==0;
        if(noDimenX&&noDimenY&&noDimenZ){
            dimensionsArray = new String[]{null, null, null};
        }
        else if((!noDimenX)&&(!noDimenY)&&(!noDimenZ)){
            dimensionsArray = new String[]{dimensionsXField.getText().toString(), dimensionsYField.getText().toString(), dimensionsZField.getText().toString()};
        }else{
            error = true;
            dimensionsXField.setBackgroundColor(wrongColor);
            dimensionsYField.setBackgroundColor(wrongColor);
            dimensionsZField.setBackgroundColor(wrongColor);
        }
        String newDimensions = null;
        if(!error){
            newDimensions = ProductDatabaseHelper.convertArrayToString(dimensionsArray);
        }

        int newDesire = (int) desireField.getRating();
        Integer newPurchased = null;
        if(amountPurchasedField.getText().length()!=0){
            newPurchased = Integer.parseInt(amountPurchasedField.getText().toString());
        }
        Integer newTotal = null;
        if(amountTotalField.getText().length()!=0){
            newTotal = Integer.parseInt(amountTotalField.getText().toString());
        }
        BitmapDrawable imageBitmapDrawable = (BitmapDrawable) productImage.getDrawable();
        Bitmap newImage = null;
        if(imageBitmapDrawable != null){
            newImage = (imageBitmapDrawable).getBitmap();
        }

        if(!error) {

            product = new Product(newName, newImage, newDescription, newCategories, newWeight, newPrice, newDesire, newDimensions, newTotal, newPurchased);
            if (!newProduct && !copyProduct) {
                productDatabaseHelper.updateProduct(product, productId);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                productId = productDatabaseHelper.addProduct(product);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                returnIntent.putExtra("newProduct", productId);
                finish();
            }
        }
    }

    /**
     * Reads the informations product and displays them in the corresponding fields
     * @param product non-null: the product whose information should be displayed
     */
    public void editProduct(Product product) {
        String productName = product.getName();
        Bitmap image = product.getPhoto();
        String description = product.getDescription();
        String[] categories = product.getCategory();
        int weight = product.getWeight();
        int price = product.getPrice();
        int desire = product.getDesire();
        String dimensions = product.getDimensions();
        int total = product.getTotal();
        int purchased = product.getPurchased();
        editCategories(categories);
        if(image!=null){
            productImage.setImageBitmap(image);
        }
        nameField.setText(productName);
        descriptionField.setText(description);
        priceField.setText(String.valueOf(price));
        desireField.setRating(desire);
        amountPurchasedField.setText(String.valueOf(purchased));
        amountTotalField.setText(String.valueOf(total));

        String[] dimensionsArray = ProductDatabaseHelper.convertStringToArray(dimensions);
        if(!dimensionsArray[0].equals("null")){
            dimensionsXField.setText(dimensionsArray[0]);
            dimensionsYField.setText(dimensionsArray[1]);
            dimensionsZField.setText(dimensionsArray[2]);
        }
        if(weight!=0){
            weightField.setText(String.valueOf(weight));
        }
    }

    /**
     * Adds the Strings in categories to the categoriesField chip group for categories,
     * checks the ones of the currently displayed product. When a chip is checked, it automatically
     * adds its string to the checkedCategories ArrayList, which is then read when writing to the
     * database.
     * @param categories
     */
    public void editCategories(String[] categories){
        checkedCategories = new ArrayList<String>(Arrays.asList(categories));
        for (String s : categoriesList) {
            Chip chip = new Chip(this);
            chip.setCheckable(true);
            chip.setText(s);
            if (checkedCategories.contains(s)) {
                chip.setChecked(true);
            }
            categoriesField.addView(chip);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if (isChecked) {
                        if (checkedCategories.contains(view.getText())) {

                        } else {
                            checkedCategories.add((String) view.getText());
                        }

                    } else {
                        if (checkedCategories.contains(view.getText())) {
                            checkedCategories.remove(view.getText());
                        } else {

                        }
                    }
                }
            });
        }
    }

}
