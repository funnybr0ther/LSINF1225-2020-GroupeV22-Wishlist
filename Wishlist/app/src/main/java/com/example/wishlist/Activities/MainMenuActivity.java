package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wishlist.Fragment.ChangePasswordOrEmailDialog;
import com.example.wishlist.R;

public class MainMenuActivity extends AppCompatActivity {
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if (intent.hasExtra("userID")){
            userID= intent.getIntExtra("userID",-1);
            setContentView(R.layout.activity_main_menu);
            TextView textView=findViewById(R.id.textView);
            textView.setText(Integer.toString(userID));
        }
        else{//If no userID go back to LoginActivity
            //Toast toast=new Toast(this,);
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }
    }
    public void MyProfile(View view){
        Intent intent=new Intent(this,MyProfileActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }
    public void exampleProduct(View view){
        Intent intent1=new Intent(this, ViewProductActivity.class);
        intent1.putExtra("productID",1);
        startActivity(intent1);
    }

    public void myWishlist(View view){
        Intent gotToWishlist=new Intent(this,ListWishlistActivity.class);
        gotToWishlist.putExtra("userID",userID);
        startActivity(gotToWishlist);
    }

    public void disconnect(View view) {
        Intent intent2 = new Intent(this, LoginActivity.class);
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
}
