package com.hello.kaiser.webview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView mWebView;
    private Activity activity;
    public static String FACEBOOK_URL = "https://www.facebook.com/hua.yingxin.9/videos/256684214854665/?hc_ref=ARTnenvMQbNXRjjvZag73twoaqxWfAd3a8D-U11DAVIaopXBFDEcHXMoGsG4KYL7BCQ&fref=gs&dti=1461364913895987&hc_location=group";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        mWebView = (WebView) findViewById(R.id.webview_show);
        WebSettings websettings = mWebView.getSettings();
        websettings.setSupportZoom(true);
        websettings.setJavaScriptEnabled(true);
        websettings.setAppCacheEnabled(true);
        websettings.setSaveFormData(true);
        websettings.setAllowFileAccess(true);
        websettings.setDomStorageEnabled(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setDisplayZoomControls(false);
//        String ua = mWebView.getSettings().getUserAgentString();
//        mWebView.getSettings().setUserAgentString("WebView");

        websettings.setLayoutAlgorithm(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                : WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        websettings.setLoadWithOverviewMode(true);
        websettings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mWebView.setWebViewClient(mWebViewClient);
//        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.loadUrl(FACEBOOK_URL);

    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "checkpoint  = " + url);

            if (url.contains("com.facebook.katana")) {
                try {
                    //確認是否有安裝facebook，假如這串錯誤 直接跳到catch
                    activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    //如果有抓取到url 把吐出來的intent改成fb
                    String facebookUrl = url.replace("intent", "fb");
                    //在外部開啟連結
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                    //在Log確認開啟連結的網址
                    Log.d("MainActivity", "facebookUrl = " + facebookUrl);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(activity, "你沒安裝facebook喔，請先安裝！", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                view.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d(TAG, "error onReceivedError = " + request);
            view.setVisibility(View.GONE);
        }
    };

//    public static Intent newFacebookIntent(PackageManager pm, String url) {
//        Uri uri = Uri.parse(url);
//        try {
//            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
//            if (applicationInfo.enabled) {
//                // http://stackoverflow.com/a/24547437/1048340
//                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
//            }
//        } catch (PackageManager.NameNotFoundException ignored) {
//        }
//        return new Intent(Intent.ACTION_VIEW, uri);
//    }

}
