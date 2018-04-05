package com.example.johanaanesen.imt3673_lab04;


import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Message {

    public String user;
    public String message;
    public String time;

    public Message(){
    }

    public Message(String user, String message, String time){
        this.user = user;
        this.message = message;
        this.time = time;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUser(){
        return this.user;
    }

    public String getTime(){
        return this.time;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setUser(String user){
        this.user = user;
    }
}
