package com.example.wishlist.Fragment;

import android.app.Activity;
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

import com.example.wishlist.Activities.CreateProfileActivity;
import com.example.wishlist.R;

import java.io.File;

public class ChangePhotoDialog extends DialogFragment {
    public final int CAMERA_REQUEST_CODE=3;
    public final int MEMORY_REQUEST_CODE=456;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_change_photo,container,false);
        //Cancel
        TextView cancel=view.findViewById(R.id.changePhotoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhotoDialog.this.getDialog().dismiss();
            }
        });
        //Take Photo
        TextView takePhoto=view.findViewById(R.id.changePhotoTakeNew);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
            }
        });

        //Choose Photo From Memory
        TextView selectPhoto=view.findViewById(R.id.changePhotoMemory);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if take a new Photo
        if(requestCode==CAMERA_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            CreateProfileActivity activity= (CreateProfileActivity) getActivity();
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            activity.changedPhoto(bitmap);
        }
        //Result if choose photo from Memory
        if(requestCode==MEMORY_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            Uri selectedImageUri=data.getData();
            File file= new File(selectedImageUri.toString());
        }
    }
}
