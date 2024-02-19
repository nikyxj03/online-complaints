package com.msm.onlinecomplaintapp.Chat.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage {
    private String senderUid;
    private String senderName;
    private Timestamp msgTime;
    private String message;
    private String status;
    private String id;
    private String type;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public Timestamp getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Timestamp msgTime) {
        this.msgTime = msgTime;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("type",type);
        map.put("status",status);
        map.put("senderName",senderName);
        map.put("senderUid",senderUid);
        map.put("message",message);
        map.put("msgTime",msgTime);
        return map;
    }
}
