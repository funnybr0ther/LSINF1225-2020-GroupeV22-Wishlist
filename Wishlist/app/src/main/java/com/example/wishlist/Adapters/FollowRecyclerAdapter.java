package com.example.wishlist.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Activities.OtherProfile;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.Viewholder> {

    private final ArrayList<User> followList;
    private final FollowerOnClickListener onClickListener;

    public FollowRecyclerAdapter(final ArrayList<User> follows , final FollowerOnClickListener followerOnClickListener) {
        followList = follows;
        onClickListener =followerOnClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_item,parent,false);
        return new Viewholder(view, this.onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {

        final String full_name = this.followList.get(position).getFirstName()+" "+ this.followList.get(position).getLastName();
        holder.name.setText(full_name);
        holder.picture.setImageBitmap(this.followList.get(position).getProfilePhoto());
    }

    @Override
    public int getItemCount() { return this.followList.size(); }


     class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView picture;
        FollowerOnClickListener followerOnClickListener;

         Viewholder(@NonNull final View itemView, final FollowerOnClickListener followerOnClickListener) {
            super(itemView);
             this.name = itemView.findViewById(R.id.followName);
             this.picture = itemView.findViewById(R.id.followPicture);
            this.followerOnClickListener=followerOnClickListener;
            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(final View v) {
             this.followerOnClickListener.onFollowerClick(this.getAdapterPosition());
         }
     }
    public interface FollowerOnClickListener{
        void onFollowerClick(int position);
    }
}
