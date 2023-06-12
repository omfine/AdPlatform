package com.qwqer.adplatform.net.base;

import android.text.TextUtils;
import android.util.Log;

import com.qwqer.adplatform.init.QwQerAdHelper;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.utils.QwQerAdConfig;

import java.security.KeyStore;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Retrofit封装。
 * @author E
 */
public class RetrofitHelper {

    protected ServerRequestInterface serverRequestInterface;

    private String token;
    private String baseUrl = "https://fat-api.igxiaodi.com";

    protected RetrofitHelper(){
        init();
    }

    private void init(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.i("ad_log" , message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) ;


        //支持https 正式上线 这里需要处理，增加安全验证
        X509TrustManager trustManager = null;
        SSLSocketFactory sslSocketFactory = null;
        try{
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            trustManager = (X509TrustManager)trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null , new X509TrustManager[]{trustManager} , null);

            sslSocketFactory = sslContext.getSocketFactory();
        }catch (Exception e){
            e.printStackTrace();
            AdLog.e("====Exception: " + e.getLocalizedMessage());
        }

        if (null != trustManager && null != sslSocketFactory){
            builder.sslSocketFactory(sslSocketFactory , trustManager);
        }

        //headers
        if (null != token){
            Interceptor headerInterceptor = chain -> {
                Request request = chain.request();
                Request.Builder builder1 =request.newBuilder();
                //添加请求头
                builder1.addHeader("token" , token);
                builder1.addHeader("Gxd-Client" , QwQerAdHelper.getClientFlag());
                return chain.proceed(builder1.build());
            };
            builder.addInterceptor(headerInterceptor);
        }else {
            Interceptor headerInterceptor = chain -> {
                Request request = chain.request();
                Request.Builder builder1 =request.newBuilder();
                //添加请求头
//                builder1.addHeader("token" , token);
                builder1.addHeader("Gxd-Client" , QwQerAdHelper.getClientFlag());
                return chain.proceed(builder1.build());
            };
            builder.addInterceptor(headerInterceptor);
        }

        if (TextUtils.isEmpty(baseUrl)){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        serverRequestInterface = retrofit.create(ServerRequestInterface.class);
    }

    public void freshToken(String token){
        this.token = token;
        AdLog.d("====刷新token: " + token);
        init();
    }

    public void setBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)){
            return;
        }
        if (!baseUrl.startsWith("http")
                && !baseUrl.startsWith("https")){
            return;
        }
        this.baseUrl = baseUrl;
        init();
    }

    protected  <T> void requestCallback(Call call , OnRequestCallBackListener<T> onRequestCallBackListener){
        if (null == call){
            return;
        }

        if (null != onRequestCallBackListener){
            onRequestCallBackListener.onStart();
        }
        call.enqueue(new Callback<BaseRetrofitBean<T>>() {
            @Override
            public void onFailure(Call<BaseRetrofitBean<T>> call, Throwable t) {
                if (null == onRequestCallBackListener){
                    return;
                }
                Log.e("ad_log" , "==0==failed===" + t.getLocalizedMessage());
                onRequestCallBackListener.onFailed(0 , t.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call<BaseRetrofitBean<T>> call, Response<BaseRetrofitBean<T>> response) {
                if (null == onRequestCallBackListener){
                    return;
                }
                int code = response.code();
                if (200 != code){
                    onRequestCallBackListener.onFailed(code , response.message());
                    Log.e("ad_log" , "=1===failed===code: " + code + "  " + response.message());
                    return;
                }
                code = response.body().code;
                if (0 == code){
                    //success
                    onRequestCallBackListener.onSuccess(response.body().data);
                    return;
                }
                //failed
                onRequestCallBackListener.onFailed(code , response.body().msg);
            }
        });
    }

}
