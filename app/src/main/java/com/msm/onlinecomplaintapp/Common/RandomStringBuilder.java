package com.msm.onlinecomplaintapp.Common;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnLoad;

import java.util.ArrayList;
import java.util.List;

public class RandomStringBuilder {

    private String type="";
    private int count;
    private List<String> id=new ArrayList<>();
    private int flag=0;
    private String tempid;
    private Context mcontext;

    public RandomStringBuilder(String type,int count){
        this.type=type;
        this.count=count;
        flag=0;
    }

    public RandomStringBuilder(Context context,String type){
        this.type=type;
        this.mcontext=context;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public String randomAlphaNumeric() {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    public void getRandomId(final OnDataSFetchListener<String> onDataSFetchListener){
        if(type=="cid"){
            GlobalApplication.databaseHelper.getAllCIDs(new OnDataFetchListener<String>() {
                @Override
                public void onDataFetched(List<String> strings) {
                    id=strings;
                    flag=0;
                    do {
                        flag=0;
                        tempid=randomAlphaNumeric();
                        if(id!=null)
                        for (int i1 = 0; i1 < id.size(); i1++) {
                            if (id.get(i1).equals(tempid)) {
                                flag = 1;
                            }
                        }
                    }while(flag==1);
                    onDataSFetchListener.onDataSFetch(tempid);
                }
            });
        }
        else if(type=="rid"){
            GlobalApplication.databaseHelper.getAllRIDs(new OnDataFetchListener<String>() {
                @Override
                public void onDataFetched(List<String> strings) {
                    id=strings;
                    flag=0;
                    do {
                        flag=0;
                        tempid=randomAlphaNumeric();
                        if (id!=null)
                        for (int i1 = 0; i1 < id.size(); i1++) {
                            if (id.get(i1).equals(tempid)) {
                                flag = 1;
                            }
                        }
                    }while(flag==1);
                    onDataSFetchListener.onDataSFetch(tempid);
                }
            });
        }
        else if(type=="msgId"){
            GlobalApplication.databaseHelper.getAllMsgIDs(new OnDataFetchListener<String>() {
                @Override
                public void onDataFetched(List<String> strings) {
                    id=strings;
                    flag=0;
                    do {
                        flag=0;
                        tempid=randomAlphaNumeric();
                        if (id!=null)
                            for (int i1 = 0; i1 < id.size(); i1++) {
                                if (id.get(i1).equals(tempid)) {
                                    flag = 1;
                                }
                            }
                    }while(flag==1);
                    onDataSFetchListener.onDataSFetch(tempid);
                }
            });
        }
        else if(type=="qid"){
            CloudFunctionHelper.getInstance(mcontext).getRandomQID(new OnDataSFetchListener<String>() {
                @Override
                public void onDataSFetch(String s) {
                    onDataSFetchListener.onDataSFetch(s);
                }
            });
        }


    }
}
