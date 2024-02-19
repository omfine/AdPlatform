package com.qwqer.adplatform.ad;

import android.app.Activity;
import com.beizi.fusion.InterstitialAd;
import com.beizi.fusion.InterstitialAdListener;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.cache.TempCacheHelper;
import com.qwqer.adplatform.dialog.SelfInsertScreenAdDialog;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.net.AdNetHelper;
import com.qwqer.adplatform.net.base.OnRequestCallBackListener;
import com.qwqer.adplatform.utils.ActivityUtils;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.utils.QwQerAdConfig;
import java.util.List;
/**
 * 广告辅助类。
 * @author E
 */
public class AdHelper {

    //插屏弹出窗。
    private static SelfInsertScreenAdDialog selfInsertScreenAdDialog;


    /**
     * 插屏广告。
     * @param activity
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param codeId 头条广告位的ID
     */
    public static void showInsertScreenAd(Activity activity , int advertPosition , int showFrom , String codeId){
        if (ActivityUtils.isActivityNotAvailable(activity)){
            return;
        }
        showInsertScreenAd(activity , advertPosition , showFrom , codeId , null);
    }

    /**
     * 插屏广告。
     * @param activity
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param codeId 头条广告位的ID
     * @param onAdListener 广告弹出窗口，显示和关闭的回调
     */
    public static void showInsertScreenAd(Activity activity , int advertPosition , int showFrom , String codeId , OnAdListener onAdListener){
        if (ActivityUtils.isActivityNotAvailable(activity)){
            return;
        }
        String key = "insertAd_" + advertPosition + "_" + showFrom + "_" + codeId;
        AdLog.d("===============插屏广告:: " + codeId);
        if (QwQerAdConfig.deBugMode){
            //测试头条广告
            //厂商广告
            showAdSetInsertScreenAd(activity , codeId , onAdListener);
            return;
        }
        AdvertInfoResultBean cacheData =TempCacheHelper.getInstance().getAdValue(key);


        AdNetHelper.getInstance().advertInfo(advertPosition , showFrom , new OnRequestCallBackListener<AdvertInfoResultBean>(){
            @Override
            public void onSuccess(AdvertInfoResultBean it) {
                //获取到数据
                onGetDataSuccess(activity , it , codeId , onAdListener);
            }
        });
    }

    /**
     * 获取到数据。
     * @param activity
     * @param it
     * @param codeId
     * @param onAdListener
     */
    private static void onGetDataSuccess(Activity activity, AdvertInfoResultBean it, String codeId, OnAdListener onAdListener){
        //是否展示，1-展示，2-不展示
        int isShow = it.getIsShow();
        //是不是自营广告，1-是，2-厂商广告
        int isSelfAdvert = it.getIsSelfAdvert();
        if (1 != isShow){
            return;
        }

        if (1 == isSelfAdvert){
            List<AdvertInfoBean> adverts = it.getAdverts();
            if (adverts.isEmpty()){
                return;
            }
            //自己的插屏广告
            if (null != selfInsertScreenAdDialog && selfInsertScreenAdDialog.isShowing()){
                selfInsertScreenAdDialog.cancel();
                selfInsertScreenAdDialog.dismiss();
                selfInsertScreenAdDialog = null;
            }
            if (ActivityUtils.isActivityNotAvailable(activity)){
                return;
            }
            selfInsertScreenAdDialog = new SelfInsertScreenAdDialog(activity , activity);
            selfInsertScreenAdDialog.show();
            selfInsertScreenAdDialog.setData(adverts);
            selfInsertScreenAdDialog.setOnDismissListener(dialogInterface -> {
                if (null != onAdListener){
                    onAdListener.onAdDialogClose();
                }
            });
            if (null != onAdListener){
                onAdListener.onAdDialogShow();
            }
            return;
        }
        //厂商广告
        showAdSetInsertScreenAd(activity , codeId , onAdListener);
    }



    private static InterstitialAd interstitialAd = null;

    /**
     * 显示AdSet集合广告的插屏广告。
     * 注意： 1、startLoad和destroy方法需要在同一个Activity调用，尽量在长生命周期的Activiy调用，因为频繁的destroy会影响广告的ecpm
     * 插屏广告位id：1D273967F51868AF2C4E080D496D06D0
     */
    private static void showAdSetInsertScreenAd(Activity activity , String adId , OnAdListener onAdListener){
        //在首页中OnCreate调用以下代码可以开始加载广告并缓存
        interstitialAd = new InterstitialAd(activity, adId, new InterstitialAdListener() {
            @Override
            public void onAdFailed(int code) {
                AdLog.e("qwqer_ad======插屏广告========onAdFailed=======code: " + code);
            }
            @Override
            public void onAdLoaded() {
                AdLog.e("qwqer_ad======插屏广告========onAdLoaded=======");
                //广告展示需要传入Activity类型
                interstitialAd.showAd(activity);
            }
            @Override
            public void onAdShown() {
                AdLog.e("qwqer_ad======插屏广告========onAdShown=======");
            }
            @Override
            public void onAdClosed() {
                AdLog.e("qwqer_ad======插屏广告========onAdClosed=======");
                if (null != onAdListener){
                    onAdListener.onAdDialogClose();
                }
                if (null != interstitialAd){
                    interstitialAd.destroy();
                }
            }
            @Override
            public void onAdClick() {
                AdLog.e("qwqer_ad======插屏广告========onClick=======");
            }
        }, 5000, 0);

        //广告版本：1表示平台模板；2表示平台模板2.0
        interstitialAd.setAdVersion(1);
        //加载广告
        interstitialAd.loadAd();
    }


/*    private static boolean isTouTiaoInsertScreenAdShowing = false;

    private static void showTouTiaoInsertScreenAd(Activity activity , String codeId, int expressViewWidth, int expressViewHeight , OnAdListener onAdListener){
        if (isTouTiaoInsertScreenAdShowing){
            //如果正在显示，则不再加载
            return;
        }
        if (ActivityUtils.isActivityNotAvailable(activity)){
            return;
        }
        TTAdNative mTTAdNative = QwQerAdHelper.getAdManager().createAdNative(activity);
        if (null == mTTAdNative){
            return;
        }
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                //此次加载广告的用途是实时加载，当用来作为缓存时，请使用：TTAdLoadType.LOAD
                .setAdLoadType(TTAdLoadType.LOAD)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                .build();
        //加载广告，新插屏
        mTTAdNative.loadFullScreenVideoAd(adSlot , new TTAdNative.FullScreenVideoAdListener(){
            @Override
            public void onError(int code, String message) {
                AdLog.d("======InsertScreenAd==onError===code:: " + code + "   message:" + message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                //加载成功
                AdLog.d("======InsertScreenAd==onFullScreenVideoAdLoad===");
                isTouTiaoInsertScreenAdShowing = true;
                ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        isTouTiaoInsertScreenAdShowing = true;
                    }
                    @Override
                    public void onAdVideoBarClick() {

                    }
                    @Override
                    public void onAdClose() {
                        isTouTiaoInsertScreenAdShowing = false;
                    }
                    @Override
                    public void onVideoComplete() {

                    }
                    @Override
                    public void onSkippedVideo() {

                    }
                });
//                ttFullScreenVideoAd.showFullScreenVideoAd(activity , TTAdConstant.RitScenes.GAME_GIFT_BONUS , null);
            }

            @Override
            public void onFullScreenVideoCached() {

            }

            @Override
            public void onFullScreenVideoCached(TTFullScreenVideoAd ttFullScreenVideoAd) {
                AdLog.d("======InsertScreenAd==onFullScreenVideoCached=2==");
                if (ActivityUtils.isActivityNotAvailable(activity)){
                    return;
                }
                ttFullScreenVideoAd.showFullScreenVideoAd(activity , TTAdConstant.RitScenes.GAME_GIFT_BONUS , null);

            }
        });



        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                TToast.show(activity, "load error : " + code + ", " + message);
            }
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                bindAdListener(activity , mTTAd , onAdListener);
                mTTAd.render();
            }
        });
    }*/
/*

    private static void bindAdListener(Activity activity ,final TTNativeExpressAd ad , OnAdListener onAdListener){
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
                TToast.show(activity, "广告关闭");
                if (null != onAdListener){
                    onAdListener.onAdDialogClose();
                }
            }

            @Override
            public void onAdClicked(View view, int type) {
                TToast.show(activity, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                TToast.show(activity, "广告展示");
                if (null != onAdListener){
                    onAdListener.onAdDialogShow();
                }
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                TToast.show(activity, msg + " code:" + code);
            }
            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                TToast.show(activity, "渲染成功");
                ad.showInteractionExpressAd(activity);
            }
        });
    }
*/

}
