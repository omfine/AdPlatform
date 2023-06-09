package com.qwqer.adplatform.ad;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.kc.openset.OSETListener;
import com.kc.openset.OSETSplash;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.ad.self.SelfSplashAdView;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.init.QwQerAdHelper;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.net.AdNetHelper;
import com.qwqer.adplatform.net.base.OnRequestCallBackListener;
import com.qwqer.adplatform.utils.ActivityUtils;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.utils.QwQerAdConfig;
import com.qwqer.adplatform.view.BaseView;
/**
 * 开屏广告。
 * @author E
 */
public class SplashAdView extends BaseView {

    private LinearLayout splashAdContainerView;
    private OnAdListener onAdListener;

    public SplashAdView(Context context) {
        super(context);
        init();
    }

    public SplashAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.splash_ad_view;
    }

    @Override
    protected void initViews() {
        splashAdContainerView = findViewById(R.id.splashAdContainerView);
    }

    /**
     * 加载广告
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param codeId 头条广告位的ID
     * @param onAdListener 跳转时用
     */
    public void loadAd(int advertPosition , int showFrom , String codeId , OnAdListener onAdListener){
        this.onAdListener = onAdListener;
        if (QwQerAdConfig.deBugMode){
            //如果是测试，直接用头条广告
            //头条广告
//            loadSplashAd(codeId);
            showAdSetSplashAd(codeId);
            return;
        }

        AdNetHelper.getInstance().advertInfoSplash(advertPosition, showFrom , new OnRequestCallBackListener<AdvertInfoResultBean>(){
            @Override
            public void onFailed(int errorCode, String errorMsg) {
                if (null != onAdListener){
                    onAdListener.onJump();
                }
            }
            @Override
            public void onSuccess(AdvertInfoResultBean it) {
                //是否展示，1-展示，2-不展示
                int isShow = it.getIsShow();
                //是不是自营广告，1-是，2-厂商广告
                int isSelfAdvert = it.getIsSelfAdvert();
                if (1 != isShow){
                    if (null != onAdListener){
                        onAdListener.onJump();
                    }
                    return;
                }

                if (ActivityUtils.isActivityNotAvailable(context)){
                    return;
                }

                if (1 == isSelfAdvert){
                    //自营广告
                    SelfSplashAdView selfSplashAdView = new SelfSplashAdView(context);
                    selfSplashAdView.setOnAdListener(onAdListener);
                    selfSplashAdView.setData(it);

                    splashAdContainerView.removeAllViews();
                    splashAdContainerView.addView(selfSplashAdView);
                    return;
                }
                //头条广告
                showAdSetSplashAd(codeId);
//                loadSplashAd(codeId);
            }
        });
    }

    /**
     * 显示AdSet的开屏广告.
     * 开屏广告位id：7D5239D8D88EBF9B6D317912EDAC6439
     */
    private void showAdSetSplashAd(String adId){
        splashAdContainerView.removeAllViews();
        OSETSplash.getInstance().show((Activity) context, splashAdContainerView, adId, new OSETListener() {
            @Override
            public void onClick() {
                AdLog.e("=========显示AdSet的开屏广告========onClick====");
            }
            @Override
            public void onClose() {
                AdLog.e("=========显示AdSet的开屏广告========onClose====");
                if (null != onAdListener){
                    onAdListener.onJump();
                }
            }
            @Override
            public void onShow() {
                AdLog.e("=========显示AdSet的开屏广告========onShow====");
            }
            @Override
            public void onError(String s, String s1) {
                AdLog.e("=========显示AdSet的开屏广告========onError====s: " + s + "   s1: " + s1);
                if (null != onAdListener){
                    onAdListener.onJump();
                }
            }
        });
    }

/*    private void loadSplashAd(String codeId){
        if (ActivityUtils.isActivityNotAvailable(context)){
            return;
        }
        TTAdNative mTTAdNative = QwQerAdHelper.getAdManager().createAdNative(context);
        if (null == mTTAdNative){
            if (null != onAdListener){
                onAdListener.onJump();
            }
            return;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                //view宽高等于图片的宽高
                .setExpressViewAcceptedSize(widthPixels, heightPixels) // 单位是dp
                .setImageAcceptedSize(widthPixels, heightPixels) // 单位是px
                .build();

        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int code, String msg) {
                Log.i("qwqer_ad" , "load banner failed , code: " + code + "  msg: " + msg);
                if (null != onAdListener){
                    onAdListener.onJump();
                }
            }

            @Override
            public void onTimeout() {
                Log.i("qwqer_ad" , "slash ad load onTimeout");
                if (null != onAdListener){
                    onAdListener.onJump();
                }
            }
            @Override
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.i("qwqer_ad" , "slash ad load success");
                if (ActivityUtils.isActivityNotAvailable(context)){
                    return;
                }
                if (null == ad){
                    if (null != onAdListener){
                        onAdListener.onJump();
                    }
                    return;
                }
                View splashView  = ad.getSplashView();
                if (null == splashView){
                    if (null != onAdListener){
                        onAdListener.onJump();
                    }
                    return;
                }
                splashAdContainerView.removeAllViews();
                splashAdContainerView.addView(splashView);

                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {
                        Log.i("qwqer_ad" , "onAdClicked");
                    }
                    @Override
                    public void onAdShow(View view, int i) {
                        Log.i("qwqer_ad" , "onAdShow");
                    }
                    @Override
                    public void onAdSkip() {
                        Log.i("qwqer_ad" , "onAdSkip");
                        splashAdContainerView.removeAllViews();
                        if (null != onAdListener){
                            onAdListener.onJump();
                        }
                    }
                    @Override
                    public void onAdTimeOver() {
                        Log.i("qwqer_ad" , "onAdTimeOver");
                        splashAdContainerView.removeAllViews();
                        if (null != onAdListener){
                            onAdListener.onJump();
                        }
                    }
                });
            }
        } , 3000);

    }*/













}
