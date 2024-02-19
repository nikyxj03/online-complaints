package com.msm.onlinecomplaintapp.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class StatusLog {

  private String from;
  private String to;
  private Timestamp timestamp;
  private String duid;

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public void setDuid(String duid) {
    this.duid = duid;
  }

  public String getDuid() {
    return duid;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public String getFrom() {
    return from;
  }

  public Map<String,Object> toMap(){
    Map<String,Object> map=new HashMap<>();
    map.put("duid",duid);
    map.put("from",from);
    map.put("to",to);
    map.put("timestamp",timestamp);
    return map;
  }
}