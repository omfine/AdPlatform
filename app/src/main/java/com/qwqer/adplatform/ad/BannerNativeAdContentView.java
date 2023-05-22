package com.qwqer.adplatform.ad;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bytedance.sdk.openadsdk.DislikeInfo;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.PersonalizationPrompt;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.dialog.DislikeDialog;
import com.qwqer.adplatform.glide.GlideHelper;
import com.qwqer.adplatform.utils.TToast;
import com.qwqer.adplatform.view.BaseView;
import java.util.ArrayList;
import java.util.List;
/**
 * 原生广告显示, 只是给了广告数据，需要自定义展示广告的页面。
 * @author E
 */
public class BannerNativeAdContentView extends BaseView {

    private OnAdViewInvokeListener onAdViewInvokeListener = null;

    public BannerNativeAdContentView(Context context) {
        super(context);
        init();
    }

    public BannerNativeAdContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.banner_native_ad_content_view;
    }

    public void setOnAdViewInvokeListener(OnAdViewInvokeListener onAdViewInvokeListener) {
        this.onAdViewInvokeListener = onAdViewInvokeListener;
    }

    public void setTTNativeAd(TTNativeAd ad){
        TextView title = findViewById(R.id.tv_native_ad_title);
        TextView descTextView = findViewById(R.id.tv_native_ad_desc);

        title.setText(ad.getTitle());
        descTextView.setText(ad.getDescription());

        ImageView imgDislike = findViewById(R.id.img_native_dislike);
        bindDislikeAction(ad , imgDislike ,false);

        List<View> imageViewList = new ArrayList<>();

        List<TTImage> list = ad.getImageList();
        if (null != list && !list.isEmpty()){
            TTImage ttImage = list.get(0);
            ImageView nativeImage = findViewById(R.id.iv_native_image);
            GlideHelper.display(nativeImage , ttImage.getImageUrl());
            //事件需要
            imageViewList.add(nativeImage);
        }

        TTImage icon = ad.getIcon();
        if (null != icon && icon.isValid()){
            ImageView nativeIconImageView = findViewById(R.id.iv_native_icon);
            GlideHelper.display(nativeIconImageView , icon.getImageUrl());
        }

        ImageView logoImageView = findViewById(R.id.iv_native_ad_logo);
        logoImageView.setImageBitmap(ad.getAdLogo());

        Button mCreativeButton = findViewById(R.id.btn_native_creative);
        //可根据广告类型，为交互区域设置不同提示信息
        int interactionType = ad.getInteractionType();
        if (interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            //下载类型的

        }else if (interactionType == TTAdConstant.INTERACTION_TYPE_DIAL){
            //拨打电话类型的
            mCreativeButton.setVisibility(View.VISIBLE);
            mCreativeButton.setText("立即拨打");

        }else if (interactionType == TTAdConstant.INTERACTION_TYPE_LANDING_PAGE
                || interactionType == TTAdConstant.INTERACTION_TYPE_BROWSER){
            //查看详情类型
            mCreativeButton.setVisibility(View.VISIBLE);
            mCreativeButton.setText("查看详情");

        }else {
            mCreativeButton.setVisibility(View.GONE);
            TToast.show(context, "交互类型异常");
        }

        //点击事件
        RelativeLayout bannerAdContentParentView = findViewById(R.id.bannerAdContentParentView);
        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(bannerAdContentParentView);
        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(mCreativeButton);

        ad.registerViewForInteraction((ViewGroup) bannerAdContentParentView, imageViewList,clickViewList, creativeViewList, imgDislike, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    TToast.show(context, "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    TToast.show(context, "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    TToast.show(context, "广告" + ad.getTitle() + "展示");
                }
            }
        });

    }

    /**
     * 接入dislike 逻辑，有助于提示广告精准投放度
     * @param ad
     * @param dislikeView
     * @param isCustomDislike 为true时 使用自定义dislike方法
     */
    private void bindDislikeAction(TTNativeAd ad, View dislikeView, boolean isCustomDislike){
        if (isCustomDislike){
            // 使用自定义Dislike
            final DislikeInfo dislikeInfo = ad.getDislikeInfo();
            if (dislikeInfo == null || dislikeInfo.getFilterWords() == null || dislikeInfo.getFilterWords().isEmpty()) {
                return;
            }


            final DislikeDialog dislikeDialog = new DislikeDialog(context, dislikeInfo);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
//                    mBannerContainer.removeAllViews();
                    if (null != onAdViewInvokeListener){
                        onAdViewInvokeListener.onDislike();
                    }
                }
            });
            dislikeDialog.setOnPersonalizationPromptClick(new DislikeDialog.OnPersonalizationPromptClick() {
                @Override
                public void onClick(PersonalizationPrompt personalizationPrompt) {
                    TToast.show(context, "点击了为什么看到此广告");
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            dislikeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislikeDialog.show();
                }
            });

            return;
        }
        // 使用默认Dislike

        final TTAdDislike ttAdDislike = ad.getDislikeDialog((Activity) context);
        if (ttAdDislike != null) {
            ttAdDislike.setDislikeInteractionCallback(new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onShow() {

                }
                @Override
                public void onSelected(int position, String value, boolean enforce) {
//                    mBannerContainer.removeAllViews();
                    if (enforce) {
                        TToast.show(context, "NativeBannerActivity 原生banner sdk强制移除View ");
                    }
                    if (null != onAdViewInvokeListener){
                        onAdViewInvokeListener.onDislike();
                    }
                }
                @Override
                public void onCancel() {

                }
            });
        }
        dislikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttAdDislike != null)
                    ttAdDislike.showDislikeDialog();
            }
        });
    }


}
