package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wishlist.Class.DatabaseHelper;
import com.example.wishlist.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginActivity extends AppCompatActivity {

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_v2);
    }


    /*
    * Check if couple mail-password exist then go to main menu if it with userID in Extra
     */
    public void checkUserAccess(View view){
        EditText editTextMail= (EditText)findViewById(R.id.username);
        String mail= editTextMail.getText().toString();
        EditText editTextPassword=(EditText)findViewById(R.id.password);
        String password=editTextPassword.getText().toString();
        //TextView textViewMessage=(TextView) findViewById(R.id.wrongLogin);
        DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
        int userID=dbHelper.checkUser(mail,password);
        if (userID!=-1){

        Intent intent=new Intent(this,MainMenuActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);
        }
        else{
            TextView textViewMessage=(TextView) findViewById(R.id.wrongLogin);
            textViewMessage.setText("Wrong password or email");
        }
    }

    public void newAccount(View view){//function called when touch text "Still not Register"
        Intent intent=new Intent(this,CreateNewAccountActivity.class);
        startActivity(intent);
    }
}
