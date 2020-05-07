package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextMail;
    private EditText editTextPassword;
    private TextView wrongLogin;

    @TargetApi(21)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID=prefs.getInt("userID",-1);
        if(tmpUserID!=-1){
            final Intent intent=new Intent(this,MainMenuActivity.class);
            this.startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login_v2);
        this.editTextMail = this.findViewById(R.id.username);
        this.editTextPassword = this.findViewById(R.id.password);
        this.wrongLogin = this.findViewById(R.id.wrongLogin);
    }


    /*
    * Check if couple mail-password exist then go to main menu if true with userID in Extra
     */
    public void checkUserAccess(final View view){

        final String mail= this.editTextMail.getText().toString();
        final String password= this.editTextPassword.getText().toString();
        //TextView textViewMessage=(TextView) findViewById(R.id.wrongLogin);
        final UserDatabaseHelper dbHelper=new UserDatabaseHelper(this.getApplicationContext());
        final int userID=dbHelper.checkUser(mail,password);
        if (userID!=-1){
            final SharedPreferences prefs = getSharedPreferences(
                    "com.example.app", Context.MODE_PRIVATE);
            prefs.edit().putInt("userID",userID).apply();
            final Intent intent=new Intent(this,MainMenuActivity.class);
            this.startActivity(intent);
        }
        else{
            this.wrongLogin.setText("Wrong password or email");
        }
    }

    //function called onclick of text "Still not Register"
    public void newAccount(final View view){
        final Intent intent=new Intent(this,CreateNewAccountActivity.class);
        this.startActivity(intent);
    }
}
