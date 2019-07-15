package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UserQuery {
    private String question;
    private String did;
    private String deptName;
    private String uid;
    private String userName;
    private Timestamp timestamp;
    private String reply;
    private String qid;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getDid() {
        return did;
    }

    public String getQuestion() {
        return question;
    }

    public String getUid() {
        return uid;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("question",question);
        map.put("timestamp",timestamp);
        map.put("reply",reply);
        map.put("uid",uid);
        map.put("did",did);
        map.put("deptName",deptName);
        map.put("userName",userName);
        map.put("qid",qid);
        return map;
    }
}
