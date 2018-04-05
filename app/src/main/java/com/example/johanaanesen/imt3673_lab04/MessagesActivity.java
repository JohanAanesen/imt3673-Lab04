package com.example.johanaanesen.imt3673_lab04;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private String USERNAME;
    private String USERS_NICKNAME;

    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private ArrayList<Message> msgList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("messages");
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        USERNAME = shared.getString("username", null);

        //Get chosen users name
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USERS_NICKNAME = extras.getString("users_nickname");
        }

        init();



    }


    private void init(){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    Message msg = child.getValue(Message.class);

                    if(msg.getUser().equals(USERS_NICKNAME)) {
                        msgList.add(msg);

                        refreshListAdapter();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // rip
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycleViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CustomAdapter(this, USERNAME);
    }

    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(msgList);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        //Scroll to bottom :)
        recyclerView.scrollToPosition(msgList.size()-1);
    }
}
