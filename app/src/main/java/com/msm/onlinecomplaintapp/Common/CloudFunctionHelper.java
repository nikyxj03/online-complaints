package com.msm.onlinecomplaintapp.Common;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.msm.onlinecomplaintapp.Interfaces.FunctionListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudFunctionHelper {

    private Context context;
    private FirebaseFunctions firebaseFunctions;

    private static CloudFunctionHelper cloudFunctionHelper;

    public static CloudFunctionHelper getInstance(Context context) {
        if (cloudFunctionHelper == null) {
            cloudFunctionHelper = new CloudFunctionHelper(context);
        }
        return cloudFunctionHelper;
    }

    private CloudFunctionHelper(Context context) {
        this.context = context;
    }

    public void init() {
        firebaseFunctions = FirebaseFunctions.getInstance();
    }

    public void blockuser(final String userId, final FunctionListener functionListener){
        Map<String,Object> map=new HashMap<>();
        map.put("uid",userId);
        Log.i("abcdef","1");
        firebaseFunctions.getHttpsCallable("BlockUser").call(map).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(Task<HttpsCallableResult> task) {
                Log.i("abcdef","2");
                if(task.isSuccessful()){
                    functionListener.onExecuted(true);
                }
                else {
                    functionListener.onExecuted(false);
                }
            }
        });
    }

    public void getRandomCID(final OnDataSFetchListener<String> onDataFetchListener){
        firebaseFunctions.getHttpsCallable("GetRandomCID").call().addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                String cids=null;
                if(task.isSuccessful()){
                    cids=(String) task.getResult().getData();
                }
                onDataFetchListener.onDataSFetch(cids);
            }
        });
    }

    public void getRandomRID(final OnDataSFetchListener<String> onDataFetchListener){
        firebaseFunctions.getHttpsCallable("GetRandomRID").call().addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                String rids=null;
                if(task.isSuccessful()){
                    rids=(String) task.getResult().getData();
                }
                onDataFetchListener.onDataSFetch(rids);
            }
        });
    }

    public void getRandomMID(final OnDataSFetchListener<String> onDataFetchListener){
        firebaseFunctions.getHttpsCallable("GetRandomMID").call().addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                String mids=null;
                if(task.isSuccessful()){
                    mids=(String) task.getResult().getData();
                }
                onDataFetchListener.onDataSFetch(mids);
            }
        });
    }

    public void getRandomQID(final OnDataSFetchListener<String> onDataSFetchListener){
        firebaseFunctions.getHttpsCallable("GetRandomQID").call().addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                String qids=null;
                if(task.isSuccessful()){
                    qids=(String) task.getResult().getData();
                }
                onDataSFetchListener.onDataSFetch(qids);
            }
        });
    }
}
