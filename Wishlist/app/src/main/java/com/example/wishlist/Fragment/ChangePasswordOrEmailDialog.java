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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        this.userID = this.getArguments().getInt("userID");
        final UserDatabaseHelper dbHelper=new UserDatabaseHelper(this.getActivity().getApplicationContext());
        final View view = inflater.inflate(R.layout.dialog_change_password_or_email, container, false);
        //Cancel
        final TextView cancel = view.findViewById(R.id.changePasswordCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getDialog().dismiss();
            }
        });
        final EditText checkPassword=view.findViewById(R.id.password);
        final Button okButton=view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String password = checkPassword.getText().toString();
                if (dbHelper.checkPassword(ChangePasswordOrEmailDialog.this.userID, password)) {
                    final Intent intent = new Intent(ChangePasswordOrEmailDialog.this.getContext(), ChangePasswordOrEmailActivity.class);
                    intent.putExtra("userID", ChangePasswordOrEmailDialog.this.userID);
                    ChangePasswordOrEmailDialog.this.startActivity(intent);
                    getDialog().dismiss();
                }
                else{
                    final Toast toast=Toast.makeText(ChangePasswordOrEmailDialog.this.getContext(),"Wrong Password",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return view;
    }
}
