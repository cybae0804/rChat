package com.example.james.rchat;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter  extends  RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<Messages> mMessageList;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();


    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;


    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout,parent,false);

        return new MessageViewHolder(v);

    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
//        public TextView timeText;
        public ImageView profileImage;

        public MessageViewHolder(View view) {
            super(view);


            messageText = (TextView) view.findViewById(R.id.message_text_layout);
//            timeText = (TextView) view.findViewById(R.id.message_item_time);
            profileImage = (ImageView) view.findViewById(R.id.message_profile_layout);
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int i) {

        String current_user_id = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();

        if(from_user.equals(current_user_id)){
            viewHolder.messageText.setBackgroundResource(R.drawable.user_message_text_background);
            //viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.WHITE);

        }else{

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);

        }

        viewHolder.messageText.setText(c.getMessage());
//        viewHolder.timeText.setText(c.getTime());
        //profile image setting here
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}
