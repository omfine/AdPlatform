package com.qwqer.adplatform.bean;

/**
 * 广告类型.
 * @author E
 */
public enum AdType {

    Banner(1),
    InsertScreen(2),
    Splash(3);

    private int type = 0;

    private AdType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
