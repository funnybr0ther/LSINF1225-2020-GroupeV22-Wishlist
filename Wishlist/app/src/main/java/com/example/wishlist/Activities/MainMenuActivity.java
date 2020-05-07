package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            userID=tmpUserID;
            setContentView(R.layout.activity_main_menu);
            TextView textView=findViewById(R.id.textView);
            textView.setText(Integer.toString(userID));
        }
        /*if (intent.hasExtra("userID")){
            userID= intent.getIntExtra("userID",-1);
            setContentView(R.layout.activity_main_menu);
            TextView textView=findViewById(R.id.textView);
            textView.setText(Integer.toString(userID));
        }*/
        else{//If no userID go back to LoginActivity
            Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }
    }
    public void MyProfile(View view){
        Intent intent=new Intent(this,MyProfileActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }

    public void myWishlist(View view){
        Intent gotToWishlist=new Intent(this,ListWishlistActivity.class);
        gotToWishlist.putExtra("userID",userID);
        gotToWishlist.putExtra("isMyWishlist",true);
        startActivity(gotToWishlist);
    }

    public void disconnect(View view) {
        Intent intent2 = new Intent(this, LoginActivity.class);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        prefs.edit().remove("userID").apply();
        startActivity(intent2);
    }
    public void changePasswordOrEmail(View view){
        ChangePasswordOrEmailDialog dialog=new ChangePasswordOrEmailDialog();
        Bundle args = new Bundle();
        args.putInt("userID", userID);
        dialog.setArguments(args);
        dialog.show(MainMenuActivity.this.getSupportFragmentManager(),"he");
    }

    public void viewHistory(View view){
        Intent intent = new Intent(this, PurchaseViewActivity.class);
        startActivity(intent);
    }

    public void viewFollowList(View view){
        Intent intent = new Intent(this, FollowListActivity.class);
        startActivity(intent);
    }


    public void viewAllUsers(View view){
        Intent intent = new Intent(this, FindFollowActivity.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                int pID = data.getIntExtra("newProduct",-1);
                Toast.makeText(this, "Product ID= "+pID, Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult
}