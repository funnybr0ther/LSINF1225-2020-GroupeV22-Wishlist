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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate( R.layout.frangment_change_wishlist_name, container, false);
        this.wishlistID = this.getArguments().getInt("wishlistID");
        this.userID = this.getArguments().getInt("userID");
        this.txtName = rootView.findViewById(R.id.nameChangeWishlist);
        this.btnChange = rootView.findViewById(R.id.buttonChangeWishlist);
        this.btnDelete = rootView.findViewById(R.id.buttonDeleteWishlist);

        this.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String newName = ChangeWishlistNameFragment.this.txtName.getText().toString();
                if(newName.matches("")) {
                    final Toast toast = Toast.makeText(ChangeWishlistNameFragment.this.getContext(), "Wrong name", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(!newName.matches("")) {
                    final WishlistDatabaseHelper db = new WishlistDatabaseHelper(ChangeWishlistNameFragment.this.getActivity().getApplicationContext());
                    //int userID = getArguments().getInt("userID");
                    db.changeWishlistName(ChangeWishlistNameFragment.this.wishlistID, newName, ChangeWishlistNameFragment.this.userID);
                    final Toast toast=Toast.makeText(ChangeWishlistNameFragment.this.getContext(),"Name changed !",Toast.LENGTH_SHORT);
                    toast.show();
                }
                final DetailWishlistActivity callingActivity = (DetailWishlistActivity) ChangeWishlistNameFragment.this.getActivity();
                callingActivity.fragmentReturn(newName);
                ChangeWishlistNameFragment.this.dismiss();
            }
        });

        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final WishlistDatabaseHelper db = new WishlistDatabaseHelper(ChangeWishlistNameFragment.this.getActivity().getApplicationContext());
                db.deleteWishlistWithProduct(ChangeWishlistNameFragment.this.wishlistID);
                /*
                Context ctx = getActivity().getApplicationContext();
                Intent gotToWishlist=new Intent(ctx,ListWishlistActivity.class);
                gotToWishlist.putExtra("userID",userID);
                gotToWishlist.putExtra("isMyWishlist",true);
                startActivity(gotToWishlist);
*/
                final DetailWishlistActivity callingActivity = (DetailWishlistActivity) ChangeWishlistNameFragment.this.getActivity();
                callingActivity.onBackPressed();
                ChangeWishlistNameFragment.this.dismiss();
            }
        });

        return rootView;
    }
}
