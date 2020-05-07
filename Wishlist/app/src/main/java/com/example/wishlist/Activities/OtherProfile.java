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
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Fragment.EditPhotoDialog;
import com.example.wishlist.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfile extends AppCompatActivity {
    private User otherUser;
    private int userID;
    private int otherUserID;
    //Button
    private ImageButton backArrow;

    private CircleImageView profilePhoto;
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
    private TextView titleToolbar;
    //RelativeLayout (group textViews of a specific information)
    private RelativeLayout relativeLayoutAddressLine2;
    private RelativeLayout relativeLayoutSize;
    private RelativeLayout relativeLayoutShoeSize;
    private RelativeLayout relativeLayoutFavoriteColor;

    public void onBackPressed(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get UserID and go back to login if there is no
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID = prefs.getInt("userID", -1);
        if (tmpUserID != -1) {
            userID = tmpUserID;
        } else {//If no userID go back to LoginActivity
            Toast toast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            toast.show();
            Intent backToLogin = new Intent(this, LoginActivity.class);
        }
        //Get the other userID then compare with the actual user ID (-> send user to MyProfile if it's the same)
        Intent intent = getIntent();
        otherUserID =intent.getIntExtra("otherUserID",-1);
        if(otherUserID == userID){
            Intent myProfileIntent=new Intent(this,MyProfileActivity.class);
            startActivity(myProfileIntent);
        }
        setContentView(R.layout.other_profile);
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(getApplicationContext());

        otherUser = dbHelper.getUserFromID(otherUserID);

        //Rely variable with layout
        profilePhoto = findViewById(R.id.profilePhoto);
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
        actualBirthDate = findViewById(R.id.actualBirthDate);
        titleToolbar = findViewById(R.id.TitleOtherProfileToolbar);

        relativeLayoutAddressLine2 = findViewById(R.id.layoutAddressLine2);
        relativeLayoutFavoriteColor = findViewById(R.id.layoutFavoriteColor);
        relativeLayoutShoeSize = findViewById(R.id.layoutShoeSize);
        relativeLayoutSize = findViewById(R.id.layoutSize);
        visibleMode();
    }

    /*
     *Make some change in the layout for the view profile :
     * -fill textViews with actual information
     * -eventually set visibility of some layout to gone if we have no information about that
     * (ex : shoesize is undefined -> we don't see "Shoe size :")
     */
    @TargetApi(21)
    public void visibleMode(){
        if(otherUser.getProfilePhoto()!=null) {
            profilePhoto.setImageBitmap(otherUser.getProfilePhoto());
        }else{
            profilePhoto.setImageDrawable(getDrawable(R.drawable.ic_default_photo));
        }
        //Fill in with actual information
        actualPostalCode.setText(String.format("%d", otherUser.getAddress().getPostalCode()));
        actualCity.setText(otherUser.getAddress().getCity());
        actualCountry.setText(otherUser.getAddress().getCountry());
        actualAddressLine1.setText(otherUser.getAddress().getAddressLine1());
        actualFirstName.setText(otherUser.getFirstName());
        actualLastName.setText(otherUser.getLastName());
        actualBirthDate.setText(otherUser.getBirthDate().toString());
        String title= otherUser.getFirstName()+"'s Details";
        titleToolbar.setText(title);
        String addressLine2= otherUser.getAddress().getAddressLine2();
        if(addressLine2==null||addressLine2.toLowerCase().equals("null")||addressLine2.equals("")){
            relativeLayoutAddressLine2.setVisibility(View.GONE);
        }
        else{
            relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
            actualAddressLine2.setText(addressLine2);
        }
        String favoriteColor= otherUser.getFavoriteColor();
        if(favoriteColor==null||favoriteColor.toLowerCase().equals("null")||
                favoriteColor.toLowerCase().equals("undefined")||favoriteColor.equals("")){
            relativeLayoutFavoriteColor.setVisibility(View.GONE);
        }
        else{
            relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
            actualFavoriteColor.setText(favoriteColor);
        }
        String size= otherUser.getSize();
        if(size==null||size.toLowerCase().equals("null")||size.toLowerCase().equals("undefined")){
            relativeLayoutSize.setVisibility(View.GONE);
        }
        else {
            relativeLayoutSize.setVisibility(View.VISIBLE);
            actualSize.setText(size);
        }
        String shoeSize= otherUser.getShoeSize();
        if(shoeSize==null||shoeSize.toLowerCase().equals("null")||
                shoeSize.toLowerCase().equals("undefined")||shoeSize.equals("")||shoeSize.equals("0")){
            relativeLayoutShoeSize.setVisibility(View.GONE);
        }
        else{
            relativeLayoutShoeSize.setVisibility(View.VISIBLE);
            actualShoeSize.setText(shoeSize);
        }
    }

}
