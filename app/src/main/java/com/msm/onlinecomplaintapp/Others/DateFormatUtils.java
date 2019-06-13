package com.msm.onlinecomplaintapp.Others;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.model.value.ServerTimestampValue;

import java.util.Formatter;
import java.util.Locale;

public class DateFormatUtils {


    public static String firebaseDBDate = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String firebaseDBDay = "yyyy-MM-dd";
    public static final long NOW_TIME_RANGE =DateUtils.MINUTE_IN_MILLIS * 5; // 5 minutes

    public static String dateTime = "yyyy-MM-dd HH:mm:ss";

    public static CharSequence getRelativeTimeSpanStringShort(Context context, long time) {
        long now = System.currentTimeMillis();
        long range = Math.abs(now - time);
        return formatDuration(context, range, time);
    }

    private static String formatDuration(Context context, long range, long time) {
        final Resources res = context.getResources();

        if(range<(DateUtils.MINUTE_IN_MILLIS)*2){
            return "Just Now";
        }
        if (range<DateUtils.HOUR_IN_MILLIS){
            String format= String.valueOf((int)(range/DateUtils.MINUTE_IN_MILLIS));
            if(format!="1"){
                format=format+" minutes ago";
            }
            else {
                format=format+"minute ago";
            }
            return format;
        }
        if(range<DateUtils.DAY_IN_MILLIS){
            String format=String.valueOf((int)(range/DateUtils.HOUR_IN_MILLIS));
            if(format!="1"){
                format=format+" hrs ago";
            }
            else {
                format=format+" hour ago";
            }
            return format;
        }
        if(range<DateUtils.WEEK_IN_MILLIS){
            String format=String.valueOf((int)(range/DateUtils.DAY_IN_MILLIS));
            if(format!="1"){
                format=format+" days ago";
            }
            else {
                format=format+" day ago";
            }
            return format;
        }
        if(range<(DateUtils.WEEK_IN_MILLIS)*4){
            String format=String.valueOf((int)(range/(DateUtils.WEEK_IN_MILLIS)));
            if(format!="1"){
                format=format+" weeks ago";
            }
            else {
                format=format+" week ago";
            }
            return format;
        }
        if(range<DateUtils.YEAR_IN_MILLIS){
            String format=String.valueOf((int)(range/(DateUtils.WEEK_IN_MILLIS*4)));
            if(format!="1"){
                format=format+" months ago";
            }
            else {
                format=format+" month ago";
            }
            return format;
        }
        else {
            String format=String.valueOf((int)(range/DateUtils.YEAR_IN_MILLIS));
            if(format!="1"){
                format=format+" years ago";
            }
            else {
                format=format+" year ago";
            }
            return format;
        }
    }

}
