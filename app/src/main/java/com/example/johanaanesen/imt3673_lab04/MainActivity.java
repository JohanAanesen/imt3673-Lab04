package com.example.johanaanesen.imt3673_lab04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String USERNAME;

    private Array Msgs;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    RecyclerView recyclerView;
    CustomAdapter adapter;
    private ArrayList<Message> listContentArr= new ArrayList<>();


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
        myRef.setValue("FUCK YOU CUNT");

        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CustomAdapter(this);
        //Method call for populating the view
        displayChatMessages();
        saveNewMessage("Johan123", "penis");
    }


    public void displayChatMessages(){

        String userId = myRef.push().getKey();

        Message msg = new Message("Johan", "Fuck");
        listContentArr.add(msg);

        myRef.child(userId).setValue(msg);
        //We set the array to the adapter
        adapter.setListContent(listContentArr);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    public void saveNewMessage(String user, String message){


    }

}
