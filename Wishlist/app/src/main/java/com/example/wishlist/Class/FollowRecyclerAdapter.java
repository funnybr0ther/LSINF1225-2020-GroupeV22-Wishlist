package com.example.wishlist.Class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.Viewholder> {

    private ArrayList<User> followList;

    public FollowRecyclerAdapter(ArrayList<User> follows) {
        this.followList = follows;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        String full_name = followList.get(position).getFirstName()+" "+followList.get(position).getLastName();
        holder.name.setText(full_name);
        //holder.picture.set comment on set des images mdrrr?

    }

    @Override
    public int getItemCount() { return followList.size(); }


     class Viewholder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView picture;

         Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.followName);
            picture = itemView.findViewById(R.id.followPicture);
        }
    }
}
