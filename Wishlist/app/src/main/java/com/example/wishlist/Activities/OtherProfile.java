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

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get UserID and go back to login if there is no
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID = prefs.getInt("userID", -1);
        if (tmpUserID != -1) {
            this.userID = tmpUserID;
        } else {//If no userID go back to LoginActivity
            final Toast toast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            toast.show();
            final Intent backToLogin = new Intent(this, LoginActivity.class);
        }
        //Get the other userID then compare with the actual user ID (-> send user to MyProfile if it's the same)
        final Intent intent = this.getIntent();
        this.otherUserID =intent.getIntExtra("otherUserID",-1);
        if(this.otherUserID == this.userID){
            final Intent myProfileIntent=new Intent(this,MyProfileActivity.class);
            this.startActivity(myProfileIntent);
        }
        this.setContentView(R.layout.other_profile);
        final UserDatabaseHelper dbHelper = new UserDatabaseHelper(this.getApplicationContext());

        this.otherUser = dbHelper.getUserFromID(this.otherUserID);

        //Rely variable with layout
        this.profilePhoto = this.findViewById(R.id.profilePhoto);
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
        this.actualBirthDate = this.findViewById(R.id.actualBirthDate);
        this.titleToolbar = this.findViewById(R.id.TitleOtherProfileToolbar);

        this.relativeLayoutAddressLine2 = this.findViewById(R.id.layoutAddressLine2);
        this.relativeLayoutFavoriteColor = this.findViewById(R.id.layoutFavoriteColor);
        this.relativeLayoutShoeSize = this.findViewById(R.id.layoutShoeSize);
        this.relativeLayoutSize = this.findViewById(R.id.layoutSize);
        this.visibleMode();
    }

    /*
     *Make some change in the layout for the view profile :
     * -fill textViews with actual information
     * -eventually set visibility of some layout to gone if we have no information about that
     * (ex : shoesize is undefined -> we don't see "Shoe size :")
     */
    @TargetApi(21)
    public void visibleMode(){
        if(this.otherUser.getProfilePhoto()!=null) {
            this.profilePhoto.setImageBitmap(this.otherUser.getProfilePhoto());
        }else{
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }
        //Fill in with actual information
        this.actualPostalCode.setText(String.format("%d", this.otherUser.getAddress().getPostalCode()));
        this.actualCity.setText(this.otherUser.getAddress().getCity());
        this.actualCountry.setText(this.otherUser.getAddress().getCountry());
        this.actualAddressLine1.setText(this.otherUser.getAddress().getAddressLine1());
        this.actualFirstName.setText(this.otherUser.getFirstName());
        this.actualLastName.setText(this.otherUser.getLastName());
        this.actualBirthDate.setText(this.otherUser.getBirthDate().toString());
        final String title= this.otherUser.getFirstName()+"'s Details";
        this.titleToolbar.setText(title);
        final String addressLine2= this.otherUser.getAddress().getAddressLine2();
        if(addressLine2==null||addressLine2.toLowerCase().equals("null")||addressLine2.equals("")){
            this.relativeLayoutAddressLine2.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutAddressLine2.setVisibility(View.VISIBLE);
            this.actualAddressLine2.setText(addressLine2);
        }
        final String favoriteColor= this.otherUser.getFavoriteColor();
        if(favoriteColor==null||favoriteColor.toLowerCase().equals("null")||
                favoriteColor.toLowerCase().equals("undefined")||favoriteColor.equals("")){
            this.relativeLayoutFavoriteColor.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutFavoriteColor.setVisibility(View.VISIBLE);
            this.actualFavoriteColor.setText(favoriteColor);
        }
        final String size= this.otherUser.getSize();
        if(size==null||size.toLowerCase().equals("null")||size.toLowerCase().equals("undefined")){
            this.relativeLayoutSize.setVisibility(View.GONE);
        }
        else {
            this.relativeLayoutSize.setVisibility(View.VISIBLE);
            this.actualSize.setText(size);
        }
        final String shoeSize= this.otherUser.getShoeSize();
        if(shoeSize==null||shoeSize.toLowerCase().equals("null")||
                shoeSize.toLowerCase().equals("undefined")||shoeSize.equals("")||shoeSize.equals("0")){
            this.relativeLayoutShoeSize.setVisibility(View.GONE);
        }
        else{
            this.relativeLayoutShoeSize.setVisibility(View.VISIBLE);
            this.actualShoeSize.setText(shoeSize);
        }
    }

}
