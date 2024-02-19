package com.msm.onlinecomplaintapp.Models;

import com.msm.onlinecomplaintapp.Chat.Models.GroupInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterClub {
    private String chapName;
    private String chapLogoUri;
    private String chapid;
    private List<ChapterMember> commiteeMembers;
    private List<ChapterMember> boardMember;
    private List<GroupInfo> chatGroupInfos;
    private Map<String,Object> head;

    public void setChapLogoUri(String chapLogoUri) {
        this.chapLogoUri = chapLogoUri;
    }

    public String getChapLogoUri() {
        return chapLogoUri;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public void setChapid(String chapid) {
        this.chapid = chapid;
    }

    public String getChapName() {
        return chapName;
    }

    public String getChapid() {
        return chapid;
    }

    public List<ChapterMember> getBoardMember() {
        return boardMember;
    }

    public List<ChapterMember> getCommiteeMembers() {
        return commiteeMembers;
    }

    public List<GroupInfo> getChatGroupInfos() {
        return chatGroupInfos;
    }

    public void setBoardMember(List<ChapterMember> boardMember) {
        this.boardMember = boardMember;
    }

    public void setChatGroupInfos(List<GroupInfo> chatGroupInfos) {
        this.chatGroupInfos = chatGroupInfos;
    }

    public void setCommiteeMembers(List<ChapterMember> commiteeMembers) {
        this.commiteeMembers = commiteeMembers;
    }

    public Map<String, Object> getHead() {
        return head;
    }

    public void setHead(Map<String,Object> head) {
        this.head = head;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("chapid",chapid);
        map.put("chapLogoUri",chapLogoUri);
        map.put("chapName",chapName);
        map.put("head",head);
        return map;
    }

    public List<Map<String,Object>> commiteeMembersListMap(){
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i=0;i<commiteeMembers.size();i++){
            list.add(commiteeMembers.get(i).toMap());
        }
        return list;
    }

    public List<Map<String,Object>> boardMembersListMap(){
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i=0;i<boardMember.size();i++){
            list.add(boardMember.get(i).toMap());
        }
        return list;
    }

    public List<Map<String,Object>> GroupsListMap(){
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i = 0; i< chatGroupInfos.size(); i++){
            list.add(chatGroupInfos.get(i).toMap());
        }
        return list;
    }
}
