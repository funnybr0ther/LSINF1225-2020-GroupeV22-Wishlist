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
    UserDatabaseHelper dbHelper;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.change_password_or_email);
        this.editTextPassword = this.findViewById(R.id.newPswrd);
        this.editTextMail = this.findViewById(R.id.newmail);
        this.editTextConfirmPassword = this.findViewById(R.id.confirmPswrd);
        this.textViewEmail = this.findViewById(R.id.wrongEmail);
        this.textViewPassword = this.findViewById(R.id.wrongPassword);
        this.textViewConfPassword = this.findViewById(R.id.wrongConfirmPassword);
        this.dbHelper = new UserDatabaseHelper(this.getApplicationContext());
        final Intent intent= this.getIntent();

        //Get UserID and go back to login if there is no
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            this.userID =tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            final Intent backToLogin=new Intent(this,LoginActivity.class);
        }

        this.user = this.dbHelper.getUserFromID(this.userID);
        this.editTextMail.setText(this.user.getEmail());
        this.editTextPassword.setText(this.user.getPassword());
        this.editTextConfirmPassword.setText(this.user.getPassword());
    }

    /*
     *Check if password :
     * is at least 5 char long
     * contains at least a uppercase letter
     * contains at least a number (digit ???)
     * Show some text or not depending of that
     */
    public boolean checkPassword(final String password){
        if (password.length()<5){
            this.textViewPassword.setText("Your password must contain at least 5 characters");
            return false;
        }
        if(password.equals(password.toLowerCase())){
            this.textViewPassword.setText("Your password must contain at least 1 uppercase letter");
            return false;
        }
        final boolean containsNumber= this.containsNumber(password);
        if (!containsNumber){
            this.textViewPassword.setText("Your password must contain at least 1 number");//comment on dit chiffre?
            return false;
        }
        this.textViewPassword.setText("");
        return true;
    }

    public boolean containsNumber(final String string){
        for (final char c:string.toCharArray()){
            if(Character.isDigit(c))return true;
        }
        return false;
    }

    /*
     *Check if email is at least 5 char long and contains an @
     * Show some text or not depending of that
     */
    public boolean checkEmail(final String email){
        final UserDatabaseHelper dbHelper= new UserDatabaseHelper(this.getApplicationContext());
        if(!email.contains("@")||email.length()<5){
            this.textViewEmail.setText("Please insert a correct email");
            return false;
        }
        else if(email.equals(this.user.getEmail())){
            return true;
        }
        else if(!dbHelper.checkMail(email)){
            this.textViewEmail.setText("This email is already used");
            return false;
        }
        else {
            this.textViewEmail.setText("");
            return true;
        }
    }
    public void updatePassword(final View view){
        final String password= this.editTextPassword.getText().toString();
        final String confirmPassword= this.editTextConfirmPassword.getText().toString();
        final String mail= this.editTextMail.getText().toString();
        if (this.checkEmail(mail)& this.checkPassword(password)){
            if (password.equals(confirmPassword)){
                this.user.setPassword(password);
                this.user.setEmail(mail);
                this.dbHelper.updateUser(this.user, this.userID);
                final Toast toast=Toast.makeText(this,"Account updated",Toast.LENGTH_SHORT);
                toast.show();
                final Intent intent=new Intent(this,MainMenuActivity.class);
                intent.putExtra("userID", this.userID);
                this.startActivity(intent);
            }
            else {
                this.textViewConfPassword.setText("You don't write the same password");
            }
        }
    }
}
