package com.example.wishlist.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.R;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if (intent.hasExtra("userID")){
            Integer userID= intent.getIntExtra("userID",-1);
            setContentView(R.layout.activity_main_menu);
            TextView textView=findViewById(R.id.textView);
            textView.setText(userID.toString());
        }
        else{//If no userID go back to LoginActivity
            //Toast toast=new Toast(this,);
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }
        Intent intent1=new Intent(this, ViewProductActivity.class);
        startActivity(intent1);
    }
}
