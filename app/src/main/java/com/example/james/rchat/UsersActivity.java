package com.example.james.rchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private RecyclerView mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolBar = (Toolbar) findViewById(R.id.users_appBar);
        getSupportActionBar(mToolBar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsersList = (RecyclerView) findViewById(R.id.users_list);

    }
}
