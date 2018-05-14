package com.example.james.rchat;

import android.os.Bundle;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.List;


public class GroupChatActivity {
    private String mChatUser;
    private DatabaseReference mRootRef;
    private String mCurrentUserID;
    private EditText mChatMessageView;
    private final List<Messages> messagesList = new ArrayList<>();
    private MessageAdapter mAdapter;

    @Override
    protected void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

}
