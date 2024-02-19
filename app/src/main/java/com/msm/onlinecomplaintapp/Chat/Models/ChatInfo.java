package com.msm.onlinecomplaintapp.Chat.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatInfo {
    private List<Map<String,Object>> users;
    private String chatId;
    private String type;
    private String encryptKey;
    private final int USERS_SIZE=2;

    public List<Map<String, Object>> getUsers() {
        return users;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setUsers(List<Map<String, Object>> users) {
        this.users = users;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("users",users);
        map.put("chatId",chatId);
        map.put("type",type);
        return map;
    }
}
