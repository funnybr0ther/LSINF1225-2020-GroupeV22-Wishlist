package com.example.wishlist.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wishlist.Activities.MyProfileActivity;
import com.example.wishlist.R;

public class EditPhotoDialog extends DialogFragment {
    public final int CAMERA_REQUEST_CODE = 2903;
    public final int MEMORY_REQUEST_CODE = 13;

    public interface OnPhotoReceivedListener {
        public void getBitmapImage(Bitmap bitmap);

        public void setUriImage(Uri uri);
    }

    OnPhotoReceivedListener onPhotoReceivedListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onPhotoReceivedListener = (OnPhotoReceivedListener) getActivity();

        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_photo, container, false);
        //Cancel
        TextView cancel = view.findViewById(R.id.changePhotoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPhotoDialog.this.getDialog().dismiss();
            }
        });
        //Take Photo
        TextView takePhoto = view.findViewById(R.id.changePhotoTakeNew);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });

        //Choose Photo From Memory
        TextView selectPhoto = view.findViewById(R.id.photoFromMemory);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memoryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                memoryIntent.setType("image/*");
                startActivityForResult(memoryIntent, MEMORY_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if take a new Photo
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            MyProfileActivity activity = (MyProfileActivity) getActivity();
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            onPhotoReceivedListener.getBitmapImage(bitmap);
            getDialog().dismiss();
        }
        //Result if choose photo from Memory
        if (requestCode == MEMORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            onPhotoReceivedListener.setUriImage(selectedImageUri);
            getDialog().dismiss();
        }
    }
}
