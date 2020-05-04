package com.example.wishlist.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.wishlist.Class.CategoriesAdapter;
import com.example.wishlist.Class.Product;
import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProductActivity extends AppCompatActivity {
//    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(null,EditProductActivity.this);
    long productId;
    Product product;
    private ProductDatabaseHelper productDatabaseHelper;
    private ImageButton saveProduct;
    private CircleImageView productImage;
    private ImageView newImage;
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

    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> checkedCategories;

    public void onBackPressed(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("modifications",true);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            productImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        saveProduct = findViewById(R.id.validateEditProduct);
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
        categoriesList = getResources().getStringArray(R.array.categories);
        newImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Intent intent = getIntent();
        productId = intent.getLongExtra("productID",-1);
        productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        if(productId==-1){
            String TAG="bill";
        }
        product = productDatabaseHelper.getProductFromID(productId);
        editProduct(product);
    }

    public void saveProductModif(View view){
        String newName = nameField.getText().toString();
        String newDescription = descriptionField.getText().toString();
        String[] newCategories = checkedCategories.toArray(new String[0]);
        int newPrice = Integer.parseInt(priceField.getText().toString());
        int newWeight = Integer.parseInt(weightField.getText().toString());
        String[] dimensionsArray = new String[]{dimensionsXField.getText().toString(),dimensionsYField.getText().toString(),dimensionsZField.getText().toString()};
        String newDimensions = ProductDatabaseHelper.convertArrayToString(dimensionsArray);
        int newDesire = (int) desireField.getRating();
        int newPurchased = Integer.parseInt(amountPurchasedField.getText().toString());
        int newTotal = Integer.parseInt(amountTotalField.getText().toString());
        BitmapDrawable imageBitmapDrawable = (BitmapDrawable) productImage.getDrawable();
        Bitmap newImage = null;
        if(imageBitmapDrawable != null){
            newImage = (imageBitmapDrawable).getBitmap();
        }
        product = new Product(newName,newImage,newDescription,newCategories,newWeight,newPrice,newDesire,newDimensions,newTotal,newPurchased);
        productDatabaseHelper.updateProduct(product,productId);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("modifications",true);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

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

        checkedCategories = new ArrayList<String>(Arrays.asList(categories));
        if(image!=null){
        productImage.setImageBitmap(image);
        }
        nameField.setText(productName);
        descriptionField.setText(description);
        for (int i = 0; i < categoriesList.length; i++) {
            Chip chip = new Chip(this);
            chip.setCheckable(true);
            chip.setText(categoriesList[i]);
            if (checkedCategories.contains(categoriesList[i])) {
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
                            checkedCategories.remove((String) view.getText());
                        } else {

                        }
                    }
                }
            });
        }
        priceField.setText(String.valueOf(price));
        desireField.setRating(desire);
        amountPurchasedField.setText(String.valueOf(purchased));
        amountTotalField.setText(String.valueOf(total));

        String[] dimensionsArray = ProductDatabaseHelper.convertStringToArray(dimensions);
        if(weight!=0){
            weightField.setText(String.valueOf(weight));
        }
    }
        public void newProduct(){

        }

}
