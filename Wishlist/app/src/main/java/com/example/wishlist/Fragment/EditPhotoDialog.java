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
        void getBitmapImage(Bitmap bitmap);

        void setUriImage(Uri uri);
    }

    OnPhotoReceivedListener onPhotoReceivedListener;

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        try {
            this.onPhotoReceivedListener = (OnPhotoReceivedListener) this.getActivity();

        } catch (final ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_change_photo, container, false);
        //Cancel
        final TextView cancel = view.findViewById(R.id.changePhotoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getDialog().dismiss();
            }
        });
        //Take Photo
        final TextView takePhoto = view.findViewById(R.id.changePhotoTakeNew);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                EditPhotoDialog.this.startActivityForResult(cameraIntent, EditPhotoDialog.this.CAMERA_REQUEST_CODE);
            }
        });

        //Choose Photo From Memory
        final TextView selectPhoto = view.findViewById(R.id.photoFromMemory);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent memoryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                memoryIntent.setType("image/*");
                EditPhotoDialog.this.startActivityForResult(memoryIntent, EditPhotoDialog.this.MEMORY_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if take a new Photo
        if (requestCode == this.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final MyProfileActivity activity = (MyProfileActivity) this.getActivity();
            final Bundle extras = data.getExtras();
            final Bitmap bitmap = (Bitmap) extras.get("data");
            this.onPhotoReceivedListener.getBitmapImage(bitmap);
            this.getDialog().dismiss();
        }
        //Result if choose photo from Memory
        if (requestCode == this.MEMORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            this.onPhotoReceivedListener.setUriImage(selectedImageUri);
            this.getDialog().dismiss();
        }
    }
}
