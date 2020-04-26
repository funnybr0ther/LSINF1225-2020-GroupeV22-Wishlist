package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_v2);
        editTextMail= (EditText)findViewById(R.id.username);
        editTextPassword=(EditText)findViewById(R.id.password);
        wrongLogin=(TextView) findViewById(R.id.wrongLogin);
    }


    /*
    * Check if couple mail-password exist then go to main menu if it with userID in Extra
     */
    public void checkUserAccess(View view){

        String mail= editTextMail.getText().toString();
        String password=editTextPassword.getText().toString();
        //TextView textViewMessage=(TextView) findViewById(R.id.wrongLogin);
        UserDatabaseHelper dbHelper=new UserDatabaseHelper(getApplicationContext());
        int userID=dbHelper.checkUser(mail,password);
        if (userID!=-1){

        Intent intent=new Intent(this,MainMenuActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);
        }
        else{
            wrongLogin.setText("Wrong password or email");
        }
    }

    public void newAccount(View view){//function called when touch text "Still not Register"
        Intent intent=new Intent(this,CreateNewAccountActivity.class);
        startActivity(intent);
    }
}
