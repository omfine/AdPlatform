package com.qwqer.adplatform.init;

import static android.app.Application.getProcessName;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.kc.openset.OSETSDK;
import com.kc.openset.listener.OSETInitListener;
import com.qwqer.adplatform.utils.AdLog;
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
/*
        if (TTAdSdk.isInitSuccess()){
            return;
        }
        TTAdSdk.init(context, buildConfig(context , appId), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                AdLog.i("init success: " + TTAdSdk.isInitSuccess());
            }

            @Override
            public void fail(int code, String msg) {
                AdLog.i("init fail:  code = " + code + " msg = " + msg);
            }
        });
*/

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
//        String APPKEY = "E6097975B89E83D6";
//        OSETSDK.getInstance().setUserId("aaaa");
        OSETSDK.getInstance().init(context, appKey, new OSETInitListener() {
            @Override
            public void onError(String s) {
                AdLog.e("=========initAdSet====onError======:: " + s);
            }
            @Override
            public void onSuccess() {
                AdLog.e("=========initAdSet====onSuccess======成功 ");
            }
        });

    }


    private static TTAdConfig buildConfig(Context context , String appId) {
        return new TTAdConfig.Builder()
                .appId(appId)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用

                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI,
                        TTAdConstant.NETWORK_STATE_2G,
                        TTAdConstant.NETWORK_STATE_3G,
                        TTAdConstant.NETWORK_STATE_4G,
                        TTAdConstant.NETWORK_STATE_5G,
                        TTAdConstant.NETWORK_STATE_MOBILE) //允许直接下载的网络状态集合
                .supportMultiProcess(false)//是否支持多进程
                .needClearTaskReset()
                .build();
    }

}
