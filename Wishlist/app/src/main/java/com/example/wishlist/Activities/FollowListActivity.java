package com.example.wishlist.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Class.FollowDatabaseHelper;
import com.example.wishlist.Class.FollowListItemDecorator;
import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Fragment.SearchUserDialog;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    int userID;

    private ArrayList<User> followList = new ArrayList<>();
    private FollowRecyclerAdapter followRecyclerAdapter;
    private LinearLayout searchToolbar;
    private LinearLayout viewToolbar;
    private EditText searchEditText;
    private ArrayList<User> allUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            userID=tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            Intent backToLogin=new Intent(this,LoginActivity.class);
        }
        setContentView(R.layout.activity_follow_list);
        recyclerView = findViewById(R.id.recyclerViewFollows);
        searchToolbar=findViewById(R.id.SearchToolbar);
        viewToolbar=findViewById(R.id.FollowListToolbar);
        searchEditText=findViewById(R.id.SearchEditText);
        initRecyclerView();
        //faireUsersOsef();
        fillFollowList();

        followRecyclerAdapter.notifyDataSetChanged();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str=searchEditText.getText().toString().toLowerCase();
                filter(str);
                followRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str=searchEditText.getText().toString().toLowerCase();
                filter(str);
                followRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=searchEditText.getText().toString().toLowerCase();
                filter(str);
                followRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
    public void filter(String string){
        followList.clear();
        if( string.length()==0){
            followList.addAll(allUser);
        }
        else{
            for (User user:allUser){
                String names=user.getFirstName()+user.getLastName();
                if(names.toLowerCase().contains(string.toLowerCase())){
                    followList.add(user);
                }
            }
        }
    }

    public void searchMode(View view){
        searchToolbar.setVisibility(View.VISIBLE);
        viewToolbar.setVisibility(View.GONE);
    }

    public void viewMode(View view){
        searchToolbar.setVisibility(View.GONE);
        viewToolbar.setVisibility(View.VISIBLE);
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

    private void fillFollowList(){
        FollowDatabaseHelper helperF = new FollowDatabaseHelper(getApplicationContext());
        UserDatabaseHelper helperU = new UserDatabaseHelper(getApplicationContext());
        helperF.addFollow(1,2,"ami");
        for(int id:helperF.getFollows(userID)){
            followList.add(helperU.getUserFromID(id));
        }
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
}
