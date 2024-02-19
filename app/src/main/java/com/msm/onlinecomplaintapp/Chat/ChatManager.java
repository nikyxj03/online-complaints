package com.msm.onlinecomplaintapp.Chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.Chat.Models.ChatContact;
import com.msm.onlinecomplaintapp.Chat.Models.ChatInfo;
import com.msm.onlinecomplaintapp.Chat.Models.GroupInfo;
import com.msm.onlinecomplaintapp.Chat.Models.MyGroup;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatManager {

    private static ChatManager chatManager;
    private static FirebaseUser user;
    private static List<ChatContact> chatContacts=new ArrayList<>();

    public synchronized static ChatManager getInstance(){
     if(chatManager==null) {
         chatManager = new ChatManager();
         user= FirebaseAuth.getInstance().getCurrentUser();
     }
     return chatManager;
    }

    public void getAllMyContacts(OnDataFetchListener<ChatContact> onDataFetchListener){
        GlobalApplication.databaseHelper.getMyContacts(user.getUid(),onDataFetchListener);
    }


    public void getAllMyGroups(OnDataFetchListener<MyGroup> myGroupOnDataFetchListener){
        GlobalApplication.databaseHelper.getMyGroups(user.getUid(),myGroupOnDataFetchListener);
    }

    public void getGroupInfo(String gid, OnDataSFetchListener<GroupInfo> onDataSFetchListener){
        GlobalApplication.databaseHelper.getGroupInfo(gid,onDataSFetchListener);
    }

    public void getChatInfo(String chatId, OnDataSFetchListener<ChatInfo> chatInfoOnDataSFetchListener){
        GlobalApplication.databaseHelper.getChatInfo(chatId,chatInfoOnDataSFetchListener);
    }




}
