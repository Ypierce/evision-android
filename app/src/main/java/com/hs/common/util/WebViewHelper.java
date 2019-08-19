package com.hs.common.util;

import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hs.activity.BaseActivity;

public class WebViewHelper {

    public static void loadWebViewPage(String mUrl, WebView mWebView, final BaseActivity baseActivity) {
        if (mUrl != null && mUrl.length() > 0) {
            WebSettings webSettings = mWebView.getSettings();
            //设置WebView属性，能够执行Javascript脚本
            webSettings.setJavaScriptEnabled(true);
            //设置可以访问文件
            webSettings.setAllowFileAccess(true);
            //设置支持缩放
            webSettings.setBuiltInZoomControls(false);

            webSettings.setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.supportMultipleWindows();
            webSettings.setAllowContentAccess(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSavePassword(true);
            webSettings.setSaveFormData(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setLoadsImagesAutomatically(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(mUrl);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    baseActivity.addLoading();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    baseActivity.removeLoading();
                }
            });
            mWebView.loadUrl(mUrl);
        }
    }
}
