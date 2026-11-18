package com.yttv.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    private WebView web;
    private View customView;
    private WebChromeClient.CustomViewCallback customCallback;
    private FrameLayout root;

    private static final String UA =
        "Mozilla/5.0 (SMART-TV; Linux; Tizen 5.5) AppleWebKit/537.36 (KHTML, like Gecko) "
        + "SamsungBrowser/2.2 Chrome/63.0.3239.84 TV Safari/537.36";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        root = new FrameLayout(this);
        web = new WebView(this);
        root.addView(web);
        setContentView(root);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setUserAgentString(UA);

        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View v, CustomViewCallback cb) {
                customView = v; customCallback = cb;
                root.addView(v);
                web.setVisibility(View.GONE);
            }
            @Override
            public void onHideCustomView() {
                if (customView == null) return;
                root.removeView(customView);
                customView = null;
                customCallback.onCustomViewHidden();
                web.setVisibility(View.VISIBLE);
            }
        });
        web.requestFocus();
        web.loadUrl("https://www.youtube.com/tv");
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent e) {
        if (code == KeyEvent.KEYCODE_BACK) {
            if (customView != null) { web.getWebChromeClient().onHideCustomView(); return true; }
            if (web.canGoBack()) { web.goBack(); return true; }
        }
        return super.onKeyDown(code, e);
    }

    @Override protected void onPause() { super.onPause(); web.onPause(); }
    @Override protected void onResume() { super.onResume(); web.onResume(); }
}
