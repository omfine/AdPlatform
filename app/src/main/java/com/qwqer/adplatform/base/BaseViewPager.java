package com.qwqer.adplatform.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class BaseViewPager extends ViewPager {

    private OnBaseViewPagerListener onBaseViewPagerListener = null;

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnBaseViewPagerListener(OnBaseViewPagerListener onBaseViewPagerListener){
        this.onBaseViewPagerListener = onBaseViewPagerListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP){
            if (null != onBaseViewPagerListener){
                onBaseViewPagerListener.onActionUp();
            }
        }
//        AdLog.e("===========Ad====onTouchEvent====1=========action: " + action );
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            if (null != onBaseViewPagerListener){
                onBaseViewPagerListener.onActionDown();
            }
        }
//        AdLog.e("===========Ad====onInterceptTouchEvent====1=========action: " + action );
        return super.onInterceptTouchEvent(ev);
    }

}
