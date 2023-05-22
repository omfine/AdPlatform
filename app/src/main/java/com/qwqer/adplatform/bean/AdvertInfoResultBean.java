package com.qwqer.adplatform.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 广告结果。
 * @author E
 */
public class AdvertInfoResultBean {

    //广告位置，1-开屏广告，2-首页广告（banner），3-我的钱包，4-提现，5-提现成功，6-余额明细，7-保证金列表，8-保证金明细，9-配送统计,10-首页广告（弹窗）
    private int advertPosition = 0;
    //是否展示，1-展示，2-不展示
    private int isShow = 0;
    //是不是自营广告，1-是，2-厂商广告
    private int isSelfAdvert = 0;

    private List<AdvertInfoBean> adverts = new ArrayList<>();

    public List<AdvertInfoBean> getAdverts() {
        if (null == adverts){
            return new ArrayList<>();
        }
        return adverts;
    }

    public void setAdverts(List<AdvertInfoBean> adverts) {
        this.adverts = adverts;
    }

    public int getAdvertPosition() {
        return advertPosition;
    }

    public void setAdvertPosition(int advertPosition) {
        this.advertPosition = advertPosition;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsSelfAdvert() {
        return isSelfAdvert;
    }

    public void setIsSelfAdvert(int isSelfAdvert) {
        this.isSelfAdvert = isSelfAdvert;
    }
}
