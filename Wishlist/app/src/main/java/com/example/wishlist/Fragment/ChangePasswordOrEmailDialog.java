package com.example.wishlist.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.wishlist.Activities.ChangePasswordOrEmailActivity;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.R;

public class ChangePasswordOrEmailDialog extends DialogFragment {
    int userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userID=getArguments().getInt("userID");
        final UserDatabaseHelper dbHelper=new UserDatabaseHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.dialog_change_password_or_email, container, false);
        //Cancel
        TextView cancel = view.findViewById(R.id.changePasswordCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordOrEmailDialog.this.getDialog().dismiss();
            }
        });
        final EditText checkPassword=view.findViewById(R.id.password);
        Button okButton=view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = checkPassword.getText().toString();
                if (dbHelper.checkPassword(userID, password)) {
                    Intent intent = new Intent(getContext(), ChangePasswordOrEmailActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                    ChangePasswordOrEmailDialog.this.getDialog().dismiss();
                }
                else{
                    Toast toast=Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return view;
    }
}
