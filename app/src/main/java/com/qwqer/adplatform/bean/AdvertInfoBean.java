package com.qwqer.adplatform.bean;

/**
 * 广告数据。
 * @author E
 */
public class AdvertInfoBean {

    //广告图片地址
    private String advertPath = null;
    //广告排序（banner广告和弹窗广告必填）升序
    private int advertSort = 0;
    //展示时间（开屏广告必填 advertPosition = 1）
    private int showTime = 0;
    //跳转类型：1-站内，2-站外
    private int station = 0;
    //跳转地址
    private String stationUrl = null;

    public String getAdvertPath() {
        return advertPath;
    }

    public void setAdvertPath(String advertPath) {
        this.advertPath = advertPath;
    }

    public int getAdvertSort() {
        return advertSort;
    }

    public void setAdvertSort(int advertSort) {
        this.advertSort = advertSort;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public String getStationUrl() {
        return stationUrl;
    }

    public void setStationUrl(String stationUrl) {
        this.stationUrl = stationUrl;
    }
}
