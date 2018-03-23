package com.example.johanaanesen.imt3673_lab04;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Message {
    private long d;
    private String u;
    private String m;

    public Message(){
    }

    public Message(String u, String m){
        this.d = new Date().getTime();
        this.u = u;
        this.m = m;
    }

    public String getMessage(){
        return this.m;
    }

    public String getUser(){
        return this.u;
    }

    public long getMsgTime(){
        return this.d;
    }

    public void setMessage(String m){
        this.m = m;
    }

    public void setMsgTime(long d){
        this.d = d;
    }

    public void setUser(String u){
        this.u = u;
    }
}
