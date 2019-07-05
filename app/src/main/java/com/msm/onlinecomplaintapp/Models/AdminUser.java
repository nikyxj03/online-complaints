package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class AdminUser {
    private String email;
    private String name;
    private String uid;
    private String type;
    private String dept;
    private String registrationToken;
    private String cat;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> admin=new HashMap<>();
        admin.put("uid",uid );
        admin.put("email",email );
        admin.put("name",name );
        admin.put("dept",dept);
        admin.put("type",type);
        admin.put("registrationToken",registrationToken);
        admin.put("cat",cat);
        return admin;
    }
}
