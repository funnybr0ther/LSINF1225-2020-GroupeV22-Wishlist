package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.ProductDatabaseHelper;
import com.example.wishlist.Fragment.ChangePasswordOrEmailDialog;
import com.example.wishlist.R;

public class MainMenuActivity extends AppCompatActivity {
    private int userID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent= this.getIntent();
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1) {
            this.userID = tmpUserID;
            this.setContentView(R.layout.activity_main_menu);
            //TextView textView=findViewById(R.id.textView);
            //textView.setText(Integer.toString(userID));
        }
        /*if (intent.hasExtra("userID")){
            userID= intent.getIntExtra("userID",-1);
            setContentView(R.layout.activity_main_menu);
            TextView textView=findViewById(R.id.textView);
            textView.setText(Integer.toString(userID));
        }*/
        else{//If no userID go back to LoginActivity
            final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            final Intent backToLogin=new Intent(this,LoginActivity.class);
        }
    }
    public void MyProfile(final View view){
        final Intent intent=new Intent(this,MyProfileActivity.class);
        intent.putExtra("userID", this.userID);
        this.startActivity(intent);
    }

    public void myWishlist(final View view){
        final Intent gotToWishlist=new Intent(this,ListWishlistActivity.class);
        gotToWishlist.putExtra("userID", this.userID);
        gotToWishlist.putExtra("isMyWishlist",true);
        this.startActivity(gotToWishlist);
    }

    public void disconnect(final View view) {
        final Intent intent2 = new Intent(this, LoginActivity.class);
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        prefs.edit().remove("userID").apply();
        this.startActivity(intent2);
    }
    public void changePasswordOrEmail(final View view){
        final ChangePasswordOrEmailDialog dialog=new ChangePasswordOrEmailDialog();
        final Bundle args = new Bundle();
        args.putInt("userID", this.userID);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(),"he");
    }

    public void viewHistory(final View view){
        final Intent intent = new Intent(this, PurchaseViewActivity.class);
        this.startActivity(intent);
    }

    public void viewFollowList(final View view){
        final Intent intent = new Intent(this, FollowListActivity.class);
        this.startActivity(intent);
    }

    public void viewAllUsers(final View view){
        final Intent intent = new Intent(this, FindFollowActivity.class);
        this.startActivity(intent);
    }

//    public void goToFriendWishlist(View view){
//        Intent intent = new Intent(this,ListWishlistActivity.class);
//        intent.putExtra("receiverID",1); //id de celui a qui appartient les wishlist
//        intent.putExtra("userID",userID); //id de celui qui consulte les wishlist de ses amis
//        intent.putExtra("isMyWishlist",false);
//        startActivity(intent);
//    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                final int pID = data.getIntExtra("newProduct",-1);
                Toast.makeText(this, "Product ID= "+pID, Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult
}