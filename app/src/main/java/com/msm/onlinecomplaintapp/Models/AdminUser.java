package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class AdminUser {
    private String email;
    private String name;
    private String uid;

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

    public Map<String,Object> toMap(){
        Map<String,Object> admin=new HashMap<>();
        admin.put("uid",uid );
        admin.put("email",email );
        admin.put("name",name );
        return admin;
    }
}
