package com.example.johanaanesen.imt3673_lab04;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private String USERNAME;

    private CustomPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private int updateFrequencySeconds;

    PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final String userChoice = shared.getString("username", null);
        //new users need to pick a nickname :)
        if (userChoice == null ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            USERNAME = userChoice;
        }

        mSectionsPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.tabContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        getPrefs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabChat tab1 = new TabChat();
                    return tab1;
                case 1:
                    TabUsers tab2 = new TabUsers();
                    return tab2;
                default:
                    TabChat defTab = new TabChat();
                    return defTab;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public void onBackPressed(){ // https://stackoverflow.com/a/42615612 answer i've used :)
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(alarmIntent != null) {
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        getPrefs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), updateFrequencySeconds * 1000, alarmIntent);

        startService(notificationIntent);
    }

    public void getPrefs(){
        // Get spinner shared state from Preferences
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int freqSpinner = shared.getInt("freq-spinner", -1);

        switch (freqSpinner){
            case 0: updateFrequencySeconds = 5*60; //5 min in seconds // 300
                break;
            case 1: updateFrequencySeconds = 15*60; //15 min in seconds // 900
                break;
            case 2: updateFrequencySeconds = 60*60; //1 hour in seconds // 86400
                break;
            case 3: updateFrequencySeconds = 24*60*60; //1 day in seconds // 86400
                break;
            default: updateFrequencySeconds = 5*60; //default 5 min
                break;
        }
    }

}
