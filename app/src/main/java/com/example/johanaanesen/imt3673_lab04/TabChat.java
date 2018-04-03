package com.example.johanaanesen.imt3673_lab04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class TabChat extends Fragment {

    private String USERNAME;
    private EditText newMsg;
    private Button btn;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    RecyclerView recyclerView;
    CustomAdapter adapter;

    private ArrayList<Message> msgList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabchat, container, false);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("messages");
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        USERNAME = shared.getString("username", null);

        init(rootView);

        return rootView;
    }

    private void init(View rootView){

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    Message msg = child.getValue(Message.class);
                    msgList.add(msg);

                    refreshListAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycleViewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new CustomAdapter(getContext(), USERNAME);

        newMsg = rootView.findViewById(R.id.sendText);
        btn = (Button)rootView.findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View paramView){
                sendNewMessage();
            }
        });

        refreshListAdapter();
    }

    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(msgList);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        //Scroll to bottom :)
        recyclerView.scrollToPosition(msgList.size()-1);
    }

    public void sendNewMessage(){
        String text = newMsg.getText().toString();

        if (!text.isEmpty()){
            Message msg = new Message(USERNAME, text, new Date().getTime());

            String userId = myRef.push().getKey();
            myRef.child(userId).setValue(msg);
        }

        newMsg.setText("");
    }
}
