package com.example.james.rchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsFragment extends Fragment {

    private RecyclerView mGroupsList;

    private DatabaseReference mConversationDatabase;
//    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

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
            public GroupsFragment.GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.groups_single_layout, parent, false);

                return new GroupsFragment.GroupsViewHolder(view);
            }
            @Override
            public void onBindViewHolder(final GroupsFragment.GroupsViewHolder groupsViewHolder, int position, Groups model) {


//                final String list_group_id = getRef(position).getKey();

//                Query lastMessageQuery = mConversationDatabase.child(list_group_id).limitToLast(1);

//                lastMessageQuery.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                        String data = dataSnapshot.child("message").getValue().toString();
//                        conversationViewHolder.setText(data);
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

//                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        final String userName = dataSnapshot.child("name").getValue().toString();
//                        String userThumb = dataSnapshot.child("image").getValue().toString();
//
//                        conversationViewHolder.setName(userName);
//                        conversationViewHolder.setUserImage(userThumb, getContext());
//
//                        conversationViewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//
//                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                chatIntent.putExtra("user_id", list_user_id);
//                                chatIntent.putExtra("user_name", userName);
//                                startActivity(chatIntent);
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


//              TODO: get click to open chat working
//                final String user_id = getRef(position).getKey();
//                ConversationViewHolder.mView.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view){
//
//                        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                        chatIntent.putExtra("user_id", user_id);
//                        startActivity(chatIntent);
//                    }
//                });
            }
        };
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public GroupsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

//        public void setText(String message){
//            TextView messageView = (TextView) mView.findViewById(R.id.user_single_status);
//            messageView.setText(message);
//        }
//
//        public void setUserImage(String thumb_image, Context ctx){
//
//            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
//            Picasso.get().load(thumb_image).placeholder(R.drawable.default_pic).into(userImageView);
//
//        }
    }
}
