package com.example.findmefood;

import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.findmefood.models.Restaurant;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private static final String TAG = WebViewActivity.class.getName();
    private static final String URL = "URL";
    private ProgressBar web_progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        web_progress_bar = findViewById(R.id.web_progress_bar);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString(URL);
        if (url == null || url.isEmpty()){
            url = "https://www.google.com";
        }
        webView = (WebView) findViewById(R.id.web_view);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG,"OverrideUrlLoading");
                String new_url = request.getUrl().toString();
                view.loadUrl(new_url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d(TAG,"Resource loaded");
                if(webView.getProgress() == 100){
                    web_progress_bar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG,"Page Finished");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG,"Ssl Error");
                handler.proceed();
            }
        });
        webView.loadUrl(url);

    }
}
