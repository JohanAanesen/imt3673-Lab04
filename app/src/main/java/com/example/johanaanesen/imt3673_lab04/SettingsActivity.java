package com.example.johanaanesen.imt3673_lab04;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    static final String FREQ_CHOICE = "freq-choice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerSelect();
    }

    private void spinnerSelect(){

        //spinner
        final Spinner spinner =  findViewById(R.id.freqSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.freq_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get spinner shared state
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int userChoice = shared.getInt(FREQ_CHOICE, 1);

        spinner.setSelection(userChoice);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt(FREQ_CHOICE, position); // Storing integer
                    editor.apply(); // commit changes

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Error?
            }
        });
    }
}
