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
import com.example.wishlist.Fragment.ChangePhotoDialog;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Date;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements ChangePhotoDialog.OnPhotoReceivedListener {
    //email and password get from CreateNewAccountActivity
    private String email;
    private String password;
    private final int MY_PERMISSIONS_REQUEST = 1;
    private String imagePath;
    private CircleImageView profilePhoto;

   /* @Override//TODO Check if possible to remove that -> done I don't see any difference
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
        Intent intent=getIntent();
        email=intent.getStringExtra("mail");
        password=intent.getStringExtra("password");
        setContentView(R.layout.activity_create_profile);
        //Set the photo
        profilePhoto= findViewById(R.id.profilePhoto);
        profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));

        //Check permission to take photo and access storage then create ChangePhotoDialog
        ImageView cameraLogo = findViewById(R.id.logoCamera);
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
    public void createUser(View view) {
        int numberError = 0;
        //Get the information
        EditText editTextFirstName = (EditText) findViewById(R.id.newmail);
        String firstName = editTextFirstName.getText().toString();
        EditText editTextLastName = (EditText) findViewById(R.id.getLastName);
        String lastName = editTextLastName.getText().toString();
        EditText editTextAddressLine1 = (EditText) findViewById(R.id.getAddressLine1);
        String addressLine1 = editTextAddressLine1.getText().toString();
        EditText editTextAddressLine2 = (EditText) findViewById(R.id.getAddressLine2);
        String addressLine2 = editTextAddressLine2.getText().toString();
        EditText editTextCity = (EditText) findViewById(R.id.getLocality);
        String city = editTextCity.getText().toString();
        EditText editTextCountry = (EditText) findViewById(R.id.getCountry);
        String country = editTextCountry.getText().toString();
        EditText editTextPostalCode = (EditText) findViewById(R.id.getPostalCode);
        int postalCode=0;
        try {
            postalCode = (int) Integer.parseInt(editTextPostalCode.getText().toString());
            editTextPostalCode.setBackgroundColor(Color.rgb(255, 255, 255));
        } catch (NumberFormatException e) {
            editTextPostalCode.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        }


        Spinner spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
        String size = spinnerSize.getSelectedItem().toString();
        Spinner spinnerShoeSize = (Spinner) findViewById(R.id.spinnerShoeSize);
        String shoeSize = spinnerShoeSize.getSelectedItem().toString();
        Spinner spinnerFavoriteColor = (Spinner) findViewById(R.id.spinnerFavoriteColor);
        String favoriteColor = spinnerFavoriteColor.getSelectedItem().toString();
        Spinner spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        String day = spinnerDay.getSelectedItem().toString();
        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        String month = spinnerMonth.getSelectedItem().toString();
        Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        String year = spinnerYear.getSelectedItem().toString();


        //Check if any required missing
        if (!checkStringIsCorrect(firstName)) {
            editTextFirstName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextFirstName.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        if (!checkStringIsCorrect(lastName)) {
            editTextLastName.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextLastName.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        if (!checkStringIsCorrect(addressLine1)) {
            editTextAddressLine1.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextAddressLine1.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        if (!checkStringIsCorrect(city)) {
            editTextCity.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCity.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        if (!checkStringIsCorrect(country)) {
            editTextCountry.setBackgroundColor(getResources().getColor(R.color.wrongInformation));
            numberError++;
        } else {
            editTextCountry.setBackgroundColor(Color.rgb(255, 255, 255));
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
            Calendar calendar= Calendar.getInstance();
            calendar.set(yearInt,transformMonth(month),dayInt);
            User user=new User(userAddress,firstName,lastName,email,calendar.getTime(),password);
            if (!favoriteColor.equals("Undefined"))user.setFavoriteColor(favoriteColor);
            if (!size.equals("Undefined")) user.setSize(size);
            if (!shoeSize.equals("Undefined")) user.setShoeSize(Integer.parseInt(shoeSize));
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
}
