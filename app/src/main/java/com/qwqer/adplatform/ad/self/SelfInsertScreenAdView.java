package com.qwqer.adplatform.ad.self;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.base.BasePagerAdapter;
import com.qwqer.adplatform.base.BaseViewPager;
import com.qwqer.adplatform.base.OnBaseViewPagerListener;
import com.qwqer.adplatform.base.OnIPageChangeListener;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.view.BaseView;
import com.qwqer.adplatform.view.DotView;
import java.util.ArrayList;
import java.util.List;
/**
 * 自已的插屏广告。
 * @author E
 */
public class SelfInsertScreenAdView  extends BaseView {

    private BaseViewPager adViewPager;
    private DotView dotView;
    private ImageView adDialogCloseBtn;

    private OnAdListener onAdListener;

    public SelfInsertScreenAdView(Context context) {
        super(context);
        init();
    }

    public SelfInsertScreenAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.self_insert_screen_ad_view;
    }

    @Override
    protected void initViews() {
        adViewPager = findViewById(R.id.adViewPager);
        dotView = findViewById(R.id.adDotView);
        adDialogCloseBtn = findViewById(R.id.adDialogCloseBtn);
    }

    @Override
    protected void initListeners() {
        adDialogCloseBtn.setOnClickListener(view -> {
            if (null != onAdListener){
                onAdListener.onAdDialogClose();
            }
        });

        adViewPager.setOnBaseViewPagerListener(new OnBaseViewPagerListener(){
            @Override
            public void onActionDown() {
                AdLog.e("===========onActionDown====回调 ");
                stopLoop();
            }
            @Override
            public void onActionUp() {
                AdLog.e("===========onActionUp====回调 pageSize: " + pageSize);
                if (pageSize > 1){
                    startLoop();
                }
            }
        });

    }

    public void setOnAdListener(OnAdListener onAdListener) {
        this.onAdListener = onAdListener;
    }

    public void setData(List<AdvertInfoBean> adverts){
        if (adverts.isEmpty()){
            return;
        }
        freshAdvertData(adverts);
    }

    private void freshAdvertData(List<AdvertInfoBean> adverts){
        int size = adverts.size();
        if (0 == size){
            return;
        }
        if (2 == size || 3 == size){
            adverts.addAll(adverts);
        }

        pageSize = adverts.size();
        List<SelfInsertScreenAdContentView> views = new ArrayList<>();
        for (int i = 0; i < pageSize; i ++ ){
            AdvertInfoBean advertInfoBean = adverts.get(i);
            SelfInsertScreenAdContentView selfInsertScreenAdContentView = new SelfInsertScreenAdContentView(context);
            selfInsertScreenAdContentView.setData(advertInfoBean);
            views.add(selfInsertScreenAdContentView);
        }

        dotView.setMaxCount(size);

        BasePagerAdapter adapter = new BasePagerAdapter(views);
        adViewPager.setAdapter(adapter);
        adViewPager.setCurrentItem(pageSize > 1 ? 1000 : pageSize);
        adViewPager.addOnPageChangeListener(new OnIPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                dotView.onItemSelected(position);
            }
        });

        if (pageSize > 1){
            startLoop();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            int msgWhat = msg.what;
            if (msgWhat == msgWhatCode){
                int currentItem = adViewPager.getCurrentItem() + 1;
                adViewPager.setCurrentItem(currentItem);

                startLoop();
            }
        }
    };

    private int pageSize = 0;
    private final int msgWhatCode = 123123;

    private void startLoop(){
        mHandler.removeMessages(msgWhatCode);
        mHandler.sendEmptyMessageDelayed(msgWhatCode , 5*1000);
    }

    private void stopLoop(){
        mHandler.removeMessages(msgWhatCode);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            if (pageSize > 1){
                startLoop();
            }
        }else {
            stopLoop();
        }
    }

}
