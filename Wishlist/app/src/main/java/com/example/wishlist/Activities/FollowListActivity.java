package com.example.wishlist.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.wishlist.Class.User;
import com.example.wishlist.Class.UserDatabaseHelper;
import com.example.wishlist.R;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity implements FollowRecyclerAdapter.FollowerOnClickListener {

    private RecyclerView recyclerView;

    int userID;

    private final ArrayList<User> followList = new ArrayList<>();
    private FollowRecyclerAdapter followRecyclerAdapter;
    private LinearLayout searchToolbar;
    private LinearLayout viewToolbar;
    private EditText searchEditText;
    private final ArrayList<User> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final int tmpUserID=prefs.getInt("userID",-1);
        if (tmpUserID!=-1){
            this.userID =tmpUserID;
        }
        else{//If no userID go back to LoginActivity
            final Toast toast=Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT);
            toast.show();
            final Intent backToLogin=new Intent(this,LoginActivity.class);
        }
        this.setContentView(R.layout.activity_follow_list);
        this.recyclerView = this.findViewById(R.id.recyclerViewFollows);
        this.searchToolbar = this.findViewById(R.id.SearchToolbar);
        this.viewToolbar = this.findViewById(R.id.FollowListToolbar);
        this.searchEditText = this.findViewById(R.id.SearchEditText);
        this.initRecyclerView();


        this.fillFollowList();


        this.followRecyclerAdapter.notifyDataSetChanged();
        this.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                final String str= FollowListActivity.this.searchEditText.getText().toString().toLowerCase();
                FollowListActivity.this.filter(str);
                FollowListActivity.this.followRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str= FollowListActivity.this.searchEditText.getText().toString().toLowerCase();
                FollowListActivity.this.filter(str);
                FollowListActivity.this.followRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String str= FollowListActivity.this.searchEditText.getText().toString().toLowerCase();
                FollowListActivity.this.filter(str);
                FollowListActivity.this.followRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
    public void filter(final String string){
        this.filteredList.clear();
        if( string.length()==0){
            this.filteredList.addAll(this.followList);
        }
        else{
            for (final User user: this.followList){
                final String names=user.getFirstName()+user.getLastName();
                if(names.toLowerCase().contains(string.toLowerCase())){
                    this.filteredList.add(user);
                }
            }
        }
    }

    public void searchMode(final View view){
        this.searchToolbar.setVisibility(View.VISIBLE);
        this.viewToolbar.setVisibility(View.GONE);
    }

    public void viewMode(final View view){
        this.searchToolbar.setVisibility(View.GONE);
        this.viewToolbar.setVisibility(View.VISIBLE);
    }


    private void fillFollowList(){
        this.followList.clear();
        this.filteredList.clear();
        final FollowDatabaseHelper helperF = new FollowDatabaseHelper(this.getApplicationContext());
        final UserDatabaseHelper helperU = new UserDatabaseHelper(this.getApplicationContext());
        for(final int id:helperF.getFollows(this.userID)){
            this.followList.add(helperU.getUserFromID(id));
        }
        this.filteredList.addAll(this.followList);
        this.followRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        final FollowListItemDecorator deco = new FollowListItemDecorator(10);
        this.recyclerView.addItemDecoration(deco);
        this.followRecyclerAdapter = new FollowRecyclerAdapter(this.filteredList,this);
        this.recyclerView.setAdapter(this.followRecyclerAdapter);
    }

    public void onBackPressed(final View view) {
        this.onBackPressed();
    }

    @Override
    public void onFollowerClick(final int position) {
        final Intent otherProfileIntent=new Intent(this,OtherProfileMenuActivity.class);
        final int userID = this.followList.get(position).getUserID();
        Log.d("TAG", "onFollowerClick: " + userID);
        otherProfileIntent.putExtra("receiverID", this.followList.get(position).getUserID());
        this.startActivityForResult(otherProfileIntent,1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            this.fillFollowList();
        }
    }
}
