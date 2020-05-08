package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Activities.OtherProfile;
import com.example.wishlist.Class.FollowDatabaseHelper;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.Viewholder> {

    private final ArrayList<User> followList;
    private final FollowerOnClickListener onClickListener;
    private final Context context;
    private final int userID;

    public FollowRecyclerAdapter(ArrayList<User> follows , FollowerOnClickListener followerOnClickListener, Context context, int userID) {
        this.followList = follows;
        this.onClickListener = followerOnClickListener;
        this.context = context;
        this.userID = userID;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_item,parent,false);
        return new Viewholder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        FollowDatabaseHelper dbh = new FollowDatabaseHelper(context);

        String full_name = followList.get(position).getFirstName()+" "+ followList.get(position).getLastName();
        holder.name.setText(full_name);
        holder.picture.setImageBitmap(followList.get(position).getProfilePhoto());
        holder.rel.setText(dbh.getRelationship(userID,followList.get(position).getUserID())); //////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public int getItemCount() { return followList.size(); }


     class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView rel;
        ImageView picture;
        FollowerOnClickListener followerOnClickListener;

         Viewholder(@NonNull View itemView, FollowerOnClickListener followerOnClickListener) {
            super(itemView);
             name = itemView.findViewById(R.id.followName);
             picture = itemView.findViewById(R.id.followPicture);
             rel = itemView.findViewById(R.id.followRelation);
            this.followerOnClickListener=followerOnClickListener;
            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
             followerOnClickListener.onFollowerClick(getAdapterPosition());
         }
     }
    public interface FollowerOnClickListener{
        void onFollowerClick(int position);
    }
}
