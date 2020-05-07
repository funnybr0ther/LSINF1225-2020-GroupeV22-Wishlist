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

public class CreateProfileActivity extends AppCompatActivity implements AddPhotoDialog.OnPhotoReceivedListener {
    private String email;
    private String password;
    private final int MY_PERMISSIONS_REQUEST = 37;
    private Bitmap image;
    private CircleImageView profilePhoto;
    private ImageView cameraLogo;
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



    @TargetApi(21)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_profile);

        //Get information pass with Extra in intent
        final Intent intent= this.getIntent();
        this.email =intent.getStringExtra("mail");
        this.password =intent.getStringExtra("password");

        //Set the different View/Edit/Spinner variable
        this.profilePhoto = this.findViewById(R.id.profilePhoto);
        this.cameraLogo = this.findViewById(R.id.logoCamera);
        this.editTextFirstName = this.findViewById(R.id.newmail);
        this.editTextLastName = this.findViewById(R.id.getLastName);
        this.editTextAddressLine1 = this.findViewById(R.id.getAddressLine1);
        this.editTextAddressLine2 = this.findViewById(R.id.getAddressLine2);
        this.editTextCity = this.findViewById(R.id.getLocality);
        this.editTextCountry = this.findViewById(R.id.getCountry);
        this.editTextPostalCode = this.findViewById(R.id.getPostalCode);
        this.spinnerSize = this.findViewById(R.id.spinnerSize);
        this.spinnerShoeSize = this.findViewById(R.id.spinnerShoeSize);
        this.spinnerFavoriteColor = this.findViewById(R.id.spinnerFavoriteColor);
        this.spinnerDay = this.findViewById(R.id.spinnerDay);
        this.spinnerMonth = this.findViewById(R.id.spinnerMonth);
        this.spinnerYear = this.findViewById(R.id.spinnerYear);

        //Set the default photo
        if(this.image ==null){
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }

        //Set function onclick of camera logo
        this.cameraLogo.setOnClickListener(new View.OnClickListener() {
            //Check permission to take photo and access storage then create ChangePhotoDialog
            @Override
            public void onClick(final View v) {
                //check permission
                final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                for (int i=0;i<2;i++) {
                    if (ContextCompat.checkSelfPermission(CreateProfileActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{permissions[i]},
                                CreateProfileActivity.this.MY_PERMISSIONS_REQUEST);
                    } else {
                        if(i==1){
                            final AddPhotoDialog dialog = new AddPhotoDialog();
                            dialog.show(getSupportFragmentManager(), "ha");
                        }
                    }
                }
            }

        });
    }

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }

    public boolean checkDate(final int day, final String month, final int year) {
        if (day == 29 && month.equals( "February")) return year % 4 == 0;
        if (day > 29 && month.equals("February")) return false;
        if (day == 31) {
            return (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November"));
        }
        return true;
    }

    /*
    *Check if the string without strange character as #/? is not empty
     */
    public boolean checkStringIsCorrect(String str) {
        str = str.replaceAll("[^\\w]", "");
        return str.length() > 0;
    }

    /*
     * Function called onclick of check mark
     * Firstly we collect the information given by the user
     * Then we check if nothing necessary is missing and if birthdate is ok
     * If something goes wrong we stay at this activity and put some information to help the user
     * If nothing goes wrong we go to the main menu
     */
    public void createUser(final View view) throws Exception{
        int numberError = 0;
        //Get the information
        final String firstName = this.editTextFirstName.getText().toString();
        final String lastName = this.editTextLastName.getText().toString();
        final String addressLine1 = this.editTextAddressLine1.getText().toString();
        final String addressLine2 = this.editTextAddressLine2.getText().toString();
        final String city = this.editTextCity.getText().toString();
        final String country = this.editTextCountry.getText().toString();
        int postalCode=0;
        try {
            postalCode = Integer.parseInt(this.editTextPostalCode.getText().toString());
            this.editTextPostalCode.setBackgroundColor(Color.rgb(255, 255, 255));
        } catch (final NumberFormatException e) {
            this.editTextPostalCode.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        }

        final String size = this.spinnerSize.getSelectedItem().toString();
        final String shoeSize = this.spinnerShoeSize.getSelectedItem().toString();
        final String favoriteColor = this.spinnerFavoriteColor.getSelectedItem().toString();
        final String day = this.spinnerDay.getSelectedItem().toString();
        final String month = this.spinnerMonth.getSelectedItem().toString();
        final String year = this.spinnerYear.getSelectedItem().toString();

        //Check if any required is missing
        if (!this.checkStringIsCorrect(firstName)) {
            this.editTextFirstName.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            this.editTextFirstName.setBackgroundColor(this.getResources().getColor(R.color.design_default_color_background));
        }
        if (!this.checkStringIsCorrect(lastName)) {
            this.editTextLastName.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            this.editTextLastName.setBackgroundColor(this.getResources().getColor(R.color.design_default_color_background));
        }
        if (!this.checkStringIsCorrect(addressLine1)) {
            this.editTextAddressLine1.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            this.editTextAddressLine1.setBackgroundColor(this.getResources().getColor(R.color.design_default_color_background));
        }
        if (!this.checkStringIsCorrect(city)) {
            this.editTextCity.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            this.editTextCity.setBackgroundColor(this.getResources().getColor(R.color.design_default_color_background));
        }
        if (!this.checkStringIsCorrect(country)) {
            this.editTextCountry.setBackgroundColor(this.getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            this.editTextCountry.setBackgroundColor(this.getResources().getColor(R.color.design_default_color_background));
        }

        //Check the birthDate
        final TextView wrongDate = this.findViewById(R.id.wrongBirthdate);
        int yearInt=-1;
        int dayInt=-1;

        if (year.equals("Year") || day.equals("Day") || month.equals("Month")) {
            wrongDate.setText(this.getResources().getText(R.string.wrongDate));
            numberError++;
        } else {
            yearInt = Integer.parseInt(year);
            dayInt = Integer.parseInt(day);
            if (!this.checkDate(dayInt, month, yearInt)) {
                wrongDate.setText(this.getResources().getText(R.string.wrongDate));
                numberError++;
            } else {
                wrongDate.setText("");
            }
        }

        if (numberError == 0) {
            //Create an Address
            final Address userAddress=new Address(addressLine1,city,country,postalCode);
            if(this.checkStringIsCorrect(addressLine2)) userAddress.setAddressLine2(addressLine2);
            //Create a DateWish with representing birthdate
            final DateWish birthdate=new DateWish(dayInt,month,yearInt);
            //create an User
            final User user=new User(userAddress,firstName,lastName, this.email,birthdate, this.password);
            if (!favoriteColor.equals("Undefined"))user.setFavoriteColor(favoriteColor);
            if (!size.equals("Undefined")) user.setSize(size);
            if(this.image !=null) user.setProfilePhoto(this.image);
            if (!shoeSize.equals("Undefined")) user.setShoeSize(shoeSize);
            final UserDatabaseHelper dbHelper= new UserDatabaseHelper(this.getApplicationContext());
            if(dbHelper.addUser(user)){
                final int userID=dbHelper.checkUser(this.email, this.password);
                if(userID==-1){
                    final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    final Toast toast=Toast.makeText(this,"Account Create",Toast.LENGTH_SHORT);
                    toast.show();
                    final SharedPreferences prefs = getSharedPreferences(
                            "com.example.app", Context.MODE_PRIVATE);
                    prefs.edit().putInt("userID",userID).apply();
                    final Intent intent=new Intent(this,MainMenuActivity.class);
                    this.startActivity(intent);
                }
            }else {
                final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            final Toast toast=Toast.makeText(this,"The information with a * are required",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void getBitmapImage(final Bitmap bitmap) {
        if(bitmap!=null){
            this.profilePhoto.setImageBitmap(bitmap);
            this.image =bitmap;
        }
    }

    @Override
    public void getUriImage(final Uri uri) {
        if(uri!=null){
            try{
                this.image = ImageHelper.compress(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                this.profilePhoto.setImageBitmap(this.image);
            }
            catch (final Exception e){
                final Toast toast=Toast.makeText(this,"something went wrong with the image",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}

