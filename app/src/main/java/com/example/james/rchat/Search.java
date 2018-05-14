package com.example.james.rchat;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class Search extends AppCompatActivity {

    private EditText searchUser;
    private Button userButton;
    private RecyclerView resultList;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        searchUser = (EditText) findViewById(R.id.User_Name);
        userButton = (Button) findViewById(R.id.user_search_button);

        resultList = (RecyclerView) findViewById(R.id.result_list);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setHasFixedSize(true);

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchUser.getText().toString();
                firebaseUserSearch(searchText);
            }
        });
    }

    private void firebaseUserSearch(String searchText) {
        Query SearchQuery = userDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<UsersSearch> options =
                new FirebaseRecyclerOptions.Builder<UsersSearch>()
                        .setQuery(SearchQuery, UsersSearch.class)
                        .build();

        FirebaseRecyclerAdapter<UsersSearch, UsersSearchViewHolder> adapterTwo = new FirebaseRecyclerAdapter<UsersSearch, UsersSearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(UsersSearchViewHolder holder, int position, UsersSearch model) {
                holder.setDetails(model.getName(), model.getStatus());
            }

            @Override
            public UsersSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_layout, parent, false);
                return new UsersSearchViewHolder(view);
            }
        };

        resultList.setAdapter(adapterTwo);

    }

    public static class UsersSearchViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersSearchViewHolder (View itemView)
        {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(String user_Name, String userStatus){
            TextView user_name = (TextView) mView.findViewById(R.id.layout_user);
            TextView user_status = (TextView) mView.findViewById((R.id.layout_status));

            user_name.setText(user_Name);
            user_status.setText(userStatus);

        }
    }

}
