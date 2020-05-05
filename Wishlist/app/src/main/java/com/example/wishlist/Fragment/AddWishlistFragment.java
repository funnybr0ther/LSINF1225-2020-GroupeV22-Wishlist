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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_add_wishlist, container, false);
        userID = getArguments().getInt("userID");
        txtName = (EditText) rootView.findViewById(R.id.nameCreateWishlist);
        btnCreate = (Button) rootView.findViewById(R.id.buttonCreateWishlist);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = txtName.getText().toString();
                if(listName.matches("")) {
                    Toast toast = Toast.makeText(getContext(), "Wrong name", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(!listName.matches("")) {
                    WishlistDatabaseHelper db = new WishlistDatabaseHelper(getActivity().getApplicationContext());
                    int userID = getArguments().getInt("userID");
                    db.addWishlist(listName, userID);
                    Toast toast=Toast.makeText(getContext(),"Wishlist " + listName + " created !",Toast.LENGTH_SHORT);
                    toast.show();
                }
                /*
                AddWishlistFragment.this.getDialog().dismiss();
                Intent intent = new Intent(getContext(), ListWishlistActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            */
                // A suprimer si le code en dessous fonctionne
                ListWishlistActivity callingActivity = (ListWishlistActivity) getActivity();
                callingActivity.fragmentReturn();
                dismiss();
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
