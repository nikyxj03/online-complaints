package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class DeptUsers {
    private String cat;
    private String dept;
    private String email;
    private String fullname;
    private String phoneno;
    private String uid;
    private String registrationToken;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getFullname() {
        return fullname;
    }

    public String getCat() {
        return cat;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDept() {
        return dept;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> deptuser=new HashMap<>();
        deptuser.put("dept",dept );
        deptuser.put("cat",cat );
        deptuser.put("fullname",fullname );
        deptuser.put("phoneno",phoneno );
        deptuser.put("email",email );
        deptuser.put("uid",uid );
        deptuser.put("registrationToken",registrationToken);
        return deptuser;
    }
}
