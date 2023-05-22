package com.qwqer.adplatform.ad.self;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.glide.GlideHelper;
import com.qwqer.adplatform.utils.AdUtils;
import com.qwqer.adplatform.utils.TToast;
import com.qwqer.adplatform.view.BaseView;

/**
 * 自定义广告的内容显示。
 * @author E
 */
public class SelfBannerAdContentView extends BaseView {

    private ImageView selfBannerAdContentImageView;

    public SelfBannerAdContentView(Context context) {
        super(context);
        init();
    }

    public SelfBannerAdContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.self_banner_ad_content_view;
    }

    @Override
    protected void initViews() {
        selfBannerAdContentImageView = findViewById(R.id.selfBannerAdContentImageView);
    }

    public void setData(AdvertInfoBean advertInfoBean){
        GlideHelper.displayCenterCrop(selfBannerAdContentImageView , advertInfoBean.getAdvertPath());
        selfBannerAdContentImageView.setOnClickListener(view -> {
            //添加点击事件
            AdUtils.jump(context , advertInfoBean);
        });
    }


}
