package com.qwqer.adplatform.net.base;

import com.qwqer.adplatform.bean.AdvertInfoRequestBean;
import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.net.Urls;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerRequestInterface {
    /**
     * 用户端
     * @param advertInfoRequestBean
     * @return
     */
    @POST(Urls.advertInfo_user)
    Call<BaseRetrofitBean<AdvertInfoResultBean>> advertInfoUser(@Body AdvertInfoRequestBean advertInfoRequestBean);
    /**
     * 用户端Splash
     * @param advertInfoRequestBean
     * @return
     */
    @POST(Urls.advertInfo_user)
    Call<BaseRetrofitBean<AdvertInfoResultBean>> advertInfoSplashUser(@Body AdvertInfoRequestBean advertInfoRequestBean);

    /**
     * 配送端
     * @param advertInfoRequestBean
     * @return
     */
    @POST(Urls.advertInfo_driver)
    Call<BaseRetrofitBean<AdvertInfoResultBean>> advertInfoDriver(@Body AdvertInfoRequestBean advertInfoRequestBean);
    /**
     * 配送端Splash
     * @param advertInfoRequestBean
     * @return
     */
    @POST(Urls.advertInfo_driver)
    Call<BaseRetrofitBean<AdvertInfoResultBean>> advertInfoSplashDriver(@Body AdvertInfoRequestBean advertInfoRequestBean);
}
