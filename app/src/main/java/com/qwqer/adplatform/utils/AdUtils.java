package com.qwqer.adplatform.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.view.AdWebViewActivity;

/**
 *
 */
public class AdUtils {

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / (scale <= 0 ? 1 : scale) + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    /**
     * 点击广告之后的跳转。
     * @param advertInfoBean
     */
    public static void jump(Context context, AdvertInfoBean advertInfoBean){
        if (null == advertInfoBean){
            return;
        }
        //跳转类型：1-站内，2-站外
        int station = advertInfoBean.getStation();
        //跳转地址
        String stationUrl = advertInfoBean.getStationUrl();
        //是否合法
        boolean hasValidStationUrl = hasValidStationUrl(advertInfoBean);
        if (!hasValidStationUrl){
            return;
        }

        if (2 == station){
            //2-站外
            try{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(stationUrl));
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        //1-站内
        Intent intent = new Intent(context , AdWebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url" , stationUrl);
        context.startActivity(intent);

    }

    /**
     * 跳转链接是否合法。
     * @param advertInfoBean
     * @return boolean
     */
    public static  boolean hasValidStationUrl(AdvertInfoBean advertInfoBean){
        if (null == advertInfoBean){
            return false;
        }
        //跳转地址
        String stationUrl = advertInfoBean.getStationUrl();
        if (TextUtils.isEmpty(stationUrl)){
            return false;
        }
        if (!stationUrl.startsWith("https")
                && !stationUrl.startsWith("http")){
            return false;
        }
        return true;
    }

    /**
     * 获取APK版本号
     */
    public static String getVersionName(Context ctx) {
        // 获取packagemanager的实例
        String version = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
        }
        return version;
    }

}
