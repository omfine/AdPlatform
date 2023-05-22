package com.qwqer.adplatform.ad.self;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.qwqer.adplatform.R;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.glide.GlideHelper;
import com.qwqer.adplatform.utils.AdUtils;
import com.qwqer.adplatform.view.BaseView;
/**
 * 自定义插屏广告的内容显示。
 * @author E
 */
public class SelfInsertScreenAdContentView extends BaseView {

    private ImageView selfBannerAdContentImageView;

    public SelfInsertScreenAdContentView(Context context) {
        super(context);
        init();
    }

    public SelfInsertScreenAdContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.self_insert_screen_ad_content_view;
    }

    @Override
    protected void initViews() {
        selfBannerAdContentImageView = findViewById(R.id.selfBannerAdContentImageView);
    }

    public void setData(AdvertInfoBean advertInfoBean){
        String advertPath = advertInfoBean.getAdvertPath();
        GlideHelper.display(selfBannerAdContentImageView , advertPath , 8);

        //添加点击事件
        selfBannerAdContentImageView.setOnClickListener(view -> {
            AdUtils.jump(context ,advertInfoBean);
        });

    }

}
