package com.qwqer.adplatform.utils;

import android.util.Log;

public class AdLog {

    private static final String logKey = "qwqer_ad";

    public static void e(String msg){
        Log.e(logKey , msg);
    }

    public static void d(String msg){
        Log.d(logKey , msg);
    }

    public static void i(String msg){
        Log.i(logKey , msg);
    }

}
