package com.msm.onlinecomplaintapp.Common;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.msm.onlinecomplaintapp.Interfaces.FunctionListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;

import java.util.HashMap;
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

    public void fetchAllUsers(final OnDataFetchListener onDataFetchListener){

    }
}
