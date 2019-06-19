package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Complaint  {

    private String title;
    private String desc;
    private int supportno;
    private String ciuri;
    private String cid;
    private String acm; //archive mode
    private String amode; //ananymous mode
    private String mode; //publish mode
    private String dept;
    private String uid;
    private Timestamp time;
    private String om;

    public String getTitle(){
        return title;
    }

    public void setAcm(String acm) {
        this.acm = acm;
    }

    public void setAmode(String amode) {
        this.amode = amode;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setCiuri(String ciuri) {
        this.ciuri = ciuri;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setSupportno(int supportno) {
        this.supportno = supportno;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setOm(String om) {
        this.om = om;
    }

    public int getSupportno(){
        return supportno;
    }

    public String getCiuri(){return ciuri;}

    public String getDesc(){return desc; }

    public String getCid(){
        return cid;
    }

    public String getAcm(){
        return acm;
    }

    public String getAmode(){
        return amode;
    }

    public String getMode(){
        return mode;
    }

    public String getDept(){
        return dept;
    }

    public String getUid(){
        return uid;
    }

    public Timestamp getTime(){
        return time;
    }

    public String getOm() {
        return om;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result=new HashMap<>();
        result.put("title",title );
        result.put("cid",cid );
        result.put("ciuri",ciuri );
        result.put("uid",uid );
        result.put("dept",dept );
        result.put("desc",desc );
        result.put("time",time );
        result.put("mode",mode );
        result.put("acm",acm );
        result.put("amode",amode );
        result.put("supportno",supportno );
        result.put("om",om);
        return result;
    }

}
