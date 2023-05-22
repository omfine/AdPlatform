package com.qwqer.adplatform.dialog;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qwqer.adplatform.R;
import com.qwqer.adplatform.ad.self.SelfInsertScreenAdView;
import com.qwqer.adplatform.bean.AdvertInfoBean;
import com.qwqer.adplatform.listeners.OnAdListener;
import com.qwqer.adplatform.utils.ActivityUtils;

import java.util.List;
/**
 * 自己的插屏广告。
 * @author E
 */
public class SelfInsertScreenAdDialog extends Dialog {

    private SelfInsertScreenAdView selfInsertScreenAdView;

    private Activity activity;

    public SelfInsertScreenAdDialog(Context context , Activity activity){
        super(context , R.style.ad_cyDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_insert_screen_ad_dialog);
        setCanceledOnTouchOutside(false);
        initViews();
        if (null != activity){
            if (ActivityUtils.isActivityNotAvailable(activity)){
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    }
                    @Override
                    public void onActivityStarted(@NonNull Activity activity) {

                    }
                    @Override
                    public void onActivityResumed(@NonNull Activity activity) {

                    }
                    @Override
                    public void onActivityPaused(@NonNull Activity activity) {

                    }
                    @Override
                    public void onActivityStopped(@NonNull Activity activity) {
                        dismiss();
                    }
                    @Override
                    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                    }
                    @Override
                    public void onActivityDestroyed(@NonNull Activity activity) {
                        dismiss();
                    }
                });
            }
        }
    }

    private void initViews(){
        selfInsertScreenAdView = findViewById(R.id.selfInsertScreenAdView);
        selfInsertScreenAdView.setOnAdListener(new OnAdListener(){
            @Override
            public void onAdDialogClose() {
                dismiss();
            }
        });
    }

    public void setData(List<AdvertInfoBean> adverts){
        selfInsertScreenAdView.setData(adverts);
    }


}
