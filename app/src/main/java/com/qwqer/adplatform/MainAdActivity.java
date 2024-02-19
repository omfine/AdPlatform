package com.qwqer.adplatform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.qwqer.adplatform.ad.AdHelper;
import com.qwqer.adplatform.ad.BannerAdView;
import com.qwqer.adplatform.ad.SplashAdActivity;
import com.qwqer.adplatform.net.RetrofitParamsHelper;
import com.qwqer.adplatform.view.AdWebViewActivity;

public class MainAdActivity extends AppCompatActivity {

    private Button loadBannerBtn;
    private Button setTokenBtn;

    private Button insertScreenBtn;
    private Button splashAdBtn;

    private Button userAdInterfaceBtn;

    private BannerAdView bannerAdView;

    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6InRva2VuIn0.eyJjdXN0b21lclR5cGUiOjIsInBob25lIjoiMTg2ODg3MjAwMzEiLCJhY2NvdW50Tm8iOiJYRDc1MjIwNDIyVk8iLCJjbGllbnRUeXBlRW51bURlc2MiOiJDVVNUT01FUl9BUFAiLCJpZCI6IjQxMyIsInV1aWQiOiJjOTVjZDMwYy0yNzkxLTQzODItYTFjZi04MTM1OTY5Y2E1YjMiLCJjbGllbnRUeXBlVmFsIjoxLCJpYXQiOjE3MDU0NzQ2NTV9.KEP1BTXgnMDjHzzo3l_bziMMC5qcRAudDwYUSPvrgmY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ad);
        initViews();
        initListeners();
    }

    private void initViews(){
        loadBannerBtn = findViewById(R.id.loadBannerBtn);
        setTokenBtn = findViewById(R.id.setTokenBtn);
        insertScreenBtn = findViewById(R.id.insertScreenBtn);
        splashAdBtn = findViewById(R.id.splashAdBtn);
        userAdInterfaceBtn = findViewById(R.id.userAdInterfaceBtn);

        bannerAdView = findViewById(R.id.bannerAdView);
    }

    private void initListeners(){
        loadBannerBtn.setOnClickListener(view ->{
            //banner广告 949191939
            //107EB50EDFE65EA3306C8318FD57D0B3
            //107EB50EDFE65EA3306C8318FD57D0B3
            bannerAdView.loadAd(2 , 1 , 0 , "103224" , false , true);
        });

        setTokenBtn.setOnClickListener(view -> {
            //设置token
            RetrofitParamsHelper.getInstance().freshToken(token);
            RetrofitParamsHelper.getInstance().freshBaseUrl("https://fat-api.igxiaodi.com");
        });

        insertScreenBtn.setOnClickListener(view -> {
            //插屏广告 949196274
            //1D273967F51868AF2C4E080D496D06D0
            AdHelper.showInsertScreenAd(MainAdActivity.this , 10 , 1, "103460");

        });

        splashAdBtn.setOnClickListener(view -> {
            //开屏广告
            startActivity(new Intent(MainAdActivity.this , SplashAdActivity.class));
        });

        userAdInterfaceBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this , AdWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("url" , "https://www.baidu.com");
            startActivity(intent);
        });

    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

}