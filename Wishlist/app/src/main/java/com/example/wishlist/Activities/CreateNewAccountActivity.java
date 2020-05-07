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

    @Override
    @TargetApi(26)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_new_account_v2);
        //Set different view/Edit
        this.editTextMail = this.findViewById(R.id.newmail);
        this.editTextPassword = this.findViewById(R.id.newPswrd);
        this.editTextConfPswrd = this.findViewById(R.id.confirmPswrd);
        this.textViewPassword = this.findViewById(R.id.wrongPassword);
        this.textViewEmail = this.findViewById(R.id.wrongEmail);
        this.textViewConfPassword = this.findViewById(R.id.wrongConfirmPassword);

        //Je sais pas ce que c'est autofill mais ça à l'air stylé -> à voir plus tard
        this.editTextMail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
    }

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }

    public boolean containsNumber(final String string){
        for (final char c:string.toCharArray()){
            if(Character.isDigit(c))return true;
        }
        return false;
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
        else if(!dbHelper.checkMail(email)){
            this.textViewEmail.setText("This email is already used");
            return false;
        }
        else {
            this.textViewEmail.setText("");
            return true;
        }
    }

    /*
    *Check if mail and password are good then if the two password are the same
    * Access to CreateProfile to finish registration if nothing goes wrong
    * Do nothing in the other case (just show some text to help the user)
     */
    public void checkSignInInfo(final View view){
        final String mail= this.editTextMail.getText().toString();
        final String password= this.editTextPassword.getText().toString();
        final String confirmPassword= this.editTextConfPswrd.getText().toString();
        if(this.checkEmail(mail)& this.checkPassword(password)){
            if(password.equals(confirmPassword)){
                final Intent intent =new Intent(this,CreateProfileActivity.class);
                intent.putExtra("mail",mail);
                intent.putExtra("password",password);
                this.startActivity(intent);
            }
            else{
                this.textViewConfPassword.setText("You don't write the same password");
            }
        }
    }
}
