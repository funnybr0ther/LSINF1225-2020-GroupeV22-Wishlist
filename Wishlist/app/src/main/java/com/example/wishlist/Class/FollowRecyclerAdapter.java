package com.example.wishlist.Class;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.Viewholder> {

    private ArrayList<User> follows = new ArrayList<>();

    public FollowRecyclerAdapter(ArrayList<User> follows) {
        this.follows = follows;
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView picture;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            picture = itemView.findViewById(R.id.picture);
        }
    }
}
