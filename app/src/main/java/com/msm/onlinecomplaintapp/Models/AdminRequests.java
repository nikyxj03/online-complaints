package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AdminRequests {
  private String cid;
  private String rid;
  private String uid;
  private String message;
  private String type;
  private Timestamp timestamp;
  private String did;
  private String duid;

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

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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

  public void setDid(String did) {
    this.did = did;
  }

  public String getDid() {
    return did;
  }

  public String getDuid() {
    return duid;
  }

  public void setDuid(String duid) {
    this.duid = duid;
  }

  public Map<String,Object> toMap(){
    Map<String,Object> result=new HashMap<>();
    result.put("type",type );
    result.put("rid",rid );
    result.put("cid",cid );
    result.put("uid",uid );
    result.put("message",message );
    result.put("timestamp",timestamp);
    result.put("did",did);
    result.put("duid",duid);
    return result;
  }

}