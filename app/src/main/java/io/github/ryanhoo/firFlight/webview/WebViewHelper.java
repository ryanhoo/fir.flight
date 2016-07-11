package io.github.ryanhoo.firFlight.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.webview.WebViewActivity;
import io.github.ryanhoo.firFlight.webview.chromium.CustomTabActivityHelper;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/24/16
 * Time: 9:31 PM
 * Desc: WebViewHelper
 */
public class WebViewHelper {

    public static void openUrl(final Activity activity, final String title, final String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(null)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.ff_webview_customTabs_toolbar))
                .setShowTitle(true)
                .build();
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(url),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        activity.startActivity(
                                new Intent(activity, WebViewActivity.class)
                                        .putExtra(WebViewActivity.EXTRA_TITLE, title)
                                        .putExtra(WebViewActivity.EXTRA_URL, url)
                        );
                    }
                }
        );
        // */
    }

    public static void openUrl(final Activity activity, final String url) {
        openUrl(activity, null, url);
    }
}
