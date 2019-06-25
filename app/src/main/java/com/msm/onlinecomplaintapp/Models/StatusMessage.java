package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class StatusMessage {
    private String message;
    private String cid;
    private Timestamp timestamp;
    private String authorId;
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMessage() {
        return message;
    }

    public String getCid() {
        return cid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("message",message);
        map.put("cid",cid);
        map.put("timestamp",timestamp);
        map.put("authorId",authorId);
        map.put("msgId",msgId);
        return map;
    }
}
