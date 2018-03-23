package com.example.johanaanesen.imt3673_lab04;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //Creating an arraylist of POJO objects
    private ArrayList<Message> list_members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public CustomAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.message_received, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Message list_items=list_members.get(position);
        holder.message_name.setText(list_items.getUser());
        holder.message_content.setText(list_items.getMessage());
        //holder.time.setText(list_items.getMsgTime());
    }

    //Setting the arraylist
    public void setListContent(ArrayList<Message> list_members){
        this.list_members=list_members;
        notifyItemRangeChanged(0,list_members.size());

    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message_name,message_content,message_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            message_name=(TextView)itemView.findViewById(R.id.message_name);
            message_content=(TextView)itemView.findViewById(R.id.message_content);
           // time=(TextView)itemView.findViewById(R.id.time);
        }
        @Override
        public void onClick(View v) {

        }
    }
    public void removeAt(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, list_members.size());
    }

}

