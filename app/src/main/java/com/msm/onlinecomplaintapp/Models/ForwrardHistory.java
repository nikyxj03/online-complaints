package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ForwrardHistory {
    private String from;
    private String to;
    private Timestamp timestamp;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("from",from);
        map.put("to",to);
        map.put("timestamp",timestamp);
        return map;
    }
}