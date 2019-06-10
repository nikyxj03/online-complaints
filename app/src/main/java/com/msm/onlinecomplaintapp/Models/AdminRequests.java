package com.msm.onlinecomplaintapp.Models;

import java.util.HashMap;
import java.util.Map;

public class AdminRequests {
    private String cid;
    private String rid;
    private String uid;
    private String status;
    private String type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result=new HashMap<>();
        result.put("type",type );
        result.put("rid",rid );
        result.put("cid",cid );
        result.put("uid",uid );
        result.put("status",status );
        return result;
    }

}
