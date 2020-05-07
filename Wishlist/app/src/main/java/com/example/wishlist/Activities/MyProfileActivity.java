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
    private boolean editMode;
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

    /*
     * get the index of a string in a specified spinner
     * Set 0 (->default item of the spinner) if string isn't in the spinner
     */
    private int getIndex(final Spinner spinner, final String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }


    public void onBackPressed(final View view) {
        this.onBackPressed();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_profile);
        //Get the userID then create user with information in db relative to that ID
        final Intent intent= this.getIntent();

        //Get UserID and go back to login if there is no
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            this.userID =tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            final Intent backToLogin=new Intent(this,LoginActivity.class);
        }

        final UserDatabaseHelper dbHelper= new UserDatabaseHelper(this.getApplicationContext());

        this.user =dbHelper.getUserFromID(this.userID);

        //Rely variable with layout
        this.profilePhoto = this.findViewById(R.id.profilePhoto);
        this.modifyButton = this.findViewById(R.id.modifyProfile);
        this.activeEditModeButton = this.findViewById(R.id.modifyMode);
        this.backArrow = this.findViewById(R.id.backArrowEditProfile);
        this.cameraLogo = this.findViewById(R.id.logoCamera);

        this.textViewFirstName = this.findViewById(R.id.FirstName);
        this.textViewLastName = this.findViewById(R.id.LastName);
        this.textViewAddressLine1 = this.findViewById(R.id.AddressLine1);
        this.textViewCity = this.findViewById(R.id.City);
        this.textViewPostalCode = this.findViewById(R.id.PostalCode);
        this.textViewCountry = this.findViewById(R.id.Country);
        this.textViewBirthDate = this.findViewById(R.id.BirthDate);

        this.actualFirstName = this.findViewById(R.id.actualFirstName);
        this.actualLastName = this.findViewById(R.id.actualLastName);
        this.actualAddressLine1 = this.findViewById(R.id.actualAddressLine1);
        this.actualAddressLine2 = this.findViewById(R.id.actualAddressLine2);
        this.actualCity = this.findViewById(R.id.actualCity);
        this.actualPostalCode = this.findViewById(R.id.actualPostalCode);
        this.actualCountry = this.findViewById(R.id.actualCountry);
        this.actualSize = this.findViewById(R.id.actualSize);
        this.actualShoeSize = this.findViewById(R.id.actualShoeSize);
        this.actualFavoriteColor = this.findViewById(R.id.actualFavoriteColor);
        this.actualBirthDate = this.findViewById(R.id.wrongBirthdate);

        this.editTextFirstName = this.findViewById(R.id.editFirstName);
        this.editTextLastName = this.findViewById(R.id.editLastName);
        this.editTextAddressLine1 = this.findViewById(R.id.editAddressLine1);
        this.editTextAddressLine2 = this.findViewById(R.id.editAddressLine2);
        this.editTextCity = this.findViewById(R.id.editCity);
        this.editTextPostalCode = this.findViewById(R.id.editPostalCode);
        this.editTextCountry = this.findViewById(R.id.editCountry);
        this.spinnerSize = this.findViewById(R.id.spinnerSize);
        this.spinnerShoeSize = this.findViewById(R.id.spinnerShoeSize);
        this.spinnerFavoriteColor = this.findViewById(R.id.spinnerFavoriteColor);
        this.spinnerDay = this.findViewById(R.id.spinnerDay);
        this.spinnerMonth = this.findViewById(R.id.spinnerMonth);
        this.spinnerYear = this.findViewById(R.id.spinnerYear);

        this.relativeLayoutAddressLine2 = this.findViewById(R.id.layoutAddressLine2);
        this.relativeLayoutFavoriteColor = this.findViewById(R.id.layoutFavoriteColor);
        this.relativeLayoutShoeSize = this.findViewById(R.id.layoutShoeSize);
        this.relativeLayoutSize = this.findViewById(R.id.layoutSize);
        this.relativeLayoutSpinnersDate = this.findViewById(R.id.layoutSpinnersDate);
        //Set the mode to view
        if(!this.editMode) this.visibleMode();
        //Set function onclick of camera logo
        this.cameraLogo.setOnClickListener(new View.OnClickListener() {
            //Check permission to take photo and access storage then create ChangePhotoDialogEdit
            @Override
            public void onClick(final View v) {
                //check permission
                final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                for (int i=0;i<2;i++) {
                    if (ContextCompat.checkSelfPermission(MyProfileActivity.this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{permissions[i]},
                                MyProfileActivity.this.MY_PERMISSIONS_REQUEST);
                    } else {
                        if(i==1){
                            final EditPhotoDialog dialog = new EditPhotoDialog();
                            dialog.show(getSupportFragmentManager(), "ha");
                        }
                    }
                }
            }
        });
    }

    /*
     *Make some change in the layout to able edit profile :
     * -change visibility of layout
     * -change some text (add * for required information)
     * -fill editTexts and spinners with actual information
     * -eventually set some layout visible if there where not in view mode
     */
    public void editMode(){
        this.editMode =true;
        this.activeEditModeButton.setVisibility(View.GONE);
        this.modifyButton.setVisibility(View.VISIBLE);
        //set visibleMode when click on backArrow (-> no change in profile)
        this.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MyProfileActivity.this.visibleMode();
            }
        });
        this.cameraLogo.setVisibility(View.VISIBLE);
        this.relativeLayoutShoeSize.setVisibility(View.VISIBLE);
        this.relativeLayoutSize.setVisibility(View.VISIBLE);
        this.relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
        this.relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
        this.relativeLayoutSpinnersDate.setVisibility(View.VISIBLE);
        this.textViewFirstName.setText(R.string.firstName);
        this.textViewLastName.setText(R.string.lastName);
        this.textViewAddressLine1.setText(R.string.addressLine1);
        this.textViewCity.setText(R.string.city);
        this.textViewCountry.setText(R.string.country);
        this.textViewPostalCode.setText(R.string.postalCode);
        this.textViewBirthDate.setText(R.string.birthdate);
        //Set Visibility
        this.editTextFirstName.setVisibility(View.VISIBLE);
        this.editTextLastName.setVisibility(View.VISIBLE);
        this.editTextAddressLine1.setVisibility(View.VISIBLE);
        this.editTextAddressLine2.setVisibility(View.VISIBLE);
        this.editTextCity.setVisibility(View.VISIBLE);
        this.editTextPostalCode.setVisibility(View.VISIBLE);
        this.editTextCountry.setVisibility(View.VISIBLE);
        this.spinnerSize.setVisibility(View.VISIBLE);
        this.spinnerShoeSize.setVisibility(View.VISIBLE);
        this.spinnerFavoriteColor.setVisibility(View.VISIBLE);
        this.spinnerDay.setVisibility(View.VISIBLE);
        this.spinnerMonth.setVisibility(View.VISIBLE);
        this.spinnerYear.setVisibility(View.VISIBLE);
        this.actualFirstName.setVisibility(View.GONE);
        this.actualLastName.setVisibility(View.GONE);
        this.actualAddressLine1.setVisibility(View.GONE);
        this.actualAddressLine2.setVisibility(View.GONE);
        this.actualCity.setVisibility(View.GONE);
        this.actualPostalCode.setVisibility(View.GONE);
        this.actualCountry.setVisibility(View.GONE);
        this.actualSize.setVisibility(View.GONE);
        this.actualShoeSize.setVisibility(View.GONE);
        this.actualFavoriteColor.setVisibility(View.GONE);
        this.actualBirthDate.setVisibility(View.GONE);

        //Fill in with actual information
        this.editTextPostalCode.setText(String.format("%d", this.user.getAddress().getPostalCode()));
        this.editTextCity.setText(this.user.getAddress().getCity());
        this.editTextCountry.setText(this.user.getAddress().getCountry());
        this.editTextAddressLine1.setText(this.user.getAddress().getAddressLine1());
        this.editTextFirstName.setText(this.user.getFirstName());
        this.editTextLastName.setText(this.user.getLastName());
        final String[] date= this.user.getBirthDate().toString().split(" ");
        this.spinnerDay.setSelection(this.getIndex(this.spinnerDay,date[0]));
        this.spinnerMonth.setSelection(this.getIndex(this.spinnerMonth,date[1]));
        this.spinnerYear.setSelection(this.getIndex(this.spinnerYear,date[2]));
        this.spinnerFavoriteColor.setSelection(this.getIndex(this.spinnerFavoriteColor, this.user.getFavoriteColor()));
        this.spinnerSize.setSelection(this.getIndex(this.spinnerSize, this.user.getSize()));
        this.spinnerShoeSize.setSelection(this.getIndex(this.spinnerShoeSize, this.user.getShoeSize()));
    }

    public void visibleMode(final View view){
        this.visibleMode();
    }

    /*
     *Make some change in the layout for the view profile :
     * -change visibility of layout
     * -change some text (take off * of required information)
     * -fill textViews with actual information
     * -eventually set visibility of some layout to gone if we have no information about that
     * (ex : shoesize is undefined -> we don't see "Shoe size :")
     */
    @TargetApi(21)
    public void visibleMode(){
        this.editMode =false;
        this.textViewFirstName.setText(R.string.firstNameWithout);
        this.textViewLastName.setText(R.string.lastNameWithout);
        this.textViewAddressLine1.setText(R.string.addressLine1Without);
        this.textViewCity.setText(R.string.cityWithout);
        this.textViewCountry.setText(R.string.countryWithout);
        this.textViewPostalCode.setText(R.string.postalCodeWithout);
        this.textViewBirthDate.setText(R.string.birthdateWithout);
        if(this.user.getProfilePhoto()!=null) {
            this.profilePhoto.setImageBitmap(this.user.getProfilePhoto());
        }else{
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }
        //Set normal onBackPressed function when we click on back arrow
        this.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MyProfileActivity.this.onBackPressed();
            }
        });
        //Set Visibility
        this.activeEditModeButton.setVisibility(View.VISIBLE);
        this.modifyButton.setVisibility(View.GONE);
        this.cameraLogo.setVisibility(View.GONE);
        this.editTextFirstName.setVisibility(View.GONE);
        this.editTextLastName.setVisibility(View.GONE);
        this.editTextAddressLine1.setVisibility(View.GONE);
        this.editTextAddressLine2.setVisibility(View.GONE);
        this.editTextCity.setVisibility(View.GONE);
        this.editTextPostalCode.setVisibility(View.GONE);
        this.editTextCountry.setVisibility(View.GONE);
        this.spinnerSize.setVisibility(View.GONE);
        this.spinnerShoeSize.setVisibility(View.GONE);
        this.spinnerFavoriteColor.setVisibility(View.GONE);
        this.spinnerDay.setVisibility(View.GONE);
        this.spinnerMonth.setVisibility(View.GONE);
        this.spinnerYear.setVisibility(View.GONE);
        this.actualFirstName.setVisibility(View.VISIBLE);
        this.actualLastName.setVisibility(View.VISIBLE);
        this.actualAddressLine1.setVisibility(View.VISIBLE);
        this.actualAddressLine2.setVisibility(View.VISIBLE);
        this.actualCity.setVisibility(View.VISIBLE);
        this.actualPostalCode.setVisibility(View.VISIBLE);
        this.actualCountry.setVisibility(View.VISIBLE);
        this.actualSize.setVisibility(View.VISIBLE);
        this.actualShoeSize.setVisibility(View.VISIBLE);
        this.actualFavoriteColor.setVisibility(View.VISIBLE);
        this.actualBirthDate.setVisibility(View.VISIBLE);

        //Fill in with actual information
        this.actualPostalCode.setText(String.format("%d", this.user.getAddress().getPostalCode()));
        this.actualCity.setText(this.user.getAddress().getCity());
        this.actualCountry.setText(this.user.getAddress().getCountry());
        this.actualAddressLine1.setText(this.user.getAddress().getAddressLine1());
        this.actualFirstName.setText(this.user.getFirstName());
        this.actualLastName.setText(this.user.getLastName());
        this.actualBirthDate.setTextColor(this.getResources().getColor(R.color.black));
        this.actualBirthDate.setText(this.user.getBirthDate().toString());
        this.relativeLayoutSpinnersDate.setVisibility(View.GONE);
        final String addressLine2= this.user.getAddress().getAddressLine2();
        if(addressLine2==null||addressLine2.toLowerCase().equals("null")||addressLine2.equals("")){
            this.relativeLayoutAddressLine2.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
            this.actualAddressLine2.setText(addressLine2);
        }
        final String favoriteColor= this.user.getFavoriteColor();
        if(favoriteColor==null||favoriteColor.toLowerCase().equals("null")||
                favoriteColor.toLowerCase().equals("undefined")||favoriteColor.equals("")){
            this.relativeLayoutFavoriteColor.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
            this.actualFavoriteColor.setText(favoriteColor);
        }
        final String size= this.user.getSize();
        if(size==null||size.toLowerCase().equals("null")||size.toLowerCase().equals("undefined")){
            this.relativeLayoutSize.setVisibility(View.GONE);
        }
        else {
            this.relativeLayoutSize.setVisibility(View.VISIBLE);
            this.actualSize.setText(size);
        }
        final String shoeSize= this.user.getShoeSize();
        if(shoeSize==null||shoeSize.toLowerCase().equals("null")||
                shoeSize.toLowerCase().equals("undefined")||shoeSize.equals("")||shoeSize.equals("0")){
            this.relativeLayoutShoeSize.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutShoeSize.setVisibility(View.VISIBLE);
            this.actualShoeSize.setText(shoeSize);
        }
    }

    public boolean checkDate(final int day, final String month, final int year) {
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

    public void editMode(final View view){
        this.editMode();}
    /*
     * Update User in the database with the new information collected
     * Give some information to the user if he doesn't fill anything well
     * Almost same function than createUser in CreateProfileActivity
     */
    public void updateUser(final View view){
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

        //Check if any required missing
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
        int yearInt=-1;
        int dayInt=-1;
        this.actualBirthDate.setTextColor(this.getResources().getColor(R.color.wrongInformation));

        if (year.equals("Year") || day.equals("Day") || month.equals("Month")) {
            this.actualBirthDate.setText(this.getResources().getText(R.string.wrongDate));
            numberError++;
        } else {
            yearInt = Integer.parseInt(year);
            dayInt = Integer.parseInt(day);
            if (!this.checkDate(dayInt, month, yearInt)) {
                this.actualBirthDate.setText(this.getResources().getText(R.string.wrongDate));
                numberError++;
            } else {
                this.actualBirthDate.setText("");
            }
        }

        if (numberError == 0) {
            //Create an Address
            final Address userAddress=new Address(addressLine1,city,country,postalCode);
            if(this.checkStringIsCorrect(addressLine2)) userAddress.setAddressLine2(addressLine2);
            //create an User
            final DateWish birthdate=new DateWish();
            birthdate.setDate(dayInt,month,yearInt);
            this.user.setAddress(userAddress);
            this.user.setFirstName(firstName);
            this.user.setLastName(lastName);
            this.user.setBirthDate(birthdate);
            this.user.setFavoriteColor(favoriteColor);
            this.user.setSize(size);
            this.user.setShoeSize(shoeSize);

            final UserDatabaseHelper dbHelper= new UserDatabaseHelper(this.getApplicationContext());
            if(dbHelper.updateUser(this.user, this.userID)){
                if(this.userID ==-1){
                    final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    final Toast toast=Toast.makeText(this,"Account updated",Toast.LENGTH_SHORT);
                    toast.show();
                    this.visibleMode();
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

    /*
     * 2 functions to modify the profile photo of the user
     * Called by ChangeDialogEdit (override public interface of this class)
     */
    @TargetApi(21)
    @Override
    public void getBitmapImage(final Bitmap bitmap) {
        if(bitmap==null){
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }
        else {
            this.profilePhoto.setImageBitmap(bitmap);
            this.image =bitmap;
            this.user.setProfilePhoto(this.image);
        }
    }
    @TargetApi(21)
    @Override
    public void setUriImage(final Uri uri) {
        if(uri!=null){
            this.profilePhoto.setImageURI(uri);
            try{
                this.image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                this.user.setProfilePhoto(this.image);
            }
            catch (final Exception e){
                final Toast toast=Toast.makeText(this,"something went wrong with the image",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }
    }
    
}
