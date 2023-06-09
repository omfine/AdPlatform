package com.qwqer.adplatform.ad;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.listeners.OnAdListener;
/**
 * 开屏广告。
 * @author E
 */
public class SplashAdActivity extends AppCompatActivity {

    private SplashAdView splashAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ad);
        initViews();
    }

    private void initViews(){
        splashAdView = findViewById(R.id.splashAdView);
        //887838278
        //7D5239D8D88EBF9B6D317912EDAC6439
        //8F934E519D26C5CD24820FC5BA3E0E4D
        splashAdView.loadAd(1 , 1 , "8F934E519D26C5CD24820FC5BA3E0E4D" , new OnAdListener(){
            @Override
            public void onJump() {
                finish();
            }
        });

    }

}