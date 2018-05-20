package com.example.james.rchat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private EditText searchUser;
    private Button userButton;
    private RecyclerView resultList;
    private DatabaseReference userDatabase;

    private RecyclerView mConvList;

    private DatabaseReference mConversationDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_chats, container, false);
        mConvList = (RecyclerView) mMainView.findViewById(R.id.chatsList);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConversationDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);
        mConversationDatabase.keepSynced(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);

        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linLayout = new LinearLayoutManager(getContext());
        linLayout.setReverseLayout(true);
        linLayout.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linLayout);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        conversationsDisplay();

    }

    public void conversationsDisplay(){
        Query messageQuery = mMessageDatabase.child("messages").limitToLast(1);

        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(messageQuery, Messages.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Messages, ChatsFragment.ConversationViewHolder>(options) {
            @Override
            public ChatsFragment.ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent, false);

                return new ChatsFragment.ConversationViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ChatsFragment.ConversationViewHolder conversationViewHolder, int position, Messages model) {
                ConversationViewHolder.setName(model.getFrom());
                ConversationViewHolder.setText(model.getMessage());
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

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setText(String message){
            TextView messageView = (TextView) mView.findViewById(R.id.user_single_status);
            messageView.setText(message);
        }
    }
}
