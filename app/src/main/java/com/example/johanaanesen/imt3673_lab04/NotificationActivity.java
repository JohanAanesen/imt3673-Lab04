package com.example.johanaanesen.imt3673_lab04;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends IntentService {
    static int oldMessages = 0;

    private NotificationChannel channel;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private PendingIntent pendingIntent;
    private SharedPreferences preferences;

    private Context context = this;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = mDatabase.getReference("messages");

    public NotificationActivity(){super("NotficationActivity");}

    @Override
    public void onHandleIntent(@Nullable Intent intent) {

        this.notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        // Create the NotificationChannel only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.channel = new NotificationChannel(
                    context.getString(R.string.channel_id),
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.channel_description));

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, channel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentMessages = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    currentMessages++;
                }

                if (currentMessages > oldMessages) {
                    sendNotification(currentMessages-oldMessages);
                }

                oldMessages = currentMessages;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // rip
            }
        });

    }

    private void sendNotification(int diff) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        // Build the Notification
        this.builder
                .setSmallIcon(R.drawable.ic_forum_black_36dp)
                .setContentTitle(diff+"New messages!")
                .setContentText("Check them out now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(this.pendingIntent);
        this.notificationManager.notify(0, builder.build());

    }
}
