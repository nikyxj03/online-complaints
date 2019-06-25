package com.msm.onlinecomplaintapp.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.common.util.concurrent.AsyncFunction;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UriToBitmapExecutor{

    private List<String> uriList;
    private List<Bitmap> bitmapList;
    private OnDataFetchListener<Bitmap> onDataFetchListener;

    public UriToBitmapExecutor(List<String> uriList, OnDataFetchListener<Bitmap> onDataFetchListener) {
        this.uriList=uriList;
        this.onDataFetchListener=onDataFetchListener;
    }

    public void execute() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            for (int i=0;i<uriList.size();i++){
                executor.submit(new Task(uriList.get(i)));
            }
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            onDataFetchListener.onDataFetched(bitmapList);
            executor.shutdownNow();
        }
    }


    public class Task implements Runnable {

        private String uri;
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;

        Task(String uri){
            this.uri=uri;
        }

        public void run() {
            try {
                URLConnection conn = new URL(uri).openConnection();
                conn.connect();
                is = conn.getInputStream();
                bis = new BufferedInputStream(is, 8192);
                bm = BitmapFactory.decodeStream(bis);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bitmapList.add(bm);
        }
    }

}
