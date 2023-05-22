package com.qwqer.adplatform.application;

import android.app.Application;
import com.qwqer.adplatform.init.QwQerAdHelper;
import com.qwqer.adplatform.utils.QwQerAdConfig;

public class AdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        QwQerAdHelper.init(this , "5001121" , 1);
        QwQerAdConfig.deBugMode = true;
        //5315280
        //E6097975B89E83D6
        //F3984C8E81F2CF70
        QwQerAdHelper.init(this , "F3984C8E81F2CF70" , 1);
    }

}
