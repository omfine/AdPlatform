package com.qwqer.adplatform.net.base;
/**
 * 接口回调。
 * @param <T>
 * @author E
 */
public class OnRequestCallBackListener <T>{

    /**
     * 开始访问接口.
     */
    public void onStart(){}

    /**
     * 接口成功返回。
     * @param it
     */
    public void onSuccess(T it){}

    /**
     * 接口响应失败。
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    public void onFailed(int errorCode , String errorMsg){}

}
