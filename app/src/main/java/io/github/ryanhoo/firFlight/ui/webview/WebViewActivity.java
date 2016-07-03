package io.github.ryanhoo.firFlight.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/18/16
 * Time: 10:29 PM
 * Desc: WebViewActivity
 */
public class WebViewActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    String mTitle;
    String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(EXTRA_URL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        ActionBar actionBar = supportActionBar(toolbar);
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_close);
        }
        setActionBarTitle();
        setUpWebView(webView);

        webView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(WebView webView) {
        // Settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // WebView Client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Chrome Client
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    mTitle = title;
                    setActionBarTitle();
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }
}
