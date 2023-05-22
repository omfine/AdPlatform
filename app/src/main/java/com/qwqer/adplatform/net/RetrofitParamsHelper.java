package com.qwqer.adplatform.net;
/**
 * 设置网络接口参数。
 * @author E
 */
public class RetrofitParamsHelper {

    private static RetrofitParamsHelper adNetHelper;

    public static RetrofitParamsHelper getInstance(){
        if (null == adNetHelper){
            adNetHelper = new RetrofitParamsHelper();
        }
        return adNetHelper;
    }

    /**
     * 刷新token
     * @param token
     */
    public void freshToken(String token) {
        AdNetHelper.getInstance().freshToken(token);
    }

    /**
     * 刷新baseUrl,放在Retrofit里的
     * @param baseUrl
     */
    public void freshBaseUrl(String baseUrl) {
        AdNetHelper.getInstance().setBaseUrl(baseUrl);
    }

}
