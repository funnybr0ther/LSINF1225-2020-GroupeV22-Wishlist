package com.example.wishlist.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.wishlist.Activities.DetailWishlistActivity;
import com.example.wishlist.Activities.ListWishlistActivity;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.R;

public class ChangeWishlistNameFragment extends DialogFragment {

    private EditText txtName;
    private Button btnChange;
    private Button btnDelete;
    private int wishlistID;
    private int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.frangment_change_wishlist_name, container, false);
        wishlistID = getArguments().getInt("wishlistID");
        userID = getArguments().getInt("userID");
        txtName = rootView.findViewById(R.id.nameChangeWishlist);
        btnChange = rootView.findViewById(R.id.buttonChangeWishlist);
        btnDelete = rootView.findViewById(R.id.buttonDeleteWishlist);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = txtName.getText().toString();
                if(newName.matches("")) {
                    Toast toast = Toast.makeText(getContext(), "Wrong name", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(!newName.matches("")) {
                    WishlistDatabaseHelper db = new WishlistDatabaseHelper(getActivity().getApplicationContext());
                    //int userID = getArguments().getInt("userID");
                    db.changeWishlistName(wishlistID, newName, userID);
                    Toast toast=Toast.makeText(getContext(),"Name changed !",Toast.LENGTH_SHORT);
                    toast.show();
                }
                DetailWishlistActivity callingActivity = (DetailWishlistActivity) getActivity();
                callingActivity.fragmentReturn(newName);
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishlistDatabaseHelper db = new WishlistDatabaseHelper(getActivity().getApplicationContext());
                db.deleteWishlistWithProduct(wishlistID);
                /*
                Context ctx = getActivity().getApplicationContext();
                Intent gotToWishlist=new Intent(ctx,ListWishlistActivity.class);
                gotToWishlist.putExtra("userID",userID);
                gotToWishlist.putExtra("isMyWishlist",true);
                startActivity(gotToWishlist);
*/
                DetailWishlistActivity callingActivity = (DetailWishlistActivity) getActivity();
                callingActivity.onBackPressed();
                dismiss();
            }
        });

        return rootView;
    }
}
