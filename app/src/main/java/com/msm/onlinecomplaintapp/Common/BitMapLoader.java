package com.msm.onlinecomplaintapp.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.msm.onlinecomplaintapp.Interfaces.OnLoad;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class BitMapLoader extends AsyncTask<Integer,Integer,Bitmap> {

  public String uri;
  public OnLoad onLoad;

  public BitMapLoader(String url, final OnLoad onLoad) {
    super();
    this.onLoad=onLoad;
    this.uri=url;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    onLoad.OnLoaded(bitmap);
  }

  @Override
  protected Bitmap doInBackground(Integer... params) {
    Bitmap bm = null;
    InputStream is = null;
    BufferedInputStream bis = null;
    try {
      URLConnection conn = new URL(uri).openConnection();
      conn.connect();
      is = conn.getInputStream();
      bis = new BufferedInputStream(is, 8192);
      bm = BitmapFactory.decodeStream(bis);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
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

    return bm;
  }

}