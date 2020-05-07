package com.example.wishlist.Fragment;

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

import com.example.wishlist.Activities.ListWishlistActivity;
import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.R;

public class AddWishlistFragment extends DialogFragment {

    private EditText txtName;
    private Button btnCreate;
    private int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate( R.layout.fragment_add_wishlist, container, false);
        this.userID = this.getArguments().getInt("userID");
        this.txtName = rootView.findViewById(R.id.nameCreateWishlist);
        this.btnCreate = rootView.findViewById(R.id.buttonCreateWishlist);

        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String listName = AddWishlistFragment.this.txtName.getText().toString();
                if(listName.matches("")) {
                    final Toast toast = Toast.makeText(AddWishlistFragment.this.getContext(), "Wrong name", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(!listName.matches("")) {
                    final WishlistDatabaseHelper db = new WishlistDatabaseHelper(AddWishlistFragment.this.getActivity().getApplicationContext());
                    final int userID = AddWishlistFragment.this.getArguments().getInt("userID");
                    db.addWishlist(listName, userID);
                    final Toast toast=Toast.makeText(AddWishlistFragment.this.getContext(),"Wishlist " + listName + " created !",Toast.LENGTH_SHORT);
                    toast.show();
                }
                /*
                AddWishlistFragment.this.getDialog().dismiss();
                Intent intent = new Intent(getContext(), ListWishlistActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            */
                // A suprimer si le code en dessous fonctionne
                final ListWishlistActivity callingActivity = (ListWishlistActivity) AddWishlistFragment.this.getActivity();
                callingActivity.fragmentReturn();
                AddWishlistFragment.this.dismiss();
            }
        });
        return rootView;
    }



    /*public void PressBtnCreate(View view){
        String listName = txtName.getText().toString();
        if(listName != "") {
            WishlistDatabaseHelper db = new WishlistDatabaseHelper(getActivity().getApplicationContext());

            Toast toast=Toast.makeText(getContext(),"Wishlist " + listName + " created !",Toast.LENGTH_SHORT);
            toast.show();
        }
        Toast toast=Toast.makeText(getContext(),"Wrong name",Toast.LENGTH_SHORT);
        toast.show();
    }*/

}
