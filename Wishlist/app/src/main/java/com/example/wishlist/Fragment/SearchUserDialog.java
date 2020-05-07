package com.example.wishlist.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.wishlist.R;
import androidx.fragment.app.DialogFragment;

import com.example.wishlist.Class.FollowDatabaseHelper;

public class SearchUserDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        FollowDatabaseHelper dbHelper = new FollowDatabaseHelper(this.getActivity().getApplicationContext());
        final View view = inflater.inflate(R.layout.dialog_search_user,container,false);

        final TextView cancel = view.findViewById(R.id.searchUserCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getDialog().dismiss();
            }
        });
        EditText searchBar = view.findViewById(R.id.searchBarUser);
        return view;
    }
}
