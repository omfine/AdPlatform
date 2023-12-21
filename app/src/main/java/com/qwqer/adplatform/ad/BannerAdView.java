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
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.ad.self.SelfBannerAdView;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
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

//    private TTAdNative mTTAdNative;

    private LinearLayout bannerAdContainerView;

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
//        try {
//            mTTAdNative = QwQerAdHelper.getAdManager().createAdNative(context);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
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
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;

        AdLog.d("====================onLayout: width: " + width + "   height: " + height + "   widthPixels: " + widthPixels + "  " + AdUtils.px2dip(context , height));
    }

    private int imageCorner = 0;

    /**
     * 加载广告
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     * @param expressViewWidth 加载广告的宽度。为头条上所选择广告的宽度
     * @param expressViewHeight 加载广告的高度。为头条上所选择广告的高度
     */
    private void loadAd(int advertPosition , int showFrom , int imageCorner , String codeId , boolean canClose, int expressViewWidth, int expressViewHeight){
        loadAds(advertPosition , showFrom , imageCorner , codeId ,canClose , expressViewWidth/2 , expressViewHeight/2);
    }

    /**
     * 加载广告
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom  广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     */
    public void loadAd(int advertPosition , int showFrom , String codeId , boolean canClose){
        loadAd(advertPosition , showFrom , 0 , codeId , canClose);
    }

    /**
     * 加载广告
     * @param advertPosition  广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom  广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param imageCorner  显示图片的角度
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     */
    public void loadAd(int advertPosition , int showFrom , int imageCorner , String codeId, boolean canClose){
        int mHeight = getMeasuredHeight();
        if (0 == mHeight){
            mHandler.postDelayed(() -> {
                loadAd(advertPosition , showFrom , imageCorner , codeId , canClose);
            }, 120);
            return;
        }

        int mWidth = getMeasuredWidth();
        mWidth = mWidth == 0 ? getResources().getDisplayMetrics().widthPixels : mWidth;
        mHeight = mHeight == 0 ? 240 : mHeight;

        AdLog.d("===============loadBanner===mHeight:: " + AdUtils.px2dip(context , mHeight));

        loadAds(advertPosition , showFrom , imageCorner , codeId ,canClose , mWidth , AdUtils.px2dip(context , mHeight));
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());


    /**
     * 加载广告
     * @param advertPosition 广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
     * @param showFrom 广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
     * @param imageCorner 显示图片的角度
     * @param codeId 头条广告位的ID
     * @param canClose 自营广告是否可以关闭
     * @param expressViewWidth 加载广告的宽度。为头条上所选择广告的宽度
     * @param expressViewHeight 加载广告的高度。为头条上所选择广告的高度
     */
    private void loadAds(int advertPosition , int showFrom , int imageCorner , String codeId, boolean canClose, int expressViewWidth, int expressViewHeight ){
        this.imageCorner = imageCorner;
        if (QwQerAdConfig.deBugMode){
            //测试用头条广告
//            loadExpressAd(codeId , expressViewWidth , expressViewHeight);
            showAdSetBanner(codeId);
            return;
        }

        AdNetHelper.getInstance().advertInfo(advertPosition , showFrom , new OnRequestCallBackListener<AdvertInfoResultBean>(){
            @Override
            public void onFailed(int errorCode, String errorMsg) {
                setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(AdvertInfoResultBean it) {
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
                showAdSetBanner(codeId);
//                loadExpressAd(codeId , expressViewWidth , expressViewHeight);
            }
        });
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
            }
            @Override
            public void onAdLoaded() {

            }
            @Override
            public void onAdShown() {
                setVisibility(View.VISIBLE);
                bannerAdContainerView.setBackgroundColor(ContextCompat.getColor(context , R.color.white));
            }
            @Override
            public void onAdClosed() {
                setVisibility(View.GONE);
                bannerAdContainerView.removeAllViews();
            }
            @Override
            public void onAdClick() {

            }
        }, 5000L);

        float screenWidth = Utils.getScreenWidthDp(context);
        bannerAd.loadAd(screenWidth , (screenWidth / 6.4f) , bannerAdContainerView);
    }


/*
    *//**
     * 加载广告并自定义显示广告的View。
     * @param codeId
     *//*
    private void loadNativeAd(String codeId){
        final AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
//                .setImageAcceptedSize(600, 257)
                .setImageAcceptedSize(context.getResources().getDisplayMetrics().widthPixels, 120)
                //[start支持模板样式]:需要支持模板广告和原生广告样式的切换，需要调用supportRenderControl和setExpressViewAcceptedSize
//                .supportRenderControl() //支持模板样式
//                .setExpressViewAcceptedSize(350,300)//设置模板宽高（dp）
                //[end支持模板样式]
                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .setAdCount(3)
                .build();
        if (null == mTTAdNative) {
            mTTAdNative = QwQerAdHelper.getAdManager().createAdNative(context);
        }

        //请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
            @Override
            public void onError(int code, String msg) {
                setVisibility(View.GONE);
                Log.i("qwqer_ad" , "load banner failed , code: " + code + "  msg: " + msg);
            }
            @Override
            public void onNativeAdLoad(List<TTNativeAd> list) {
                if (list.isEmpty()){
                    return;
                }
                final TTNativeAd ad = list.get(0);
                //【注意】
                //如果打开了支持模板样式开关 supportRenderControl()：
                //则需要给广告对象设置ExpressRenderListener监听，
                //然后调用广告对象的render()方法开始渲染，在渲染成功的回调中再调用showAd(ad)
                //
                //如果没有打开支持模板样式开关 ：
                //这里向前兼容，则和以前版本sdk的使用保持一致，
                //不用设置监听以及调用render()
                //可以直接调用showAd(ad)

                ad.setExpressRenderListener((view, width, height, isExpress) ->
                        showAd(ad)
                );
                ad.render();
            }
        });
    }*/

/*
    //广告展示和数据绑定
    private void showAd(TTNativeAd ad) {
        Log.i("qwqer_ad" , "load banner ad success, " + ad.getDescription() + "  " + ad.getSource() + "   " + ad.getTitle()
                + "    " + ad.getImageList().size() );

        List<TTImage> list = ad.getImageList();
        if (list.isEmpty()){
            return;
        }
        TTImage ttImage = list.get(0);

        Log.i("qwqer_ad" , "load banner ad success, " + ttImage.getWidth() + "  " + ttImage.getHeight() + "   " + ttImage.isValid());

        BannerNativeAdAContentView bannerNativeAdContentView = new BannerNativeAdAContentView(context);
        bannerNativeAdContentView.setTTNativeAd(ad , imageCorner);
        bannerNativeAdContentView.setOnAdViewInvokeListener(new OnAdViewInvokeListener(){
            @Override
            public void onDislike() {
                bannerAdContainerView.removeAllViews();
                setVisibility(View.GONE);
            }
        });
        setVisibility(View.VISIBLE);
        bannerAdContainerView.removeAllViews();
        bannerAdContainerView.addView(bannerNativeAdContentView);
    }
*/


    /**
     * 加载可以自定义尺寸的广告,使用是给定的模板。
     * @param codeId
     * @param expressViewWidth
     * @param expressViewHeight
     */
//    private void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight){
//        if (ActivityUtils.isActivityNotAvailable(context)){
//            return;
//        }
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(codeId) //广告位id
//                .setAdCount(1) //请求广告数量为1到3条
//                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
//                .build();
//
//        AdLog.d("=========loadBanner===expressViewWidth:: " + expressViewWidth + "   expressViewHeight: " + expressViewHeight + "    codeId: " + codeId);
//
//        if (null == mTTAdNative) {
//            mTTAdNative = QwQerAdHelper.getAdManager().createAdNative(context);
//        }
//        //请求广告，对请求回调的广告作渲染处理
//        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
//            @Override
//            public void onError(int code, String message) {
//                bannerAdContainerView.removeAllViews();
//                setVisibility(View.GONE);
//                TToast.show(context, "load error : " + code + ", " + message);
//            }
//            @Override
//            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
//                if (ads == null || ads.size() == 0) {
//                    setVisibility(View.GONE);
//                    return;
//                }
//                TTNativeExpressAd mTTAd = ads.get(0);
//                if (null == mTTAd){
//                    setVisibility(View.GONE);
//                    return;
//                }
//                if (ActivityUtils.isActivityNotAvailable(context)){
//                    return;
//                }
//
//                mTTAd.setSlideIntervalTime(5 * 1000);
//                bindAdListener(mTTAd);
//                mTTAd.render();
//            }
//        });

//    }

//    private boolean mHasShowDownloadActive = false;

//    private void bindAdListener(TTNativeExpressAd ad){
//        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
//            @Override
//            public void onAdClicked(View view, int type) {
//                TToast.show(context, "广告被点击");
//            }
//            @Override
//            public void onAdShow(View view, int type) {
//                TToast.show(context, "广告展示");
//            }
//            @Override
//            public void onRenderFail(View view, String msg, int code) {
//                TToast.show(context,  "onRenderFail: " + msg + " code:" + code);
//                bannerAdContainerView.removeAllViews();
//                setVisibility(View.GONE);
//            }
//            @Override
//            public void onRenderSuccess(View view, float width, float height) {
//                TToast.show(context, "渲染成功,width: " + width + " , height: " + height);
//                if (null == view){
//                    setVisibility(View.GONE);
//                    return;
//                }
//                if (ActivityUtils.isActivityNotAvailable(context)){
//                    return;
//                }
//                setVisibility(View.VISIBLE);
//                bannerAdContainerView.removeAllViews();
//                bannerAdContainerView.addView(view);
//            }
//        });
//        //dislike设置
//        bindDislike(ad, false);
//
//        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//            return;
//        }
//        ad.setDownloadListener(new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//                TToast.show(context, "点击开始下载");
//            }
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!mHasShowDownloadActive) {
//                    mHasShowDownloadActive = true;
//                    TToast.show(context, "下载中，点击暂停");
//                }
//            }
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(context, "下载暂停，点击继续");
//            }
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(context, "下载失败，点击重新下载");
//            }
//            @Override
//            public void onInstalled(String fileName, String appName) {
//                TToast.show(context, "安装完成，点击图片打开");
//            }
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(context, "点击安装");
//            }
//        });
//
//    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
//    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
//        if (customStyle) {
//            //使用自定义样式
//            final DislikeInfo dislikeInfo = ad.getDislikeInfo();
//            if (dislikeInfo == null || dislikeInfo.getFilterWords() == null || dislikeInfo.getFilterWords().isEmpty()) {
//                return;
//            }
//            if (ActivityUtils.isActivityNotAvailable(context)){
//                return;
//            }
//            final DislikeDialog dislikeDialog = new DislikeDialog(context, dislikeInfo);
//            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
//                @Override
//                public void onItemClick(FilterWord filterWord) {
//                    //屏蔽广告
//                    TToast.show(context, "点击 " + filterWord.getName());
//                    //用户选择不喜欢原因后，移除广告展示
//                    bannerAdContainerView.removeAllViews();
//                    setVisibility(View.GONE);
//                }
//            });
//            dislikeDialog.setOnPersonalizationPromptClick(personalizationPrompt ->
//                    TToast.show(context, "点击了为什么看到此广告")
//            );
//            ad.setDislikeDialog(dislikeDialog);
//            return;
//        }
//        //使用默认模板中默认dislike弹出样式
//        ad.setDislikeCallback((Activity) context, new TTAdDislike.DislikeInteractionCallback() {
//            @Override
//            public void onShow() {
//            }
//            @Override
//            public void onSelected(int position, String value, boolean enforce) {
//                TToast.show(context, "点击 " + value);
//                bannerAdContainerView.removeAllViews();
//                setVisibility(View.GONE);
//                //用户选择不喜欢原因后，移除广告展示
//                if (enforce) {
//                    TToast.show(context, "模版Banner 穿山甲sdk强制将view关闭了");
//                }
//            }
//            @Override
//            public void onCancel() {
//                TToast.show(context, "点击取消 ");
//            }
//        });
//    }

}
