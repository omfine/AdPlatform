package com.qwqer.adplatform.cache;

import com.qwqer.adplatform.bean.AdvertInfoResultBean;
import com.qwqer.adplatform.utils.AdLog;
import com.qwqer.adplatform.utils.QwQerAdConfig;
import java.util.HashMap;
/**
 * 缓存，仅缓存于内存中。
 * @author E
 */
public class TempCacheHelper {

    //保存数据
    private final HashMap<String , AdvertInfoResultBean> dataMap = new HashMap<>();
    //保存时间
    private final HashMap<String , Long> timeMap = new HashMap<>();

    private TempCacheHelper(){}

    private static TempCacheHelper tempCacheHelper;

    private final static Object object = new Object();

    public static TempCacheHelper getInstance(){
        synchronized (object){
            if (null == tempCacheHelper){
                tempCacheHelper = new TempCacheHelper();
            }
        }
        return tempCacheHelper;
    }

    public void save(String key , AdvertInfoResultBean advertInfoResultBean){
        if (!hasExpired(key)){
            //没有超时，不更新数据
            return;
        }
        dataMap.put(key , advertInfoResultBean);
        timeMap.put(key , System.currentTimeMillis()/1000);
    }

    public AdvertInfoResultBean getAdValue(String key){
        if (timeMap.containsKey(key) && dataMap.containsKey(key)){
            //判断是否有超进，超时返回NULL
            if (hasExpired(key)){
                AdLog.e("qwqer_ad==========超时=======更新数据");
                return null;
            }
            AdLog.e("qwqer_ad==========没有超时=======使用缓存数据");
            //没有超时
            return dataMap.get(key);
        }
        AdLog.e("qwqer_ad==========没有缓存数据=======更新数据");
        return null;
    }

    /**
     * 是否超时。
     * @param key
     * @return
     */
    private boolean hasExpired(String key){
        if (!timeMap.containsKey(key)){
            return true;
        }
        long lastTimestamp = timeMap.get(key);
        long ctm = System.currentTimeMillis()/1000;
        long intervalTime = ctm - lastTimestamp;
        return intervalTime > QwQerAdConfig.expiredIntervalSeconds;
    }


}
