package com.msm.onlinecomplaintapp.Chat.Models;

import java.util.HashMap;
import java.util.Map;

public class ChatContact {
    private String uid;
    private String name;
    private String imageUri;
    private String type;
    private String email;
    private String phoneNo;
    private boolean enable;
    private String chatId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getType() {
        return type;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("phoneNo",phoneNo);
        map.put("type",type);
        map.put("imageUri",imageUri);
        map.put("uid",uid);
        map.put("name",name);
        map.put("email",email);
        map.put("enable",enable);
        map.put("chatId",chatId);
        return map;
    }
}
