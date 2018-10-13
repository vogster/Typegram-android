package com.unlogicon.typegram.utils;

import android.content.Context;
import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String convertTimeStamp(String timestamp){
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            Long time = simpleDateFormat.parse(timestamp).getTime();

            if (time < 1000000000000L) {
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " seconds ago";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "1 day ago";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String timeAgo(String timestamp){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            Long millis = simpleDateFormat.parse(timestamp).getTime();

            long diff = new Date().getTime() - millis;


            double seconds = Math.abs(diff) / 1000;
            double minutes = seconds / 60;
            double hours = minutes / 60;
            double days = hours / 24;
            double years = days / 365;

            String words;

            if (seconds < 45) {
                words = String.valueOf(Math.round(seconds)) + " seconds ago";
            } else if (seconds < 90) {
                words = "1 minute ago";
            } else if (minutes < 45) {
                words =  String.valueOf(Math.round(minutes)) + " minutes ago";
            } else if (minutes < 90) {
                words = "1 hour ago";
            } else if (hours < 24) {
                words = String.valueOf(Math.round(hours)) + " hours ago";
            } else if (hours < 42) {
                words = "1 day ago";
            } else if (days < 30) {
                words = String.valueOf(Math.round(days)) + " days ago";
            } else if (days < 45) {
                words = "1 month ago";
            } else if (days < 365) {
                words =  String.valueOf(Math.round(days / 30)) + " months ago";
            } else if (years < 1.5) {
                words = "1 year ago";
            } else {
                words = String.valueOf(Math.round(years)) + " years ago";
            }

            return words;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
