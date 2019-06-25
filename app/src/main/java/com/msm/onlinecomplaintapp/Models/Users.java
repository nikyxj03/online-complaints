package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String uid;
    private String email;
    private String cat;
    private String uenable;
    private String fullname;
    private String phoneno;
    private String registrationToken;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCat() {
        return cat;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getUenable() {
        return uenable;
    }

    public String getUid() {
        return uid;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public void setUenable(String uenable) {
        this.uenable = uenable;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result=new HashMap<>();
        result.put("uenable",uenable );
        result.put("phoneno",phoneno );
        result.put("fullname",fullname );
        result.put("email",email );
        result.put("cat",cat );
        result.put("uid",uid );
        result.put("registrationToken",registrationToken);
        return result;
    }


}
