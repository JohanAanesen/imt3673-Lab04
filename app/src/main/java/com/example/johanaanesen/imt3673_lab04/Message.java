package com.example.johanaanesen.imt3673_lab04;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Message {

    public String user;
    public String message;
    public long date;

    public Message(){
    }

    public Message(String user, String message){
        this.date = new Date().getTime();
        this.user = user;
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUser(){
        return this.user;
    }

    public long getDate(){
        return this.date;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setDate(long date){
        this.date = date;
    }

    public void setUser(String user){
        this.user = user;
    }
}
