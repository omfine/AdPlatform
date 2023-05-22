package com.qwqer.adplatform.ad.self;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.glide.GlideHelper;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.utils.AdUtils;
import com.qwqer.adplatform.utils.ScheduleRun;
import com.qwqer.adplatform.view.BaseView;
import java.util.List;
/**
 * 自己的开屏广告。
 * @author E
 */
public class SelfSplashAdView extends BaseView {

    private ImageView selfSplashAdImageView;
    private TextView adTimeCounterTextView;
    private LinearLayout jumpBtnView;
    private LinearLayout jumpCounterBtnView;

    private ScheduleRun scheduleRun = new ScheduleRun(1);

    private OnAdListener onAdListener;

    public SelfSplashAdView(Context context) {
        super(context);
        init();
    }

    public SelfSplashAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.self_splash_ad_view;
    }

    @Override
    protected void initViews() {
        selfSplashAdImageView = findViewById(R.id.selfSplashAdImageView);
        adTimeCounterTextView = findViewById(R.id.adTimeCounterTextView);
        jumpBtnView = findViewById(R.id.jumpBtnView);
        jumpCounterBtnView = findViewById(R.id.jumpCounterBtnView);
    }

    @Override
    protected void initListeners() {
        jumpCounterBtnView.setOnClickListener(view -> {
            if (null != onAdListener){
                onAdListener.onJump();
                return;
            }
        });
        jumpBtnView.setOnClickListener(view -> {
            //添加点击事件
            AdUtils.jump(context , advertInfoBean);
        });
    }

    private AdvertInfoBean advertInfoBean = null;

    public void setData(AdvertInfoResultBean advertInfoResultBean){
        List<AdvertInfoBean> adverts = advertInfoResultBean.getAdverts();
        if (adverts.isEmpty()){
            adTimeCounterTextView.setText("跳过");
            jumpCounterBtnView.setVisibility(View.VISIBLE);
            return;
        }
        advertInfoBean = adverts.get(0);
        GlideHelper.displayCenterCrop(selfSplashAdImageView , advertInfoBean.getAdvertPath());
        //跳转链接是否合法
        boolean hasValidStationUrl = AdUtils.hasValidStationUrl(advertInfoBean);
        jumpBtnView.setVisibility(hasValidStationUrl ? View.VISIBLE : View.GONE);

        //用后台返回计时数
        startTimer(advertInfoBean.getShowTime());
    }

    private void startTimer(int maxCount){
        adTimeCounterTextView.setText(maxCount + "s跳过");
        jumpCounterBtnView.setVisibility(View.VISIBLE);

        scheduleRun.setScheduleRunListener((count, leftCount) -> {
            mHandler.post(()->{
                adTimeCounterTextView.setText(leftCount + "s跳过");

                if (0 == leftCount){
                    if (null != onAdListener){
                        onAdListener.onJump();
                    }
                }
            });
        });
        scheduleRun.setMaxCount(maxCount);
        scheduleRun.start();
    }

    private void onTimerPause(){
        scheduleRun.pause();
    }

    private void onTimerResume(){
        scheduleRun.resume();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void setOnAdListener(OnAdListener onAdListener) {
        this.onAdListener = onAdListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            onTimerResume();
        }else {
            onTimerPause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        scheduleRun.stop();
        super.onDetachedFromWindow();
    }

}
