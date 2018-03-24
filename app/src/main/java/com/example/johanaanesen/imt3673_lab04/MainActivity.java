package com.example.johanaanesen.imt3673_lab04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String USERNAME;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    RecyclerView recyclerView;
    CustomAdapter adapter;

    private ArrayList<Message> msgList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final String userChoice = shared.getString("username", null);

        if (userChoice == null ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            USERNAME = userChoice;
        }

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("messages");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    Message msg = child.getValue(Message.class);
                    msgList.add(msg);

                    refreshListAdapter();

                    recyclerView.scrollToPosition(msgList.size()-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CustomAdapter(this);
        recyclerView.scrollToPosition(msgList.size()-1);
    }

    public void addMessages(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("phone"));
        }

        System.out.println(phoneNumbers.toString());
    }


    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(msgList);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    public void sendNewMessage(View view){
        EditText editText = findViewById(R.id.sendText);
        String text = editText.getText().toString();

        if (!text.isEmpty()){
            Message msg = new Message(USERNAME, text, new Date().getTime());

            String userId = myRef.push().getKey();
            myRef.child(userId).setValue(msg);
        }
    }

}
