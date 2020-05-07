package com.example.wishlist.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.Class.FollowDatabaseHelper;
import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileMenuActivity extends AppCompatActivity {

    private int userID;
    private int receiverID;
    private User otherUser;
    private CircleImageView profilePhoto;
    private TextView titleToolbar;
    private Button followButton;
    private Button unfollowButton;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.receiverID =intent.getIntExtra("receiverID",-1);
        this.setContentView(R.layout.other_profile_menu);

        final UserDatabaseHelper dbHelperU = new UserDatabaseHelper(this.getApplicationContext());
        this.profilePhoto = this.findViewById(R.id.profilePhoto);
        this.otherUser = dbHelperU.getUserFromID(this.receiverID);

        this.followButton = this.findViewById(R.id.followButton);
        this.unfollowButton = this.findViewById(R.id.unfollowButton);

        this.actualiseButtons();
        this.visibleMode();
    }

    public void actualiseButtons(){
        final FollowDatabaseHelper dbHelperF = new FollowDatabaseHelper(this.getApplicationContext());

        if(dbHelperF.checkIfFollows(this.userID, this.receiverID)){
            this.followButton.setVisibility(View.GONE);
            this.unfollowButton.setVisibility(View.VISIBLE);
        }else{
            this.unfollowButton.setVisibility(View.GONE);
            this.followButton.setVisibility(View.VISIBLE);
        }
    }
    @TargetApi(21)
    public void visibleMode() {
        if (this.otherUser.getProfilePhoto() != null) {
            this.profilePhoto.setImageBitmap(this.otherUser.getProfilePhoto());
        } else {
            this.profilePhoto.setImageDrawable(this.getDrawable(R.drawable.ic_default_photo));
        }
        this.titleToolbar = this.findViewById(R.id.TitleOtherProfileToolbar);
        final String title= this.otherUser.getFirstName()+" " + this.otherUser.getLastName();
        this.titleToolbar.setText(title);
    }

    public void seeDetails(final View view){
        final Intent seeProfileIntent = new Intent(this,OtherProfile.class);
        seeProfileIntent.putExtra("otherUserID", this.receiverID);
        this.startActivity(seeProfileIntent);
    }

    public void onBackPressed(final View view) {
        final Intent returnIntent = new Intent();
        this.setResult(Activity.RESULT_OK, returnIntent);
        this.finish();
    }

    public void goToFriendWishlist(final View view){
        Log.d("SUPEER", "goToFriendWishlist: " + this.receiverID);
        final Intent intent = new Intent(this,ListWishlistActivity.class);
        intent.putExtra("receiverID", this.receiverID); //id de celui a qui appartient les wishlist
        intent.putExtra("userID", this.userID); //id de celui qui consulte les wishlist de ses amis
        intent.putExtra("isMyWishlist",false);
        this.startActivity(intent);
    }

    public void followCurrentUser(final View view){
        final FollowDatabaseHelper helper = new FollowDatabaseHelper(this.getApplicationContext());
        helper.addFollow(this.userID, this.receiverID,"Friend");
        this.actualiseButtons();
    }

    public void unfollowCurrentUser(final View view){
        final FollowDatabaseHelper helper = new FollowDatabaseHelper(this.getApplicationContext());
        helper.unfollow(this.userID, this.receiverID);
        this.actualiseButtons();
    }
}
