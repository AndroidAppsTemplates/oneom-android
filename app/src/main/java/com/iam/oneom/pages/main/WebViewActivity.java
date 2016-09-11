package com.iam.oneom.pages.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.iam.oneom.R;
import com.iam.oneom.core.util.Web;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        webView = (WebView) findViewById(R.id.webView);
        url = getIntent().getStringExtra(getString(R.string.url_for_web_view_activity));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
