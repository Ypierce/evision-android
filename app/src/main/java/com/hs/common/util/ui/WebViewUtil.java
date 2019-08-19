package com.hs.common.util.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hs.activity.BaseActivity;


/**
 * Created by jilil on 2018/12/20.
 */

public class WebViewUtil {

    public static void initWebView(WebView webView, final BaseActivity baseActivity){
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);

        webView.setWebViewClient(new WebViewClient());

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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //6.0以下执行
                //网络未连接
//                baseActivity.showToast("网络未连接");
                view.loadUrl("about:blank");
            }

            //处理网页加载失败时
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                //6.0以上执行
////                baseActivity.showToast("网络未连接");
//                view.loadUrl("about:blank");
//            }

//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                // 这个方法在6.0才出现
//                int statusCode = errorResponse.getStatusCode();
////                if (404 == statusCode || 500 == statusCode) {
////                }
//                view.loadUrl("about:blank");// 避免出现默认的错误界面
//            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    Log.d("", "");
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")){
                    baseActivity.showToast("服务器出现异常");
                }

//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    if (title.contains("404") || title.contains("500") || title.contains("Error")) {
//                        view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    }
//                }
            }


        });
    }
}
