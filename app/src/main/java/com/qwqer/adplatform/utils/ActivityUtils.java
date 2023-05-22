package com.qwqer.adplatform.utils;

import android.app.Activity;
import android.content.Context;

public class ActivityUtils {

    public static boolean isActivityNotAvailable(Context context){
        if (null == context){
            return true;
        }
        try{
            return isActivityNotAvailable((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isActivityNotAvailable(Activity activity){
        if (null == activity){
            return true;
        }
        try{
            return activity.isFinishing() || activity.isDestroyed();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }




}
