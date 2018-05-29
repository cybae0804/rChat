package com.example.james.rchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {


    private String mChatUser;
    private String GroupName;

    private Toolbar mChatToolbar;
    private String mGroupID;
    private DatabaseReference mRootRef;
    private DatabaseReference mGroupRef;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private TextView mCurrentlyTyping;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;

    private String mCurrentUserId;

    private EditText mChatMessageView;
    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private Button mAddUserBtn;


    private final List<Messages> messagesList = new ArrayList<>();
    private MessageAdapter mAdapter;
    private RecyclerView mMessagesList;


    private static final int GALLERY_PICK = 1;

    // Firebase Storage
    private StorageReference mImageStorage;

    ///// move this
//    private void dispatchTakeVideoIntent() {
//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mChatToolbar = (Toolbar) findViewById(R.id.chat_app__bar);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

//        mChatUser = getIntent().getStringExtra("user_id");
//        String userName = getIntent().getStringExtra("user_name");
        mGroupID = getIntent().getStringExtra("group_id");
        mGroupRef = mRootRef.child("GroupData").child(mGroupID);
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupName = dataSnapshot.child("groupName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(action_bar_view);

        // Custom Action Bar Items

        mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.custom);

        mTitleView.setText(GroupName);
//
//        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String online = dataSnapshot.child("online").getValue().toString();
//                String image = dataSnapshot.child("image").getValue().toString();
//
//                if(online.equals("true")){
//                    mLastSeenView.setText("Online");
//                }else{
//
//                    GetTimeAgo getTimeAgo = new GetTimeAgo();
//
//                    long lastTime = Long.parseLong(online);
//
//                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
//
//                    mLastSeenView.setText(lastSeenTime);
//
////                    mLastSeenView.setText(online);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        mAddUserBtn = (Button) findViewById(R.id.add_users_btn);
        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mCurrentlyTyping = (TextView) findViewById(R.id.currently_typing_text);
        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);
//        mChatMessageView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(s)) {
//                    mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("typing").setValue(false);
//                } else {
//                    mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("typing").setValue(true);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });

        //------IMAGE STORAGE-------------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mAdapter = new MessageAdapter(messagesList);
        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);


        final LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setItemViewCacheSize(20);
        mMessagesList.setDrawingCacheEnabled(true);
        mMessagesList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
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

        loadMessages();
//        mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("typing").addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(DataSnapshot snapsnap){
//                if (snapsnap.exists()) {
//                    if (snapsnap.getValue().toString() == "true") {
//                        mCurrentlyTyping.setText("Typing...");
//                    } else {
//                        mCurrentlyTyping.setText("");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(!dataSnapshot.hasChild(mChatUser)){
//
//                    Map chatAddMap = new HashMap();
//
//                    chatAddMap.put("seen",false);
//                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
//                    chatAddMap.put("typing",false);
//
//                    Map chatUserMap = new HashMap();
//                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser,chatAddMap);
//                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId,chatAddMap);
//
//                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                            if(databaseError != null){
//
//                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
//
//                            }
//
//                        }
//                    });
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        // need new add image button clicker

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
                mMessagesList.smoothScrollToPosition(mMessagesList.getAdapter().getItemCount()); //scrolls to the bottom with new message.
            }
        });

        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        mAddUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent addUserIntent = new Intent(GroupChatActivity.this, GroupUserSearch.class);
                addUserIntent.putExtra("groupID", mGroupID);
                startActivity(addUserIntent);
            }
        });


    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            //added
            //Uri videoUri = data.getData();
//
            final String current_user_ref = "messages/";
//            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mGroupRef.child("messages").push();

            final String push_id = user_message_push.getKey();

            StorageReference filepath = mImageStorage.child("message_images").child(push_id);
            //added
            //StorageReference vidFilepath = mVideoStorage.child("message_videos").child(push_id + ".mp4 ");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        String download_uri = task.getResult().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message", download_uri);
//                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
//                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

                        mChatMessageView.setText("");

                        mGroupRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError != null){

                                    Log.d("CHAT_LOG",databaseError.getMessage().toString());

                                }

                            }
                        });

                    }
                }
            });
        }
    }

    private void loadMessages() {

        mGroupRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                messagesList.add(message);

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

            String current_user_ref = "messages";

            DatabaseReference user_message_push = mGroupRef.child("messages").push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
//            messageMap.put("seen", false);
            messageMap.put("type", "text");
//            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put("messages" + "/" + push_id, messageMap);

            mChatMessageView.setText("");

            mGroupRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
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
