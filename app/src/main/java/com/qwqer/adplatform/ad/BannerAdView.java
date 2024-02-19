package com.qwqer.adplatform.ad;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.beizi.fusion.BannerAd;
import com.beizi.fusion.BannerAdListener;
import com.beizi.fusion.NativeAd;
import com.beizi.fusion.NativeAdListener;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.ad.self.SelfBannerAdView;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.cache.TempCacheHelper;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.net.AdNetHelper;
import com.qwqer.adplatform.net.base.OnRequestCallBackListener;
import com.qwqer.adplatform.utils.ActivityUtils;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.utils.AdUtils;
import com.qwqer.adplatform.utils.QwQerAdConfig;
import com.qwqer.adplatform.utils.Utils;
import com.qwqer.adplatform.view.BaseView;
/**
 * Banner广告。
 * @author E
 */
public class BannerAdView extends BaseView {

    private LinearLayout bannerAdContainerView;

    private int measuredWidth = 0;
    private int measuredHeight = 0;

    public BannerAdView(Context context) {
        super(context);
        init();
    }

    public BannerAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId(){
        return R.layout.banner_ad_view;
    }

    @Override
    protected void initParams() {
    }

    @Override
    protected void initViews() {
        bannerAdContainerView = findViewById(R.id.bannerAdContainerView);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
//        QwQerAdHelper.getAdManager().requestPermissionIfNecessary(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        this.measuredWidth = width;
        this.measuredHeight = height;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;

        AdLog.d("====================onLayout: width: " + width + "   height: " + height + "   widthPixels: " + widthPixels + "  " + AdUtils.px2dip(context , height));
    }

    /**
     * 加载广告
     * @param advertPosition  广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom  广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param imageCorner  显示图片的角度
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     */
    public void loadAd(int advertPosition , int showFrom , int imageCorner , String codeId, boolean canClose , boolean nativeAd){
        int mHeight = getMeasuredHeight();
        if (0 == mHeight){
            mHandler.postDelayed(() -> {
                loadAd(advertPosition , showFrom , imageCorner , codeId , canClose , nativeAd);
            }, 120);
            return;
        }
        loadAds(advertPosition , showFrom , imageCorner , codeId ,canClose , nativeAd);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());


    /**
     * 加载广告
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param imageCorner 显示图片的角度
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     */
    private void loadAds(int advertPosition , int showFrom , int imageCorner , String codeId, boolean canClose , boolean nativeAd){
        String key = "banner_" + advertPosition + "_" + showFrom + "_" + codeId;
        if (QwQerAdConfig.deBugMode){
            //测试用头条广告
            showAd(codeId , nativeAd);
            return;
        }
        //判断是否有未过期的缓存数据，有的话，用缓存数据
        AdvertInfoResultBean cacheData = TempCacheHelper.getInstance().getAdValue(key);
        if (null != cacheData){
            //使用缓存数据
            onGetDataSuccess(cacheData , codeId , canClose , nativeAd);
            return;
        }
        //通过接口去获取新数据
        AdNetHelper.getInstance().advertInfo(advertPosition , showFrom , new OnRequestCallBackListener<AdvertInfoResultBean>(){
            @Override
            public void onFailed(int errorCode, String errorMsg) {
                setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(AdvertInfoResultBean it) {
                //获取数据成功
                onGetDataSuccess(it , codeId , canClose , nativeAd);
                //缓存数据
                TempCacheHelper.getInstance().save(key , it);
            }
        });
    }

    /**
     * 获取数据成功
     * @param it
     * @param codeId
     * @param canClose
     * @param nativeAd
     */
    private void onGetDataSuccess(AdvertInfoResultBean it , String codeId, boolean canClose , boolean nativeAd){
        //是否展示，1-展示，2-不展示
        int isShow = it.getIsShow();
        //是不是自营广告，1-是，2-厂商广告
        int isSelfAdvert = it.getIsSelfAdvert();
        if (1 != isShow){
            setVisibility(View.GONE);
            return;
        }

        if (ActivityUtils.isActivityNotAvailable(context)){
            return;
        }
        if (1 == isSelfAdvert){
            //自营广告
            SelfBannerAdView selfBannerAdView = new SelfBannerAdView(context);
            selfBannerAdView.setData(it , canClose);
            selfBannerAdView.setOnAdListener(new OnAdListener(){
                @Override
                public void onBannerClose() {
                    setVisibility(View.GONE);
                }
            });

            setVisibility(View.VISIBLE);
            bannerAdContainerView.removeAllViews();
            bannerAdContainerView.addView(selfBannerAdView);
            return;
        }
        //显示第三方广告平台的广告
        showAd(codeId , nativeAd);
    }

    /**
     * 第三方广告。
     * @param adId
     * @param nativeAd
     */
    private void showAd(String adId , boolean nativeAd){
        if (nativeAd){
            loadNativeAd(adId);
        }else {
            showAdSetBanner(adId);
        }
    }

    private BannerAd bannerAd;

    /**
     * 接入的Adset集合广告，展示banner.
     * banner广告位id：107EB50EDFE65EA3306C8318FD57D0B3
     */
    private void showAdSetBanner(String adId){
        setVisibility(View.VISIBLE);
        bannerAdContainerView.removeAllViews();
        bannerAdContainerView.setBackgroundColor(ContextCompat.getColor(context , R.color.ad_colorTransparent));
        if (null != bannerAd){
            bannerAd.destroy();
            bannerAd = null;
        }
        bannerAd = new BannerAd(context, adId, new BannerAdListener() {
            @Override
            public void onAdFailed(int i) {
                setVisibility(View.GONE);
                bannerAdContainerView.removeAllViews();
                AdLog.e("qwqer_ad============BannerAd=====onAdFailed===========code: " + i);
            }
            @Override
            public void onAdLoaded() {
                AdLog.e("qwqer_ad============BannerAd=====onAdLoaded===========");
            }
            @Override
            public void onAdShown() {
                setVisibility(View.VISIBLE);
                bannerAdContainerView.setBackgroundColor(ContextCompat.getColor(context , R.color.white));
                AdLog.e("qwqer_ad============BannerAd=====onAdShown===========");
            }
            @Override
            public void onAdClosed() {
                setVisibility(View.GONE);
                bannerAdContainerView.removeAllViews();
                AdLog.e("qwqer_ad============BannerAd=====onAdClosed===========");
            }
            @Override
            public void onAdClick() {
                AdLog.e("qwqer_ad============BannerAd=====onAdClick===========");
            }
        }, 5000L);

        float screenWidth = Utils.getScreenWidthDp(context);
        bannerAd.loadAd(screenWidth , (screenWidth / 6.4f) , bannerAdContainerView);
    }


    private NativeAd mNativeAd;
    private int mAdWidth = 0;
    private int mAdHeight = -2;

    /**
     * 原生广告.
     * @param codeId
     */
    private void loadNativeAd(String codeId){
        mNativeAd = new NativeAd(context, codeId, new NativeAdListener() {
            @Override
            public void onAdFailed(int i) {
                setVisibility(View.GONE);
                AdLog.e("qwqer_ad============NativeAd=====onAdFailed===========codeId: " + codeId +  "    code: " + i);
            }

            @Override
            public void onAdLoaded(View view) {
                setVisibility(View.VISIBLE);
                bannerAdContainerView.removeAllViews();
                bannerAdContainerView.addView(view);
                AdLog.e("qwqer_ad============NativeAd=====onAdLoaded===========");
                bannerAdContainerView.setBackgroundColor(ContextCompat.getColor(context , R.color.white));
            }

            @Override
            public void onAdShown() {
                AdLog.e("qwqer_ad============NativeAd=====onAdShown===========");
            }

            @Override
            public void onAdClosed() {
                bannerAdContainerView.removeAllViews();
                setVisibility(View.GONE);
                AdLog.e("qwqer_ad============NativeAd=====onAdClosed===========");
            }

            @Override
            public void onAdClosed(View view) {
                bannerAdContainerView.removeAllViews();
                setVisibility(View.GONE);
                AdLog.e("qwqer_ad============NativeAd=====onAdClosed===========");

            }
            @Override
            public void onAdClick() {
                AdLog.e("qwqer_ad============NativeAd=====onAdClick===========");
            }
        }, 5000 , 1);


//        mAdWidth = (int) Utils.getScreenWidthDp(context);
        mAdWidth = Utils.px2dip(context , this.measuredWidth);
//        mAdHeight = Utils.dip2px(context , 60);
//        mAdHeight = this.measuredHeight;
        mAdHeight = AdUtils.px2dip(context , this.measuredHeight);
        AdLog.e("qwqer_ad============NativeAd=====开始加载===========mAdHeight: " + mAdHeight   + "    mAdWidth:: " + mAdWidth);
        /**
         * 广告加载
         * 广告view的宽度，单位为dp, 特别说明：假如广告有左右间距，故广告view的宽度 = 屏幕宽度 - 左右间距总和
         * 广告view的高度，单位为dp，特别说明：0表示高度自适应
         */
        mNativeAd.loadAd(mAdWidth, mAdHeight);
    }

}
