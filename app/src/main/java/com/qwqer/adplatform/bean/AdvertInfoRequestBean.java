package com.qwqer.adplatform.bean;

/**
 * 广告接口请求接口。
 * @author E
 */
public class AdvertInfoRequestBean {

    //广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
    private int advertPosition = 0;
    //广告获取来源：1-每次打开app，2-每次打开点击首页，advertPosition = 10 必填
    private int showFrom = 0;


    public int getAdvertPosition() {
        return advertPosition;
    }

    public void setAdvertPosition(int advertPosition) {
        this.advertPosition = advertPosition;
    }

    public int getShowFrom() {
        return showFrom;
    }

    public void setShowFrom(int showFrom) {
        this.showFrom = showFrom;
    }
}
