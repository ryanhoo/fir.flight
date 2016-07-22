package io.github.ryanhoo.firFlight.ui.about;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.ryanhoo.firFlight.BuildConfig;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/24/16
 * Time: 12:33 PM
 * Desc: AboutActivity
 */
public class AboutActivity extends BaseActivity {

    private static final String TAG = "AboutActivity";

    private final static String URL_FIR_IM = "http://fir.im";
    private final static String URL_AUTHOR = "https://ryanhoo.github.io";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_view_app_version)
    TextView textViewViewVersion;
    @Bind(R.id.text_view_flavor_name)
    TextView textViewFlavorName;

    @Override
    @SuppressLint("DefaultLocale")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        supportActionBar(toolbar);

        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            textViewViewVersion.setText(String.format("%s(%d)", versionName, versionCode));
        } catch (Exception e) {
            Log.e(TAG, "Get app version info", e);
        }
        textViewFlavorName.setText(BuildConfig.FLAVOR);
    }

    @OnClick({R.id.button_fir, R.id.button_acknowledgements, R.id.button_author, R.id.text_view_app_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_fir:
                WebViewHelper.openUrl(this, URL_FIR_IM);
                break;
            case R.id.button_acknowledgements:
                // WebViewHelper.openUrl(this, URL_ACKNOWLEDGEMENTS);
                startActivity(new Intent(this, AcknowledgementsActivity.class));
                break;
            case R.id.button_author:
                WebViewHelper.openUrl(this, URL_AUTHOR);
                break;
            case R.id.text_view_app_version:
                if (textViewFlavorName.getVisibility() == View.VISIBLE) {
                    textViewFlavorName.setVisibility(View.GONE);
                } else {
                    textViewFlavorName.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
