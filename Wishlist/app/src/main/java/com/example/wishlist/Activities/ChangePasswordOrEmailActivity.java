package com.example.wishlist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Fragment.ChangePasswordOrEmailDialog;
import com.example.wishlist.R;

import static java.security.AccessController.getContext;

public class ChangePasswordOrEmailActivity extends AppCompatActivity {
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextMail;
    private TextView textViewPassword;
    private TextView textViewEmail;
    private TextView textViewConfPassword;
    private int userID;
    private User user;
    private UserDatabaseHelper dbHelper;

    /**
     * Need nothing special as parameter to be created
     * Look if any User is declared connected by shared preference
     * -> Set the actual password and email of the user if any connected
     * -> Go to login activity otherwise
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_or_email);
        editTextPassword = findViewById(R.id.newPswrd);
        editTextMail = findViewById(R.id.newmail);
        editTextConfirmPassword = findViewById(R.id.confirmPswrd);
        textViewEmail = findViewById(R.id.wrongEmail);
        textViewPassword = findViewById(R.id.wrongPassword);
        textViewConfPassword = findViewById(R.id.wrongConfirmPassword);
        dbHelper = new UserDatabaseHelper(getApplicationContext());

        //Get UserID and go back to login if there is no
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            userID =tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }

        user = dbHelper.getUserFromID(userID);
        editTextMail.setText(user.getEmail());
        editTextPassword.setText(user.getPassword());
        editTextConfirmPassword.setText(user.getPassword());
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
            textViewPassword.setText("Your password must contain at least 1 number");//comment on dit chiffre?
            return false;
        }
        textViewPassword.setText("");
        return true;
    }


    public boolean containsNumber(String string){
        for (char c:string.toCharArray()){
            if(Character.isDigit(c))return true;
        }
        return false;
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
        else if(email.equals(user.getEmail())){
            return true;
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
     * Then update the user with the (new) mail and password if all is ok
     * Show some text to help user otherwise
     * @param view
     */
    public void updatePassword(View view){
        String password= editTextPassword.getText().toString();
        String confirmPassword= editTextConfirmPassword.getText().toString();
        String mail= editTextMail.getText().toString();
        if (checkEmail(mail)& checkPassword(password)){
            if (password.equals(confirmPassword)){
                user.setPassword(password);
                user.setEmail(mail);
                dbHelper.updateUser(user, userID);
                Toast toast=Toast.makeText(this,"Account updated",Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(this,MainMenuActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
            else {
                textViewConfPassword.setText("You don't write the same password");
            }
        }
    }
}
