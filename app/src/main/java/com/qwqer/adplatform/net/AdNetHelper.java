package com.qwqer.adplatform.net;

import com.qwqer.adplatform.bean.AdvertInfoRequestBean;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.net.base.OnRequestCallBackListener;
import com.qwqer.adplatform.net.base.RetrofitHelper;
import com.qwqer.adplatform.utils.QwQerAdConfig;
/**
 * 相关接口类。
 * @author E
 */
public class AdNetHelper extends RetrofitHelper {

    private static AdNetHelper adNetHelper;

    public static AdNetHelper getInstance(){
        if (null == adNetHelper){
            adNetHelper = new AdNetHelper();
        }
        return adNetHelper;
    }

    /**
     * 广告。
     * @param advertInfoRequestBean
     * @param onRequestCallBackListener
     */
    public void advertInfo(AdvertInfoRequestBean advertInfoRequestBean , OnRequestCallBackListener<AdvertInfoResultBean> onRequestCallBackListener){
        if (QwQerAdConfig.appType == 1){
            //用户端
            requestCallback(serverRequestInterface.advertInfoUser(advertInfoRequestBean), onRequestCallBackListener);
            return;
        }
        //配送端
        requestCallback(serverRequestInterface.advertInfoDriver(advertInfoRequestBean), onRequestCallBackListener);
    }

    /**
     * 广告。
     * @param advertInfoRequestBean
     * @param onRequestCallBackListener
     */
    public void advertInfoSplash(AdvertInfoRequestBean advertInfoRequestBean , OnRequestCallBackListener<AdvertInfoResultBean> onRequestCallBackListener){
        if (QwQerAdConfig.appType == 1){
            //用户端
            requestCallback(serverRequestInterface.advertInfoSplashUser(advertInfoRequestBean), onRequestCallBackListener);
            return;
        }
        //配送端
        requestCallback(serverRequestInterface.advertInfoSplashDriver(advertInfoRequestBean), onRequestCallBackListener);
    }

    /**
     * 用户端广告。
     * @param advertPosition
     * @param showFrom
     * @param onRequestCallBackListener
     */
    public void advertInfo(int advertPosition , int showFrom , OnRequestCallBackListener<AdvertInfoResultBean> onRequestCallBackListener){
        AdvertInfoRequestBean advertInfoRequestBean = new AdvertInfoRequestBean();
        advertInfoRequestBean.setAdvertPosition(advertPosition);
        advertInfoRequestBean.setShowFrom(showFrom);
        advertInfo(advertInfoRequestBean , onRequestCallBackListener);
    }

    /**
     * 用户端广告。
     * @param advertPosition
     * @param showFrom
     * @param onRequestCallBackListener
     */
    public void advertInfoSplash(int advertPosition , int showFrom , OnRequestCallBackListener<AdvertInfoResultBean> onRequestCallBackListener){
        AdvertInfoRequestBean advertInfoRequestBean = new AdvertInfoRequestBean();
        advertInfoRequestBean.setAdvertPosition(advertPosition);
        advertInfoRequestBean.setShowFrom(showFrom);
        advertInfoSplash(advertInfoRequestBean , onRequestCallBackListener);
    }





}
