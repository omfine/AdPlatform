package com.qwqer.adplatform.net;

public interface Urls {

    String deleteAddress = "customer/address-book/delete/{id}"; //删除地址
    //v2.0.3-用户|商户获取广告信息
    String advertInfo_user = "/rests/advertInfo/detail-customer";
    /**
     * v2.0.3-用户|商户获取开屏广告信息
     * 经邓优化，统一用上面的接口
     */
//    String advertInfo_user_splash = "/express-inland-app/no-auth/advert/detail-customer";


    //v2.0.3-配送员获取广告信息
    String advertInfo_driver = "/rests/advertInfo/detail-worker";
    /**
     * v2.0.3-配送员获取开屏广告信息
     * 经邓优化，统一用上面的接口
     */
//    String advertInfo_driver_splash = "/express-inland-app/no-auth/advert/detail-worker";


}
