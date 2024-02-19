package com.msm.onlinecomplaintapp.Chat.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class MyGroup {
    private String gid;
    private Timestamp joinedTime;
    private String groupName;
    private String groupImageUri;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupImageUri(String groupImageUri) {
        this.groupImageUri = groupImageUri;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupImageUri() {
        return groupImageUri;
    }

    public Timestamp getJoinedTime() {
        return joinedTime;
    }

    public void setJoinedTime(Timestamp joinedTime) {
        this.joinedTime = joinedTime;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("joinedTime",joinedTime);
        map.put("groupImageUri",groupImageUri);
        map.put("groupName",groupName);
        map.put("gid",gid);
        return map;
    }
}
