package io.github.ryanhoo.firFlight.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/18/16
 * Time: 10:29 PM
 * Desc: WebViewActivity
 */
public class WebViewActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;

    String mTitle;
    String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(EXTRA_URL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        setActionBarTitle();
        setUpWebView(webView);

        webView.loadUrl(mUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(WebView webView) {
        // Settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Chrome
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    mTitle = title;
                    setActionBarTitle();
                }
            }
        });
    }

    private void setActionBarTitle() {
        ActionBar actionBar = supportActionBar(toolbar);
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }
}
