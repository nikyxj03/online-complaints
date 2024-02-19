package com.msm.onlinecomplaintapp.Chat;

import com.msm.onlinecomplaintapp.Chat.Models.ChatInfo;
import com.msm.onlinecomplaintapp.Chat.Models.ChatMessage;

import java.util.List;

public class ChatConversationHandler {

    private static ChatConversationHandler chatConversationHandler;
    private static List<ChatMessage> chatMessageList;
    private static String uid;
    private static ChatInfo chatInfo;

    public static ChatConversationHandler newInstance(String uid, ChatInfo chatInfo){
        chatConversationHandler=new ChatConversationHandler();
        ChatConversationHandler.uid=uid;
        ChatConversationHandler.chatInfo=chatInfo;
        return chatConversationHandler;
    }

    public static void destroyInstance(){
        chatConversationHandler=null;
        chatMessageList=null;
        uid=null;
        chatInfo=null;
    }

    public void connect(){

    }

}
