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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.ImageHelper;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.User;

import com.example.wishlist.Fragment.EditPhotoDialog;
import com.example.wishlist.Fragment.AddPhotoDialog;
import com.example.wishlist.Fragment.EditPhotoDialog;

import com.example.wishlist.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements EditPhotoDialog.OnPhotoReceivedListener {
    private User user;
    private int userID;
    private final int MY_PERMISSIONS_REQUEST=45;
    private Bitmap image;
    //Button
    private ImageButton modifyButton;
    private ImageButton activeEditModeButton;
    private ImageButton backArrow;
    private ImageView cameraLogo;

    private CircleImageView profilePhoto;
    //TexView : "Information :"
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewAddressLine1;
    private TextView textViewCity;
    private TextView textViewPostalCode;
    private TextView textViewCountry;
    private TextView textViewBirthDate;
    //TextView : "actualInformation"
    private TextView actualFirstName;
    private TextView actualLastName;
    private TextView actualAddressLine1;
    private TextView actualAddressLine2;
    private TextView actualCity;
    private TextView actualPostalCode;
    private TextView actualCountry;
    private TextView actualSize;
    private TextView actualShoeSize;
    private TextView actualFavoriteColor;
    private TextView actualBirthDate;
    //EditText or Spinner to modify the information
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAddressLine1;
    private EditText editTextAddressLine2;
    private EditText editTextCity;
    private EditText editTextPostalCode;
    private EditText editTextCountry;
    private Spinner spinnerSize;
    private Spinner spinnerShoeSize;
    private Spinner spinnerFavoriteColor;
    private Spinner spinnerDay;
    private Spinner spinnerMonth;
    private Spinner spinnerYear;
    //RelativeLayout (group textViews and editText/Spinner of a specific information)
    private RelativeLayout relativeLayoutAddressLine2;
    private RelativeLayout relativeLayoutSize;
    private RelativeLayout relativeLayoutShoeSize;
    private RelativeLayout relativeLayoutFavoriteColor;
    private RelativeLayout relativeLayoutSpinnersDate;

    /**
     * Function to get the index of a string in a spinner
     * @param spinner
     * @param myString
     * @return the index if the string is in the spinner 0 (basic position of spinner) otherwise
     */
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }


    public void onBackPressed(View view) {
        onBackPressed();
    }

    /**
     * Look if any User is declared connected by shared preference
     * -> If one user is connected :
     *      - assign the views to their global variables and set the onClickListener of camera logo
     *      - get user from his id and assign it to the global variable user
     *      - fill the information about user
     * -> Go to login activity otherwise
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        //Get UserID and go back to login if there is no
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            userID =tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }
        UserDatabaseHelper dbHelper= new UserDatabaseHelper(getApplicationContext());
        user =dbHelper.getUserFromID(userID);

        //Rely variable with layout
        profilePhoto = findViewById(R.id.profilePhoto);
        modifyButton = findViewById(R.id.modifyProfile);
        activeEditModeButton = findViewById(R.id.modifyMode);
        backArrow = findViewById(R.id.backArrowEditProfile);
        cameraLogo = findViewById(R.id.logoCamera);

        textViewFirstName = findViewById(R.id.FirstName);
        textViewLastName = findViewById(R.id.LastName);
        textViewAddressLine1 = findViewById(R.id.AddressLine1);
        textViewCity = findViewById(R.id.City);
        textViewPostalCode = findViewById(R.id.PostalCode);
        textViewCountry = findViewById(R.id.Country);
        textViewBirthDate = findViewById(R.id.BirthDate);

        actualFirstName = findViewById(R.id.actualFirstName);
        actualLastName = findViewById(R.id.actualLastName);
        actualAddressLine1 = findViewById(R.id.actualAddressLine1);
        actualAddressLine2 = findViewById(R.id.actualAddressLine2);
        actualCity = findViewById(R.id.actualCity);
        actualPostalCode = findViewById(R.id.actualPostalCode);
        actualCountry = findViewById(R.id.actualCountry);
        actualSize = findViewById(R.id.actualSize);
        actualShoeSize = findViewById(R.id.actualShoeSize);
        actualFavoriteColor = findViewById(R.id.actualFavoriteColor);
        actualBirthDate = findViewById(R.id.wrongBirthdate);

        editTextFirstName = findViewById(R.id.editFirstName);
        editTextLastName = findViewById(R.id.editLastName);
        editTextAddressLine1 = findViewById(R.id.editAddressLine1);
        editTextAddressLine2 = findViewById(R.id.editAddressLine2);
        editTextCity = findViewById(R.id.editCity);
        editTextPostalCode = findViewById(R.id.editPostalCode);
        editTextCountry = findViewById(R.id.editCountry);
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerShoeSize = findViewById(R.id.spinnerShoeSize);
        spinnerFavoriteColor = findViewById(R.id.spinnerFavoriteColor);
        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);

        relativeLayoutAddressLine2 = findViewById(R.id.layoutAddressLine2);
        relativeLayoutFavoriteColor = findViewById(R.id.layoutFavoriteColor);
        relativeLayoutShoeSize = findViewById(R.id.layoutShoeSize);
        relativeLayoutSize = findViewById(R.id.layoutSize);
        relativeLayoutSpinnersDate = findViewById(R.id.layoutSpinnersDate);
        //Set the mode to view
        visibleMode();
        //Set function onclick of camera logo
        cameraLogo.setOnClickListener(new View.OnClickListener() {
            //Check permission to take photo and access storage then create ChangePhotoDialogEdit
            @Override
            public void onClick(View v) {
                //check permission
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                for (int i=0;i<2;i++) {
                    if (ContextCompat.checkSelfPermission(MyProfileActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{permissions[i]},
                                MY_PERMISSIONS_REQUEST);
                    } else {
                        if(i==1){
                            EditPhotoDialog dialog = new EditPhotoDialog();
                            dialog.show(MyProfileActivity.this.getSupportFragmentManager(), "ha");
                        }
                    }
                }
            }
        });
    }

    /**
     * Make some change in the layout to able edit profile :
     *      - change visibility of layout
     *      - change some text (add * for required information)
     *      - fill editTexts and spinners with actual information
     *      - eventually set some layout visible if there where not in view mode
     */
    public void editMode(){
        activeEditModeButton.setVisibility(View.GONE);
        modifyButton.setVisibility(View.VISIBLE);
        //set visibleMode when click on backArrow (-> no change in profile)
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMode();
            }
        });
        cameraLogo.setVisibility(View.VISIBLE);
        relativeLayoutShoeSize.setVisibility(View.VISIBLE);
        relativeLayoutSize.setVisibility(View.VISIBLE);
        relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
        relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
        relativeLayoutSpinnersDate.setVisibility(View.VISIBLE);
        textViewFirstName.setText(R.string.firstName);
        textViewLastName.setText(R.string.lastName);
        textViewAddressLine1.setText(R.string.addressLine1);
        textViewCity.setText(R.string.city);
        textViewCountry.setText(R.string.country);
        textViewPostalCode.setText(R.string.postalCode);
        textViewBirthDate.setText(R.string.birthdate);
        //Set Visibility
        editTextFirstName.setVisibility(View.VISIBLE);
        editTextLastName.setVisibility(View.VISIBLE);
        editTextAddressLine1.setVisibility(View.VISIBLE);
        editTextAddressLine2.setVisibility(View.VISIBLE);
        editTextCity.setVisibility(View.VISIBLE);
        editTextPostalCode.setVisibility(View.VISIBLE);
        editTextCountry.setVisibility(View.VISIBLE);
        spinnerSize.setVisibility(View.VISIBLE);
        spinnerShoeSize.setVisibility(View.VISIBLE);
        spinnerFavoriteColor.setVisibility(View.VISIBLE);
        spinnerDay.setVisibility(View.VISIBLE);
        spinnerMonth.setVisibility(View.VISIBLE);
        spinnerYear.setVisibility(View.VISIBLE);
        actualFirstName.setVisibility(View.GONE);
        actualLastName.setVisibility(View.GONE);
        actualAddressLine1.setVisibility(View.GONE);
        actualAddressLine2.setVisibility(View.GONE);
        actualCity.setVisibility(View.GONE);
        actualPostalCode.setVisibility(View.GONE);
        actualCountry.setVisibility(View.GONE);
        actualSize.setVisibility(View.GONE);
        actualShoeSize.setVisibility(View.GONE);
        actualFavoriteColor.setVisibility(View.GONE);
        actualBirthDate.setVisibility(View.GONE);

        //Fill in with actual information
        editTextPostalCode.setText(String.format("%d", user.getAddress().getPostalCode()));
        editTextCity.setText(user.getAddress().getCity());
        editTextCountry.setText(user.getAddress().getCountry());
        editTextAddressLine1.setText(user.getAddress().getAddressLine1());
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        String[] date= user.getBirthDate().toString().split(" ");
        spinnerDay.setSelection(getIndex(spinnerDay,date[0]));
        spinnerMonth.setSelection(getIndex(spinnerMonth,date[1]));
        spinnerYear.setSelection(getIndex(spinnerYear,date[2]));
        spinnerFavoriteColor.setSelection(getIndex(spinnerFavoriteColor, user.getFavoriteColor()));
        spinnerSize.setSelection(getIndex(spinnerSize, user.getSize()));
        spinnerShoeSize.setSelection(getIndex(spinnerShoeSize, user.getShoeSize()));
    }

    public void visibleMode(View view){
        visibleMode();
    }

    /**
     * Make some change in the layout for the view profile :
     *      - change visibility of layout
     *      - change some text (take off * of required information)
     *      - fill textViews with actual information
     *      - eventually set visibility of some layout to gone if we have no information about that
     *                                   (ex : shoesize is undefined -> we don't see "Shoe size :")
     */
    @TargetApi(21)
    public void visibleMode(){
        textViewFirstName.setText(R.string.firstNameWithout);
        textViewLastName.setText(R.string.lastNameWithout);
        textViewAddressLine1.setText(R.string.addressLine1Without);
        textViewCity.setText(R.string.cityWithout);
        textViewCountry.setText(R.string.countryWithout);
        textViewPostalCode.setText(R.string.postalCodeWithout);
        textViewBirthDate.setText(R.string.birthdateWithout);
        if(user.getProfilePhoto()!=null) {
            profilePhoto.setImageBitmap(user.getProfilePhoto());
        }else{
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));
        }
        //Set normal onBackPressed function when we click on back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Set Visibility
        activeEditModeButton.setVisibility(View.VISIBLE);
        modifyButton.setVisibility(View.GONE);
        cameraLogo.setVisibility(View.GONE);
        editTextFirstName.setVisibility(View.GONE);
        editTextLastName.setVisibility(View.GONE);
        editTextAddressLine1.setVisibility(View.GONE);
        editTextAddressLine2.setVisibility(View.GONE);
        editTextCity.setVisibility(View.GONE);
        editTextPostalCode.setVisibility(View.GONE);
        editTextCountry.setVisibility(View.GONE);
        spinnerSize.setVisibility(View.GONE);
        spinnerShoeSize.setVisibility(View.GONE);
        spinnerFavoriteColor.setVisibility(View.GONE);
        spinnerDay.setVisibility(View.GONE);
        spinnerMonth.setVisibility(View.GONE);
        spinnerYear.setVisibility(View.GONE);
        actualFirstName.setVisibility(View.VISIBLE);
        actualLastName.setVisibility(View.VISIBLE);
        actualAddressLine1.setVisibility(View.VISIBLE);
        actualAddressLine2.setVisibility(View.VISIBLE);
        actualCity.setVisibility(View.VISIBLE);
        actualPostalCode.setVisibility(View.VISIBLE);
        actualCountry.setVisibility(View.VISIBLE);
        actualSize.setVisibility(View.VISIBLE);
        actualShoeSize.setVisibility(View.VISIBLE);
        actualFavoriteColor.setVisibility(View.VISIBLE);
        actualBirthDate.setVisibility(View.VISIBLE);

        //Fill in with actual information
        actualPostalCode.setText(String.format("%d", user.getAddress().getPostalCode()));
        actualCity.setText(user.getAddress().getCity());
        actualCountry.setText(user.getAddress().getCountry());
        actualAddressLine1.setText(user.getAddress().getAddressLine1());
        actualFirstName.setText(user.getFirstName());
        actualLastName.setText(user.getLastName());
        actualBirthDate.setTextColor(getResources().getColor(R.color.black));
        actualBirthDate.setText(user.getBirthDate().toString());
        relativeLayoutSpinnersDate.setVisibility(View.GONE);
        String addressLine2= user.getAddress().getAddressLine2();
        if(addressLine2==null||addressLine2.toLowerCase().equals("null")||addressLine2.equals("")){
            relativeLayoutAddressLine2.setVisibility(View.GONE);
        }
        else{
            relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
            actualAddressLine2.setText(addressLine2);
        }
        String favoriteColor= user.getFavoriteColor();
        if(favoriteColor==null||favoriteColor.toLowerCase().equals("null")||
                favoriteColor.toLowerCase().equals("undefined")||favoriteColor.equals("")){
            relativeLayoutFavoriteColor.setVisibility(View.GONE);
        }
        else{
            relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
            actualFavoriteColor.setText(favoriteColor);
        }
        String size= user.getSize();
        if(size==null||size.toLowerCase().equals("null")||size.toLowerCase().equals("undefined")){
            relativeLayoutSize.setVisibility(View.GONE);
        }
        else {
            relativeLayoutSize.setVisibility(View.VISIBLE);
            actualSize.setText(size);
        }
        String shoeSize= user.getShoeSize();
        if(shoeSize==null||shoeSize.toLowerCase().equals("null")||
                shoeSize.toLowerCase().equals("undefined")||shoeSize.equals("")||shoeSize.equals("0")){
            relativeLayoutShoeSize.setVisibility(View.GONE);
        }
        else{
            relativeLayoutShoeSize.setVisibility(View.VISIBLE);
            actualShoeSize.setText(shoeSize);
        }
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

    public void editMode(View view){
        editMode();
    }


    /**
     * Update User in the database with the new information collected
     * Give some information to the user if he doesn't fill anything well
     * Almost same function than createUser in CreateProfileActivity
     * @param view
     */
    public void updateUser(View view){
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

        //Check if any required missing
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
        int yearInt=-1;
        int dayInt=-1;
        actualBirthDate.setTextColor(getResources().getColor(R.color.wrongInformation));

        if (year.equals("Year") || day.equals("Day") || month.equals("Month")) {
            actualBirthDate.setText(getResources().getText(R.string.wrongDate));
            numberError++;
        } else {
            yearInt = Integer.parseInt(year);
            dayInt = Integer.parseInt(day);
            if (!checkDate(dayInt, month, yearInt)) {
                actualBirthDate.setText(getResources().getText(R.string.wrongDate));
                numberError++;
            } else {
                actualBirthDate.setText("");
            }
        }

        if (numberError == 0) {
            //Create an Address
            Address userAddress=new Address(addressLine1,city,country,postalCode);
            if(checkStringIsCorrect(addressLine2)) userAddress.setAddressLine2(addressLine2);
            //create an User
            DateWish birthdate=new DateWish();
            birthdate.setDate(dayInt,month,yearInt);
            user.setAddress(userAddress);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setBirthDate(birthdate);
            user.setFavoriteColor(favoriteColor);
            user.setSize(size);
            user.setShoeSize(shoeSize);

            UserDatabaseHelper dbHelper= new UserDatabaseHelper(getApplicationContext());
            if(dbHelper.updateUser(user, userID)){
                if(userID ==-1){
                    Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast=Toast.makeText(this,"Account updated",Toast.LENGTH_SHORT);
                    toast.show();
                    visibleMode();
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
     * Set bitmap if it's not null in profile photo view, in global variable image and in user
     * @param bitmap image to set in profile photo
     */
    @TargetApi(21)
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if(bitmap==null){
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));
        }
        else {
            profilePhoto.setImageBitmap(bitmap);
            image =bitmap;
            user.setProfilePhoto(image);
        }
    }

    /**
     * Transform the uri image to bitmap
     * Set  this bitmap if it's not null in profile photo view, in global variable image and in user
     * @param uri image to set in profile photo
     */
    @TargetApi(21)
    @Override
    public void setUriImage(Uri uri) {
        if(uri!=null){
            profilePhoto.setImageURI(uri);
            try{
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                user.setProfilePhoto(image);
            }
            catch (Exception e){
                Toast toast=Toast.makeText(this,"something went wrong with the image",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));
        }
    }
    
}
