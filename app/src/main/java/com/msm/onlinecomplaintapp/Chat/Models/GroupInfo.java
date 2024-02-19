package com.msm.onlinecomplaintapp.Chat.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInfo {
    private String groupName;
    private Map<String,Object> admin;
    private Timestamp createdTime;
    private List<ChatContact> members;
    private String type;
    private String groupImageUri;
    private String gid;
    private String encryptKey;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getAdmin() {
        return admin;
    }

    public List<ChatContact> getMembers() {
        return members;
    }

    public String getGroupImageUri() {
        return groupImageUri;
    }

    public String getGroupName() {
        return groupName;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setAdmin(Map<String,Object> admin) {
        this.admin = admin;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public void setGroupImageUri(String groupImageUri) {
        this.groupImageUri = groupImageUri;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMembers(List<ChatContact> members) {
        this.members = members;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGid() {
        return gid;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("admin",admin);
        map.put("groupName",groupName);
        map.put("groupImageUri",groupImageUri);
        map.put("type",type);
        map.put("createdTime",createdTime);
        map.put("gid",gid);
        return map;
    }

    public List<Map<String,Object>> membersListMap(){
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i=0;i<members.size();i++){
            list.add(members.get(i).toMap());
        }
        return list;
    }
}
