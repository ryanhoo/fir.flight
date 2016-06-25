package io.github.ryanhoo.firFlight.network.download;

import android.net.Uri;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/20/16
 * Time: 7:50 PM
 * Desc: DownloadListener
 */
public interface DownloadListener {

    void onStart();

    void onProgress(int current, int total, float progress);

    void onFinish(Uri uri);

    void onFail();
}
