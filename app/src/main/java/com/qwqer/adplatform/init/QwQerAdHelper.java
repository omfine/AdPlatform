package com.qwqer.adplatform.init;

import static android.app.Application.getProcessName;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;
import com.beizi.fusion.BeiZis;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.qwqer.adplatform.utils.QwQerAdConfig;
/**
 * 广告初始化。
 * @author E
 */
public class QwQerAdHelper {

    public static TTAdManager getAdManager() {
        return TTAdSdk.getAdManager();
    }

    public static Context getContext(){
        return mContext;
    }

    public static String getClientFlag(){
        if (null == mClientFlag){
            return "";
        }
        return mClientFlag;
    }

    private static Context mContext;

    private static String mClientFlag;

    /**
     * 接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
     * @param context
     * @param appId
     * @param appType 1，用户端 2，配送端
     */
    public static void init(Application context , String appId , int appType , String clientFlag) {
        mContext = context;
        mClientFlag = clientFlag;
        QwQerAdConfig.appType = appType;

        initAdSet(context , appId);
    }

    /**
     * appkey：E6097975B89E83D6
     * @param context
     */
    private static void initAdSet(Application context , String appKey){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 安卓9.0后不允许多进程使用同一个数据目录
            try {
                WebView.setDataDirectorySuffix(getProcessName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BeiZis.init(context , appKey);

    }



}
