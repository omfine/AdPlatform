package com.qwqer.adplatform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.qwqer.adplatform.utils.AdLog;

abstract public class BaseView extends RelativeLayout {

    protected Context context;

    public BaseView(Context context) {
        super(context);
        this.context = context;
//        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        init();
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        init();
    }

    abstract protected int getLayoutViewId();

    protected void init(){
        if (0 == getLayoutViewId()){
            return;
        }
        inflate(getContext() , getLayoutViewId() , this);

        initParams();
        initViews();
        initListeners();
    }

    protected void initParams(){}

    protected void initViews(){

    }

    protected void initListeners(){

    }

}
