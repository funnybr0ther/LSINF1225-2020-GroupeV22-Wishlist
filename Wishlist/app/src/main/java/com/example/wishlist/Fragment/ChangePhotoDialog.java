package com.example.wishlist.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.wishlist.Activities.CreateProfileActivity;
import com.example.wishlist.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangePhotoDialog extends DialogFragment {
    public final int CAMERA_REQUEST_CODE = 3;
    public final int MEMORY_REQUEST_CODE = 456;

    public interface OnPhotoReceivedListener {
        public void getBitmapImage(Bitmap bitmap);

        public void setImagePath(String ImagePath);

        public void getUriImage(Uri uri);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        onPhotoReceivedListener.setImagePath(image.getAbsolutePath());
        return image;
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
                ChangePhotoDialog.this.getDialog().dismiss();
            }
        });
        //Take Photo
        TextView takePhoto = view.findViewById(R.id.changePhotoTakeNew);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast toast = Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        });

        //Choose Photo From Memory
        TextView selectPhoto = view.findViewById(R.id.photoFromMemory);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memoryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                memoryIntent.setType("image/*");
                /*File photoFile=null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast toast=Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                    memoryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }*/
                startActivityForResult(memoryIntent, MEMORY_REQUEST_CODE);
                //getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if take a new Photo
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CreateProfileActivity activity = (CreateProfileActivity) getActivity();
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            onPhotoReceivedListener.getBitmapImage(bitmap);
            getDialog().dismiss();
        }
        //Result if choose photo from Memory
        if (requestCode == MEMORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            //File file= new File(selectedImageUri.toString());
            onPhotoReceivedListener.getUriImage(selectedImageUri);
            getDialog().dismiss();
        }
    }
}
