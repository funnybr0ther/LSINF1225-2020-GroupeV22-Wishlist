package com.example.wishlist.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Class.FollowListItemDecorator;
import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Class.User;
import com.example.wishlist.Fragment.SearchUserDialog;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<User> followList = new ArrayList<>();
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
        followList.add(kim);
        followList.add(sieg);
        followList.add(jeandidou);
        followList.add(vincent);
        followRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FollowListItemDecorator deco = new FollowListItemDecorator(10);
        recyclerView.addItemDecoration(deco);
        followRecyclerAdapter = new FollowRecyclerAdapter(followList);
        recyclerView.setAdapter(followRecyclerAdapter);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

    public void searchUser(View view){
        SearchUserDialog dialog = new SearchUserDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        dialog.show(FollowListActivity.this.getSupportFragmentManager(),"hein");
    }
}
