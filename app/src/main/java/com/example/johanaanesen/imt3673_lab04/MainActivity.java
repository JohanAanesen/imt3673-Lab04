package com.example.johanaanesen.imt3673_lab04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private String USERNAME;

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
    }




}
