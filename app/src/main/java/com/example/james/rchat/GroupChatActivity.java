//TODO: ADD INTENT FILTER
//TODO: DEAL WITH SPECIFIC GROUP ID ISSUE
/*
package com.example.james.rchat;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    private DatabaseReference mRootRef;

    private String mCurrentUserId;
    private EditText mChatMessageView;
    private String mCurrentGroupId;                             //TODO FIGURE OUT HOW TO LOAD WITH GROUP ID

    private final List<Messages> messagesList = new ArrayList<>();
    private List<String> userList = new ArrayList<>();    //TODO figure out data structure to merge with firebase || Need to talk to James or Cy
    //private string chatName;                                  //TODO implement group name

    private MessageAdapter mAdapter;
    private RecyclerView mMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);                 //TODO TEST IF XML IS CROSS COMPATIBLE

        Toolbar mChatToolbar = (Toolbar) findViewById(R.id.chat_app__bar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();   //get Firebase reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();            //get Firebase authentication key
        mCurrentUserId = mAuth.getCurrentUser().getUid();           //get CurrentUser Database ID

        string mUserRef = "Groups/" + mCurrentUserId + "/" + mCurrentGroupId + "/Users";    //copy userIDs to userList
        mUserRef.once("value", function(snapshot) {
            snapshot.forEach(function(childSnapshot) {
                string childData = childSnapshot.val();
                userList.add(childData);
            });
        });

        for(int i = 0; i < userList.size(); i++){ //TODO: TEST WITH GROUPCHAT
            mRootRef.child("Users").child(userList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String chat_user_name = dataSnapshot.child("name").getValue().toString();
                    getSupportActionBar().setTitle(chat_user_name);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

////////////////////////////////////////////////////////////////////////////////////////////////////
// User Interface
////////////////////////////////////////////////////////////////////////////////////////////////////
        ImageButton mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        ImageButton mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList);
        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);
        final LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom){
                if (bottom < oldBottom && ((mMessagesList.getAdapter().getItemCount() - 1) > 3)){
                    mMessagesList.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            mMessagesList.smoothScrollToPosition(
                                    mMessagesList.getAdapter().getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount){
                super.onItemRangeInserted(positionStart, itemCount);
                int count = mAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayout.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (count - 1) &&
                                lastVisiblePosition == (positionStart - 1))){
                    mMessagesList.smoothScrollToPosition(positionStart);
                }
            }
        });

        mMessagesList.setAdapter(mAdapter);
        loadMessages(); //messages loaded


        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                        //chatAddMap.put("seen",false);
                        //chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser,chatAddMap);
                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId,chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
                mMessagesList.smoothScrollToPosition(mMessagesList.getAdapter().getItemCount()); //scrolls to the bottom with new message.
            }
        });
    }

    private void loadMessages() {

        mRootRef.child("Groups").child(mCurrentUserId).child(groupID).child("Messages").addChildEventListener(new ChildEventListener() { //TODO modify to load messages from multiple people unique to the group chat -- database issue
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                messagesList.add(message); //TODO modify the message to hold the user that sent it

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {

        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "Groups/" + mCurrentUserId + "/" + groupID + "/Messages"; //TODO Change recipients for group chat instead of one user
            //String chat_user_ref = "Groups/" + mChatUser + "/" + groupID + "/Messages";

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
//            messageMap.put("seen", false);
            messageMap.put("type", "group-text");
//            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);


            String chat_user_ref = "Groups/" + mChatUser + "/" + groupID + "/Messages"; //TODO MODIFY FOR GROUPCHAT
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mChatMessageView.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG",databaseError.getMessage().toString());

                    }

                }
            });
        }
    }
}

*/