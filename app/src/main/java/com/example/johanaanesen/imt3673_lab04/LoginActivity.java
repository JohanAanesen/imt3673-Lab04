package com.example.johanaanesen.imt3673_lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private ArrayList<String> userList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("users");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    String user = child.getValue(String.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Error :)
            }
        });
    }


    public void goMain(View view){
        EditText editText = findViewById(R.id.editTextUserName);
        String userName = editText.getText().toString();

        if(!checkIfUnique(userName)) {
            if (!userName.isEmpty()){
                String userId = myRef.push().getKey();
                myRef.child(userId).setValue(userName);
            }

            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = shared.edit();

            editor.putString("username", userName);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Nickname taken";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public boolean checkIfUnique(String user){
        return userList.contains(user);
    }
}
