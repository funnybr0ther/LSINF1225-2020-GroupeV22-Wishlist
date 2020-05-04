package com.example.wishlist.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Class.FollowRecyclerAdapter;
import com.example.wishlist.Class.User;
import com.example.wishlist.R;

import java.util.ArrayList;

public class ViewFollowsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<User> followed_list = new ArrayList<>();
    private FollowRecyclerAdapter followRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        recyclerView = findViewById(R.id.recyclerViewFollows);

        initRecyclerView();
        faireUsersOsef();
    }

    private void faireUsersOsef(){
        User kim = new User(new Address("Ici","La","Nob",12),"Kim","Mens","kim.mens@hotmail.com",new DateWish(11,"Janvier",1903),"1234aA");
        User sieg = new User(new Address("Ici","La","Nob",12),"Siegfried","Nijsen","kim.mens@hotmail.com",new DateWish(11,"Janvier",1903),"1234aA");
        User jeandidou = new User(new Address("Ici","La","Nob",12),"Jean-Didou","Legat","kim.mens@hotmail.com",new DateWish(11,"Janvier",1903),"1234aA");
        User vincent = new User(new Address("Ici","La","Nob",12),"Vincent","Legat","kim.mens@hotmail.com",new DateWish(11,"Janvier",1903),"1234aA");
        followed_list.add(kim);
        followed_list.add(sieg);
        followed_list.add(jeandidou);
        followed_list.add(vincent);
        followRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        followRecyclerAdapter = new FollowRecyclerAdapter(followed_list);
        recyclerView.setAdapter(followRecyclerAdapter);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}
