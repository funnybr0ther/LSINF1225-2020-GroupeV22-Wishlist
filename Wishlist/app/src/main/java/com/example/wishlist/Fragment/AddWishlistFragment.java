package com.example.wishlist.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.wishlist.Class.WishlistDatabaseHelper;
import com.example.wishlist.R;

public class AddWishlistFragment extends DialogFragment {

    private EditText txtName;
    private Button btnCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_add_wishlist, container, false);

        txtName = (EditText) rootView.findViewById(R.id.nameCreateWishlist);
        btnCreate = (Button) rootView.findViewById(R.id.buttonCreateWishlist);

        return rootView;
    }



    public void PressBtnCreate(View view){
        if(txtName.getText().toString() != "") {
            WishlistDatabaseHelper db = new WishlistDatabaseHelper(getContext());

        }
    }

}
