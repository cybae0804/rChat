package com.example.james.rchat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GroupsFragment extends Fragment {

    private RecyclerView mGroupsList;

    private DatabaseReference mConversationDatabase;
//    private DatabaseReference mMessageDatabase;
//    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_groups
                , container, false);
        mGroupsList = (RecyclerView) mMainView.findViewById(R.id.groupsList);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConversationDatabase = FirebaseDatabase.getInstance().getReference().child("Groups").child(mCurrent_user_id);
        mConversationDatabase.keepSynced(true);

//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
//        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linLayout = new LinearLayoutManager(getContext());
        linLayout.setReverseLayout(true);
        linLayout.setStackFromEnd(true);

        mGroupsList.setHasFixedSize(true);
        mGroupsList.setLayoutManager(linLayout);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        groupsDisplay();
    }

    public void groupsDisplay(){
        Query groupsQuery = mConversationDatabase;

        FirebaseRecyclerOptions<Groups> options =
                new FirebaseRecyclerOptions.Builder<Groups>()
                .setQuery(groupsQuery, Groups.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Groups, GroupsFragment.GroupsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }
}
