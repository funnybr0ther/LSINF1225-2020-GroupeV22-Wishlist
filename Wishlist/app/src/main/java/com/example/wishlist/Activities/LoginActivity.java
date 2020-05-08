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

    /**
     * Look if any User is declared connected by shared preference
     * -> Send user to it's main menu if any user connected
     * -> Let user here otherwise and assign the different views to their global variable
     * @param savedInstanceState
     */
    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID=prefs.getInt("userID",-1);
        if(tmpUserID!=-1){
            Intent intent=new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_v2);
        editTextMail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        wrongLogin = findViewById(R.id.wrongLogin);
    }


    /**
     * Check if couple mail-password exist then go to main menu if true with userID in Extra
     * @param view
     */
    public void checkUserAccess(View view){

        String mail= editTextMail.getText().toString();
        String password= editTextPassword.getText().toString();
        //TextView textViewMessage=(TextView) findViewById(R.id.wrongLogin);
        UserDatabaseHelper dbHelper=new UserDatabaseHelper(getApplicationContext());
        int userID=dbHelper.checkUser(mail,password);
        if (userID!=-1){
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.example.app", Context.MODE_PRIVATE);
            prefs.edit().putInt("userID",userID).apply();
            Intent intent=new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }
        else{
            wrongLogin.setText("Wrong password or email");
        }
    }

    /**
     * Start new CreateNewAccountActivity
     * @param view
     */
    public void newAccount(View view){
        Intent intent=new Intent(this,CreateNewAccountActivity.class);
        startActivity(intent);
    }
}
