package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class ChapterMember {
    private String name;
    private String phoneNo;
    private String uid;
    private String email;
    private String posType;
    private String posName;
    private String imageUri;
    private String chapid;
    private String chapName;
    private String chapLogoUri;

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPosName() {
        return posName;
    }

    public String getPosType() {
        return posType;
    }

    public void setEmail(String emmail) {
        this.email = email;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public void setPosType(String posType) {
        this.posType = posType;
    }

    public String getChapid() {
        return chapid;
    }

    public String getChapName() {
        return chapName;
    }

    public void setChapid(String chapid) {
        this.chapid = chapid;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public String getChapLogoUri() {
        return chapLogoUri;
    }

    public void setChapLogoUri(String chapLogoUri) {
        this.chapLogoUri = chapLogoUri;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("posType",posType);
        map.put("posName",posName);
        map.put("chapName",chapName);
        map.put("chapid",chapid);
        map.put("name",name);
        map.put("uid",uid);
        map.put("email",email);
        map.put("imageUri",imageUri);
        map.put("phoneNo",phoneNo);
        map.put("chapLogoUri",chapLogoUri);
        return map;
    }
}
