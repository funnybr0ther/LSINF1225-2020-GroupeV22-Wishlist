package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.DatabaseHelper;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Fragment.ChangePhotoDialog;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements ChangePhotoDialog.OnPhotoReceivedListener {
    private String email;
    private String password;
    private final int MY_PERMISSIONS_REQUEST = 37;
    private String imagePath;
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




   /* @Override//TODO Check if possible to remove that -> done I don't see any difference
     *Je le laisse ici au cas où
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }*/

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        //Get information pass with Extra in intent
        Intent intent=getIntent();
        email=intent.getStringExtra("mail");
        password=intent.getStringExtra("password");

        //Set the different View/Edit/Spinner variable
        profilePhoto= findViewById(R.id.profilePhoto);
        cameraLogo = findViewById(R.id.logoCamera);
        editTextFirstName = (EditText) findViewById(R.id.newmail);
        editTextLastName = (EditText) findViewById(R.id.getLastName);
        editTextAddressLine1 = (EditText) findViewById(R.id.getAddressLine1);
        editTextAddressLine2 = (EditText) findViewById(R.id.getAddressLine2);
        editTextCity = (EditText) findViewById(R.id.getLocality);
        editTextCountry = (EditText) findViewById(R.id.getCountry);
        editTextPostalCode = (EditText) findViewById(R.id.getPostalCode);
        spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
        spinnerShoeSize = (Spinner) findViewById(R.id.spinnerShoeSize);
        spinnerFavoriteColor = (Spinner) findViewById(R.id.spinnerFavoriteColor);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

        //Set the default photo
        profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));

        //Check permission to take photo and access storage then create ChangePhotoDialog
        cameraLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                int numberOfPermission = 0;
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                for (int i=0;i<2;i++) {
                    if (ContextCompat.checkSelfPermission(CreateProfileActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{permissions[i]},
                                MY_PERMISSIONS_REQUEST);
                    } else {
                        if(i==1){
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
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

    public boolean checkDate(int day, String month, int year) {
        if (day == 29 && month.equals( "February")) return year % 4 == 0;
        if (day > 29 && month.equals("February")) return false;
        if (day == 31) {
            return (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November"));
        }
        return true;
    }

    public boolean checkStringIsCorrect(String str) {
        str = str.replaceAll("[^\\w]", "");
        return str.length() > 0;
    }

    public int transformMonth(String month){
        switch (month){
            case "January" :return 1;
            case "February" : return 2;
            case "March":return 3;
            case "April": return 4;
            case "May": return 5;
            case "June":return 6;
            case "July": return 7;
            case "Augustus": return 8;
            case "September" : return  9;
            case "October" : return 10;
            case "November" : return 11;
            case "December" : return 12;
            default: return -1;
        }
    }


    /*
    * Firstly we collect the information given by the user
    * Then we check if nothing necessary is missing and if birthdate is ok
    * If something goes wrong we stay at this activity and put some information to help the user
    * If nothing goes wrong we go to the main menu
     */
    public void createUser(View view) throws Exception{
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
            postalCode = (int) Integer.parseInt(editTextPostalCode.getText().toString());
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

        //Check if any required missing
        if (!checkStringIsCorrect(firstName)) {
            editTextFirstName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextFirstName.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (!checkStringIsCorrect(lastName)) {
            editTextLastName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextLastName.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (!checkStringIsCorrect(addressLine1)) {
            editTextAddressLine1.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextAddressLine1.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (!checkStringIsCorrect(city)) {
            editTextCity.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCity.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (!checkStringIsCorrect(country)) {
            editTextCountry.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCountry.setBackgroundColor(getResources().getColor(R.color.white));
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
            //create an User
            DateWish birthdate=new DateWish();
            birthdate.setDate(dayInt,month,yearInt);
            User user=new User(userAddress,firstName,lastName,email,birthdate,password);
            if (!favoriteColor.equals("Undefined"))user.setFavoriteColor(favoriteColor);
            if (!size.equals("Undefined")) user.setSize(size);
            if (!shoeSize.equals("Undefined")) user.setShoeSize(shoeSize);
            DatabaseHelper dbHelper= new DatabaseHelper(getApplicationContext());
            if(dbHelper.addUser(user)){
                int userID=dbHelper.checkUser(email,password);
                if(userID==-1){
                    Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast=Toast.makeText(this,"Account Create",Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent=new Intent(this,MainMenuActivity.class);
                    intent.putExtra("userID",userID);
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

    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if(bitmap!=null){
            profilePhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getUriImage(Uri uri) {
        if(uri!=null){
            profilePhoto.setImageURI(uri);
        }
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
