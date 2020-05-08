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

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextConfPswrd;
    private TextView textViewPassword;
    private TextView textViewEmail;
    private TextView textViewConfPassword;

    /**
     * Assign the views to their relative global variable
     * @param savedInstanceState
     */
    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_v2);
        //Set different view/Edit
        editTextMail = findViewById(R.id.newmail);
        editTextPassword = findViewById(R.id.newPswrd);
        editTextConfPswrd = findViewById(R.id.confirmPswrd);
        textViewPassword = findViewById(R.id.wrongPassword);
        textViewEmail = findViewById(R.id.wrongEmail);
        textViewConfPassword = findViewById(R.id.wrongConfirmPassword);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

    public boolean containsNumber(String string){
        for (char c:string.toCharArray()){
            if(Character.isDigit(c))return true;
        }
        return false;
    }

    /**
     *Check if password :
     * is at least 5 char long
     * contains at least a uppercase letter
     * contains at least a number (digit ???)
     * Show some text or not depending of that
     * @param password password to check
     * @return boolean
     */
    public boolean checkPassword(String password){
        if (password.length()<5){
            textViewPassword.setText("Your password must contain at least 5 characters");
            return false;
        }
        if(password.equals(password.toLowerCase())){
            textViewPassword.setText("Your password must contain at least 1 uppercase letter");
            return false;
        }
        boolean containsNumber= containsNumber(password);
        if (!containsNumber){
            textViewPassword.setText("Your password must contain at least 1 number");
            return false;
        }
        textViewPassword.setText("");
        return true;
    }

    /**
     * Check if email is at least 5 char long and contains an @
     * Then check if email is not already used by another user
     * Show some text to help the user or not depending of that
     * @param email
     * @return boolean
     */
    public boolean checkEmail(String email){
        UserDatabaseHelper dbHelper= new UserDatabaseHelper(getApplicationContext());
        if(!email.contains("@")||email.length()<5){
            textViewEmail.setText("Please insert a correct email");
            return false;
        }
        else if(!dbHelper.checkMail(email)){
            textViewEmail.setText("This email is already used");
            return false;
        }
        else {
            textViewEmail.setText("");
            return true;
        }
    }

    /**
     * Check if the password and the email are correct and if the two password are the same
     * If all is ok start CreateProfileActivity with the mail and password put in extra
     * Show some text to help user otherwise
     * @param view
     */
    public void checkSignInInfo(View view){
        String mail= editTextMail.getText().toString();
        String password= editTextPassword.getText().toString();
        String confirmPassword= editTextConfPswrd.getText().toString();
        if(checkEmail(mail)& checkPassword(password)){
            if(password.equals(confirmPassword)){
                Intent intent =new Intent(this,CreateProfileActivity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("password",password);
                startActivity(intent);
            }
            else{
                textViewConfPassword.setText("You don't write the same password");
            }
        }
    }
}
