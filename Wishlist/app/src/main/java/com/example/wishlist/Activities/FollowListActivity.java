package com.example.wishlist.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Class.Address;
import com.example.wishlist.Class.DateWish;
import com.example.wishlist.Adapters.FollowRecyclerAdapter;
import com.example.wishlist.Class.FollowListItemDecorator;
import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.Fragment.SearchUserDialog;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> followed_list=new ArrayList<User>();
    private FollowRecyclerAdapter followRecyclerAdapter;
    private LinearLayout searchToolbar;
    private LinearLayout viewToolbar;
    private EditText searchEditText;
    private ArrayList<User> allUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        recyclerView = findViewById(R.id.recyclerViewFollows);
        searchToolbar=findViewById(R.id.SearchToolbar);
        viewToolbar=findViewById(R.id.FollowListToolbar);
        searchEditText=findViewById(R.id.SearchEditText);
        UserDatabaseHelper userDatabaseHelper= new UserDatabaseHelper(getApplicationContext());
        allUser=userDatabaseHelper.getAllUser();/*new ArrayList<User>();
        allUser.add(userDatabaseHelper.getUserFromID(1));
        allUser.add(userDatabaseHelper.getUserFromID(2));
        allUser.add(userDatabaseHelper.getUserFromID(1));
        allUser.add(userDatabaseHelper.getUserFromID(2));*/
        followed_list.addAll(allUser);
        initRecyclerView();
        //followRecyclerAdapter.notifyDataSetChanged();
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
        followed_list.clear();
        if( string.length()==0){
            followed_list.addAll(allUser);
        }
        else{
            for (User user:allUser){
                String names=user.getFirstName()+user.getLastName();
                if(names.toLowerCase().contains(string.toLowerCase())){
                    followed_list.add(user);
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
        followed_list.add(kim);
        followed_list.add(sieg);
        followed_list.add(jeandidou);
        followed_list.add(vincent);
        followRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FollowListItemDecorator deco = new FollowListItemDecorator(10);
        recyclerView.addItemDecoration(deco);
        followRecyclerAdapter = new FollowRecyclerAdapter(followed_list);
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
