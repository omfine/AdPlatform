package com.qwqer.adplatform.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.qwqer.adplatform.R;
import com.qwqer.adplatform.utils.AdLog;

/**
 * 站内跳转页面。
 * @author E
 */
public class AdWebViewActivity extends Activity {

    private WebView webView;
    private Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ad_web_view);

        initViews();
        initListeners();
    }

    private void initViews(){
        returnBtn = findViewById(R.id.returnBtn);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        String userAgent = webView.getSettings().getUserAgentString();
        AdLog.e("========userAgent: " + userAgent);

//        webView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6;Linux;wv;Android o) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36" );

        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url) || (!url.startsWith("http") && !url.startsWith("https") )){
            finish();
            return;
        }
        webView.loadUrl(url);
    }

    private void initListeners(){
        returnBtn.setOnClickListener(view -> {
            if (null == webView){
                finish();
                return;
            }
            if (webView.canGoBack()){
                webView.goBack();
                return;
            }
            finish();
        });
    }



}