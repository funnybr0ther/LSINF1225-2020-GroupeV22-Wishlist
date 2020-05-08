package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.ImageHelper;
import com.example.wishlist.Class.UserDatabaseHelper;

import com.example.wishlist.Class.DateWish;

import com.example.wishlist.Fragment.AddPhotoDialog;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 *  Need to be start with password and email pass in extra of its intent
 */
public class CreateProfileActivity extends AppCompatActivity implements AddPhotoDialog.OnPhotoReceivedListener {
    private String email;
    private String password;
    private final int MY_PERMISSIONS_REQUEST = 37;
    private Bitmap image;
    private CircleImageView profilePhoto;
    private EditText editTextFirstName;
    private  EditText editTextLastName;
    private EditText editTextAddressLine1;
    private EditText editTextAddressLine2;
    private EditText editTextCity;
    private EditText editTextCountry;
    private EditText editTextPostalCode;
    private Spinner spinnerSize;
    private Spinner spinnerShoeSize;
    private Spinner spinnerFavoriteColor;
    private Spinner spinnerDay;
    private Spinner spinnerMonth;
    private Spinner spinnerYear;


    /**
     * Get the user mail and password pass with extra to the intent
     * Assign the views to their global variables
     * Set the onClickListener of the camera logo
     * @param savedInstanceState
     */
    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        //Get information pass with Extra in intent
        Intent intent= getIntent();
        email =intent.getStringExtra("mail");
        password =intent.getStringExtra("password");

        //Set the different View/Edit/Spinner variable
        profilePhoto = findViewById(R.id.profilePhoto);
        ImageView cameraLogo = findViewById(R.id.logoCamera);
        editTextFirstName = findViewById(R.id.newmail);
        editTextLastName = findViewById(R.id.getLastName);
        editTextAddressLine1 = findViewById(R.id.getAddressLine1);
        editTextAddressLine2 = findViewById(R.id.getAddressLine2);
        editTextCity = findViewById(R.id.getLocality);
        editTextCountry = findViewById(R.id.getCountry);
        editTextPostalCode = findViewById(R.id.getPostalCode);
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerShoeSize = findViewById(R.id.spinnerShoeSize);
        spinnerFavoriteColor = findViewById(R.id.spinnerFavoriteColor);
        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);

        //Set the default photo
        if(image ==null){
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));
        }

        //Set function onclick of camera logo
        cameraLogo.setOnClickListener(new View.OnClickListener() {
            //Check permission to take photo and access storage then create ChangePhotoDialog
            @Override
            public void onClick(View v) {
                //check permission
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                for (int i=0;i<2;i++) {
                    if (ContextCompat.checkSelfPermission(CreateProfileActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{permissions[i]},
                                MY_PERMISSIONS_REQUEST);
                    } else {
                        if(i==1){
                            AddPhotoDialog dialog = new AddPhotoDialog();
                            dialog.show(CreateProfileActivity.this.getSupportFragmentManager(), "ha");
                        }
                    }
                }
            }

        });
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

    /**
     * Check if the date is correct
     * @param day
     * @param month
     * @param year
     * @return boolean
     */
    public boolean checkDate(int day, String month, int year) {
        if (day == 29 && month.equals( "February")) return year % 4 == 0;
        if (day > 29 && month.equals("February")) return false;
        if (day == 31) {
            return (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November"));
        }
        return true;
    }

    /**
     * Check if the string without strange character as #/? is not empty
     * @param str
     * @return boolean
     */
    public boolean checkStringIsCorrect(String str) {
        str = str.replaceAll("[^\\w]", "");
        return str.length() > 0;
    }

    /**
     * Firstly we collect the information given by the user
     * Then we check if nothing necessary is missing and if birthdate is ok
     * If something goes wrong stay at this activity and put some information to help the user
     * If nothing goes wrong go to the main menu and put the userID in shared preference
     * @param view
     */
    public void createUser(View view) {
        int numberError = 0;
        //Get the information
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String addressLine1 = editTextAddressLine1.getText().toString();
        String addressLine2 = editTextAddressLine2.getText().toString();
        String city = editTextCity.getText().toString();
        String country = editTextCountry.getText().toString();
        int postalCode=0;
        try {
            postalCode = Integer.parseInt(editTextPostalCode.getText().toString());
            editTextPostalCode.setBackgroundColor(Color.rgb(255, 255, 255));
        } catch (NumberFormatException e) {
            editTextPostalCode.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        }

        String size = spinnerSize.getSelectedItem().toString();
        String shoeSize = spinnerShoeSize.getSelectedItem().toString();
        String favoriteColor = spinnerFavoriteColor.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        //Check if any required is missing
        if (!checkStringIsCorrect(firstName)) {
            editTextFirstName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextFirstName.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }
        if (!checkStringIsCorrect(lastName)) {
            editTextLastName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextLastName.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }
        if (!checkStringIsCorrect(addressLine1)) {
            editTextAddressLine1.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextAddressLine1.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }
        if (!checkStringIsCorrect(city)) {
            editTextCity.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCity.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }
        if (!checkStringIsCorrect(country)) {
            editTextCountry.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCountry.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }

        //Check the birthDate
        TextView wrongDate = findViewById(R.id.wrongBirthdate);
        int yearInt=-1;
        int dayInt=-1;

        if (year.equals("Year") || day.equals("Day") || month.equals("Month")) {
            wrongDate.setText(getResources().getText(R.string.wrongDate));
            numberError++;
        } else {
            yearInt = Integer.parseInt(year);
            dayInt = Integer.parseInt(day);
            if (!checkDate(dayInt, month, yearInt)) {
                wrongDate.setText(getResources().getText(R.string.wrongDate));
                numberError++;
            } else {
                wrongDate.setText("");
            }
        }

        if (numberError == 0) {
            //Create an Address
            Address userAddress=new Address(addressLine1,city,country,postalCode);
            if(checkStringIsCorrect(addressLine2)) userAddress.setAddressLine2(addressLine2);
            //Create a DateWish with representing birthdate
            DateWish birthdate=new DateWish(dayInt,month,yearInt);
            //create an User
            User user=new User(userAddress,firstName,lastName, email,birthdate, password);
            if (!favoriteColor.equals("Undefined"))user.setFavoriteColor(favoriteColor);
            if (!size.equals("Undefined")) user.setSize(size);
            if(image !=null) user.setProfilePhoto(image);
            if (!shoeSize.equals("Undefined")) user.setShoeSize(shoeSize);
            UserDatabaseHelper dbHelper= new UserDatabaseHelper(getApplicationContext());
            if(dbHelper.addUser(user)){
                int userID=dbHelper.checkUser(email, password);
                if(userID==-1){
                    Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast=Toast.makeText(this,"Account Create",Toast.LENGTH_SHORT);
                    toast.show();
                    SharedPreferences prefs = this.getSharedPreferences(
                            "com.example.app", Context.MODE_PRIVATE);
                    prefs.edit().putInt("userID",userID).apply();
                    Intent intent=new Intent(this,MainMenuActivity.class);
                    startActivity(intent);
                }
            }else {
                Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast=Toast.makeText(this,"The information with a * are required",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Set bitmap if it's not null in profile photo view and in global variable image
     * @param bitmap image to set in profile photo
     */
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if(bitmap!=null){
            profilePhoto.setImageBitmap(bitmap);
            image =bitmap;
        }
    }

    /**
     * Transform the uri image to bitmap
     * Set  this bitmap if it's not null in profile photo view and in global variable image
     * @param uri image to set in profile photo
     */
    @Override
    public void getUriImage(Uri uri) {
        if(uri!=null){
            try{
                image = ImageHelper.compress(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                profilePhoto.setImageBitmap(image);
            }
            catch (Exception e){
                Toast toast=Toast.makeText(this,"something went wrong with the image",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}

