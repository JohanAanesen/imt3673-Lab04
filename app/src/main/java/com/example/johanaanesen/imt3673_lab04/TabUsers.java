package com.example.johanaanesen.imt3673_lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class TabUsers extends Fragment {

    private String USERNAME;

    private ArrayList<User> users;
    private UserAdapter adapter;

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabusers, container, false);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("users");
        this.recyclerView = (RecyclerView)rootView.findViewById(R.id.recycleViewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        USERNAME = shared.getString("username", null);

        users = new ArrayList<>();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = child.getValue(String.class);
                    User user = new User(name);
                    users.add(user);
                }

                //sorting alphabetically with function from https://stackoverflow.com/a/2784576
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                refreshListAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        adapter = new UserAdapter(getContext());

        refreshListAdapter();
        return rootView;
    }

    public void refreshListAdapter(){
        //We set the array to the adapter
        adapter.setListContent(users);
        //We in turn set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

        Context context;
        private ArrayList<User> users=new ArrayList<>();
        private final LayoutInflater inflater;

        public UserAdapter(Context context) {
            this.context = context;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.users_list, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            User user=users.get(position);
            holder.nameTxt.setText(user.getName());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        //Setting the arraylist
        public void setListContent(ArrayList<User> users){
            this.users=users;
            notifyItemRangeChanged(0, users.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView nameTxt;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                nameTxt = (TextView) itemView.findViewById(R.id.userName);
            }
            @Override
            public void onClick(View v) {
                Log.i("TEST:", "onClick: "+nameTxt.getText().toString());

                Intent intent = new Intent(getContext(), MessagesActivity.class);
                intent.putExtra("users_nickname", nameTxt.getText().toString());
                startActivity(intent);
            }

        }
    }
}
